package tech.mhuang.fastdfs.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.github.tobato.fastdfs.exception.FdfsUnsupportStorePathException;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import tech.mhuang.fastdfs.attach.dto.OperatorFile;

@Component
public class FastDFSClientWrapper {

    private final Logger logger = LoggerFactory.getLogger(FastDFSClientWrapper.class);

    private static final String[] SUPPORT_IMAGE_TYPE = { "JPG", "JPEG", "PNG", "GIF", "BMP", "WBMP" };
    private static final List<String> SUPPORT_IMAGE_LIST = Arrays.asList(SUPPORT_IMAGE_TYPE);
    
    @Qualifier("commonFastClient")
    @Autowired
    private FastFileStorageClient storageClient;

    /** * 上传文件 * @param file 文件对象 * @return 文件访问地址 * @throws IOException */
    public OperatorFile uploadFile(MultipartFile file) throws IOException {
    	Long size = file.getSize();
    	InputStream in = file.getInputStream();
    	String suffix = FilenameUtils.getExtension(file.getOriginalFilename());
    	StorePath storePath = null;
    	if(SUPPORT_IMAGE_LIST.contains(suffix.toUpperCase())){
    		storePath = storageClient.uploadImageAndCrtThumbImage(in, size, suffix, null);
    	}else{
    		storePath = storageClient.uploadFile(in,size, suffix,null);
    	}
        
        OperatorFile operatorFile = new OperatorFile();
        operatorFile.setSize(size);
        operatorFile.setSource(file.getOriginalFilename());
        operatorFile.setUrl(getResAccessUrl(storePath));
        operatorFile.setSuffix(suffix);
        in.close();
        return operatorFile;
    }

    /** * 将一段字符串生成一个文件上传 * @param content 文件内容 * @param fileExtension * @return 
     * @throws IOException */
    public OperatorFile uploadFile(String content, String fileExtension) throws IOException {
        byte[] buff = content.getBytes(Charset.forName("UTF-8"));
        return updateFile(buff,fileExtension);
    }
    
    public OperatorFile updateFile(byte[] buff,String fileExtension) throws IOException{
    	Long size = (long) buff.length;
    	ByteArrayInputStream stream = new ByteArrayInputStream(buff);
    	StorePath storePath = storageClient.uploadFile(stream,size, fileExtension,null);
    	OperatorFile operatorFile = new OperatorFile();
        operatorFile.setSize(size);
        operatorFile.setSource("generate");
        operatorFile.setUrl(getResAccessUrl(storePath));
        operatorFile.setSuffix(fileExtension);
        stream.close();
        return operatorFile;
    }

    // 封装图片完整URL地址
    private String getResAccessUrl(StorePath storePath) {
    	
        return  storePath.getFullPath();
    }
   
    public byte[] downloadFile(String fileUrl){
    	DownloadByteArray callback = new DownloadByteArray();
    	StorePath storePath = StorePath.parseFromUrl(fileUrl);
        byte[] content = storageClient.downloadFile(storePath.getGroup(), storePath.getPath(), callback);
        return content;
    }

    /** * 删除文件 * @param fileUrl 文件访问地址 * @return */
    public void deleteFile(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            return;
        }
        try {
            StorePath storePath = StorePath.parseFromUrl(fileUrl);
            storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
        } catch (FdfsUnsupportStorePathException e) {
            logger.warn(e.getMessage());
        }
    }
}