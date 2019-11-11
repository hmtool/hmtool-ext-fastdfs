package tech.mhuang.fastdfs.common;

import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorageNode;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.fdfs.ThumbImageConfig;
import com.github.tobato.fastdfs.domain.proto.storage.StorageSetMetadataCommand;
import com.github.tobato.fastdfs.domain.proto.storage.StorageUploadFileCommand;
import com.github.tobato.fastdfs.domain.proto.storage.StorageUploadSlaveFileCommand;
import com.github.tobato.fastdfs.domain.proto.storage.enums.StorageMetadataSetType;
import com.github.tobato.fastdfs.exception.FdfsUnsupportImageTypeException;
import com.github.tobato.fastdfs.exception.FdfsUploadImageException;
import com.github.tobato.fastdfs.service.DefaultFastFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 基于github上进行二次修改、生成缩略图按照自身的业务场景生成对应x50,x100,x150比例的图
 *
 * @author mhuang
 * @since 1.0.0
 */
@Component("commonFastClient")
public class CommonFastFileStorageClient extends DefaultFastFileStorageClient implements FastFileStorageClient {

    /**
     * 支持的图片类型
     */
    private static final String[] SUPPORT_IMAGE_TYPE = {"JPG", "JPEG", "PNG", "GIF", "BMP", "WBMP"};
    private static final List<String> SUPPORT_IMAGE_LIST = Arrays.asList(SUPPORT_IMAGE_TYPE);
    /**
     * 缩略图生成配置
     */
    @Resource
    private ThumbImageConfig thumbImageConfig;

    /**
     * 上传文件
     */
    @Override
    public StorePath uploadFile(InputStream inputStream, long fileSize, String fileExtName, Set<MetaData> metaDataSet) {
        Validate.notNull(inputStream, "上传文件流不能为空");
        Validate.notBlank(fileExtName, "文件扩展名不能为空");
        StorageNode client = trackerClient.getStoreStorage();
        return uploadFileAndMateData(client, inputStream, fileSize, fileExtName, metaDataSet);
    }

    /**
     * 上传图片并且生成缩略图
     */
    @Override
    public StorePath uploadImageAndCrtThumbImage(InputStream inputStream, long fileSize, String fileExtName,
                                                 Set<MetaData> metaDataSet) {
        Validate.notNull(inputStream, "上传文件流不能为空");
        Validate.notBlank(fileExtName, "文件扩展名不能为空");
        // 检查是否能处理此类图片
        if (!isSupportImage(fileExtName)) {
            throw new FdfsUnsupportImageTypeException("不支持的图片格式" + fileExtName);
        }
        StorageNode client = trackerClient.getStoreStorage();
        byte[] bytes = inputStreamToByte(inputStream);

        // 上传文件和mateData
        StorePath path = uploadFileAndMateData(client, new ByteArrayInputStream(bytes), fileSize, fileExtName,
                metaDataSet);
        // 上传缩略图
        uploadThumbImage(client, bytes, path.getPath(), fileExtName);
        bytes = null;
        return path;
    }

    /**
     * 获取byte流
     *
     * @param inputStream
     * @return
     */
    private byte[] inputStreamToByte(InputStream inputStream) {
        try {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            LOGGER.error("image inputStream to byte error", e);
            throw new FdfsUploadImageException("upload ThumbImage error", e.getCause());
        }
    }

    /**
     * 检查是否有MateData
     *
     * @param metaDataSet
     * @return
     */
    private boolean hasMateData(Set<MetaData> metaDataSet) {
        return null != metaDataSet && !metaDataSet.isEmpty();
    }

    /**
     * 是否是支持的图片文件
     *
     * @param fileExtName
     * @return
     */
    private boolean isSupportImage(String fileExtName) {
        return SUPPORT_IMAGE_LIST.contains(fileExtName.toUpperCase());
    }

    /**
     * 上传文件和元数据
     *
     * @param client
     * @param inputStream
     * @param fileSize
     * @param fileExtName
     * @param metaDataSet
     * @return
     */
    private StorePath uploadFileAndMateData(StorageNode client, InputStream inputStream, long fileSize,
                                            String fileExtName, Set<MetaData> metaDataSet) {
        // 上传文件
        StorageUploadFileCommand command = new StorageUploadFileCommand(client.getStoreIndex(), inputStream,
                fileExtName, fileSize, false);
        StorePath path = connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
        // 上传matadata
        if (hasMateData(metaDataSet)) {
            StorageSetMetadataCommand setMDCommand = new StorageSetMetadataCommand(path.getGroup(), path.getPath(),
                    metaDataSet, StorageMetadataSetType.STORAGE_SET_METADATA_FLAG_OVERWRITE);
            connectionManager.executeFdfsCmd(client.getInetSocketAddress(), setMDCommand);
        }
        return path;
    }

    /**
     * 上传缩略图
     *
     * @param client         客户端
     * @param dates          数据
     * @param masterFilename 原文件名
     * @param fileExtName    扩展名
     */
    private void uploadThumbImage(StorageNode client, byte[] dates, String masterFilename,
                                  String fileExtName) {
        ByteArrayInputStream thumbImageStream = null;
        try {
            for (int i = 0; i < 3; i++) {
                int width = 50 + 50 * i, height = width;
                thumbImageStream = getThumbImageStream(new ByteArrayInputStream(dates), width, height);
                // 获取文件大小
                long fileSize = thumbImageStream.available();
                // 获取缩略图前缀
                StringBuilder buffer = new StringBuilder();
                buffer.append("_").append(width).append("x").append(height);
                String prefixName = buffer.toString();
                StorageUploadSlaveFileCommand command = new StorageUploadSlaveFileCommand(thumbImageStream, fileSize,
                        masterFilename, prefixName, fileExtName);
                connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);

            }
        } catch (IOException e) {
            LOGGER.error("upload ThumbImage error", e);
            throw new FdfsUploadImageException("upload ThumbImage error", e.getCause());
        } finally {
            IOUtils.closeQuietly(thumbImageStream);
        }
    }

    /**
     * 获取缩略图
     *
     * @param inputStream 原IO
     * @param width       宽度
     * @param height      长度
     * @return
     * @throws IOException
     */
    private ByteArrayInputStream getThumbImageStream(InputStream inputStream, int width, int height) throws IOException {
        // 在内存当中生成缩略图
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //@formatter:off
        Thumbnails
                .of(inputStream)
                .size(width, height)
                .toOutputStream(out);
        //@formatter:on
        return new ByteArrayInputStream(out.toByteArray());
    }

    /**
     * 删除文件
     */
    @Override
    public void deleteFile(String filePath) {
        StorePath storePath = StorePath.parseFromUrl(filePath);
        super.deleteFile(storePath.getGroup(), storePath.getPath());
    }
}
