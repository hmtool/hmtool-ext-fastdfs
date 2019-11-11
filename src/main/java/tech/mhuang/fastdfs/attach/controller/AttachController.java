package tech.mhuang.fastdfs.attach.controller;

import org.apache.tomcat.util.codec.binary.Base64;
import tech.mhuang.ext.interchan.core.exception.BusinessException;
import tech.mhuang.ext.interchan.protocol.Result;
import tech.mhuang.fastdfs.common.QRCode;
import tech.mhuang.core.id.BaseIdeable;
import tech.mhuang.fastdfs.aop.annoation.Auth;
import tech.mhuang.fastdfs.aop.annoation.Level;
import tech.mhuang.fastdfs.attach.dto.FileModel;
import tech.mhuang.fastdfs.attach.dto.OperatorFile;
import tech.mhuang.fastdfs.attach.dto.OperatorFileVO;
import tech.mhuang.fastdfs.attach.dto.TrunkFile;
import tech.mhuang.fastdfs.attach.model.AttachDetail;
import tech.mhuang.fastdfs.attach.service.IAttachService;
import tech.mhuang.fastdfs.common.FastDFSClientWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * 核心接入控制层
 *
 * @author mhuang
 * @since 1.0.0
 */
@RequestMapping("/attach")
@RestController("/attach")
public class AttachController {

    @Autowired
    private IAttachService attachService;

    @Autowired
    private FastDFSClientWrapper dfsClient;

    @Autowired
    private BaseIdeable<String> ideable;

    private void packUploadFileData(MultipartFile file, List<OperatorFile> dataList) {
        try {
            dataList.add(dfsClient.uploadFile(file));
        } catch (IOException e) {
            throw new BusinessException(Result.SYS_FAILD, "上传图片失败", e);
        }
    }

    @Auth(level = Level.QUERY, check = false)
    @GetMapping("/generatorId")
    public Result generatorId(@ModelAttribute FileModel fileModel) {
        return Result.ok(ideable.generateId());
    }

    @Auth(level = Level.SAVE, check = false)
    @PostMapping(value = "/save")
    public Result save(FileModel fileModel, List<MultipartFile> file) {
        if (StringUtils.isEmpty(fileModel.getFunc())) {
            throw new BusinessException(Result.SYS_FAILD, "请输入你上传的文件的功能点");
        }

        List<OperatorFile> dataList = saveProccess(fileModel, file,true);
        OperatorFileVO fileVo = attachService.save(fileModel, dataList);
        return Result.ok(fileVo.getId());
    }

    private void upload(FileModel fileModel, List<OperatorFile> dataList) {
        if (StringUtils.isEmpty(fileModel.getReqParams())) {
            throw new BusinessException(Result.SYS_FAILD, "请输入你上传得二维码内容");
        }
        byte[] qrCodeByte = QRCode.generate(fileModel.getReqParams());
        try {
            dataList.add(dfsClient.updateFile(qrCodeByte, QRCode.FORMAT));
        } catch (IOException e) {
            throw new BusinessException(Result.SYS_FAILD, "上传图片失败", e);
        }
    }

    private List<OperatorFile> saveProccess(FileModel fileModel, List<MultipartFile> file,boolean check) {
        if(fileModel.getTrunk() < 1){
            throw new BusinessException(Result.SYS_FAILD, "分片必须>=1");
        }
        if(fileModel.getTrunk() > fileModel.getTrunks()){
            throw new BusinessException(Result.SYS_FAILD, "分片大于原分片数");
        }
        List<OperatorFile> dataList = new LinkedList<>();
        if (null != fileModel.getReqType() && fileModel.getReqType() == 1) {
            upload(fileModel, dataList);
        } else {
            if (check && CollectionUtils.isEmpty(file)) {
                throw new BusinessException(Result.SYS_FAILD, "请选择上传的文件");
            }
            file.forEach(f -> packUploadFileData(f, dataList));
        }
        return dataList;
    }

    @Auth(level = Level.SAVE, check = false)
    @PostMapping(value = "/saveRestFile")
    public Result saveRestFile(FileModel fileModel, List<MultipartFile> file) {
        List<OperatorFile> dataList = saveProccess(fileModel, file,true);
        return Result.ok(attachService.save(fileModel, dataList));
    }

    @Auth(level = Level.UPDATE)
    @PostMapping("/update")
    public Result update(FileModel fileModel, List<MultipartFile> file) {
        List<OperatorFile> dataList = saveProccess(fileModel, file, false);
        return Result.ok(attachService.update(fileModel, dataList));
    }

    @Auth(level = Level.UPDATE)
    @PostMapping("/updateQryCurrent")
    public Result updateQryCurrent(FileModel fileModel, List<MultipartFile> file) {
        List<OperatorFile> dataList = new LinkedList<>();
        if (null != fileModel.getReqType() && fileModel.getReqType() == 1) {
            upload(fileModel, dataList);
        } else {
            if (!CollectionUtils.isEmpty(file)) {
                file.forEach(f -> packUploadFileData(f, dataList));
            }
        }
        attachService.update(fileModel, dataList);
        FileModel qryModel = new FileModel();
        qryModel.setId(fileModel.getId());
        qryModel.setToken(fileModel.getToken());
        return getById(qryModel);
    }

    @Auth(level = Level.DELETE)
    @DeleteMapping(value = "/remove")
    public Result remove(@ModelAttribute FileModel fileModel) {
        attachService.remove(fileModel);
        return Result.ok();
    }

    @Auth(level = Level.QUERY, check = false)
    @GetMapping(value = "/getById")
    public Result getById(@ModelAttribute FileModel fileModel) {
        return Result.ok(attachService.getById(fileModel));
    }

    @Auth(level = Level.QUERY, check = false)
    @GetMapping(value = "/query")
    public Result query(@ModelAttribute FileModel fileModel) {
        return Result.ok(attachService.findBySysFunc(fileModel));
    }

    @Auth(level = Level.QUERY, check = false)
    @GetMapping(value = "/queryByIds")
    public Result queryByIds(@ModelAttribute FileModel fileModel) {
        return Result.ok(attachService.queryByIds(fileModel));
    }

    private static final int shard_byte_count = 1024000;

    @Auth(level = Level.QUERY, check = false)
    @GetMapping("/getTrunk")
    public Result getTrunk(@ModelAttribute FileModel fileModel) {
        TrunkFile trunkFile = new TrunkFile();
        trunkFile.setUrl(fileModel.getUrl());
        byte[] fileData = dfsClient.downloadFile(trunkFile.getUrl());
        String data = Base64.encodeBase64String(fileData);
        int dataLen = data.length();
        if (dataLen % shard_byte_count > 0) {
            trunkFile.setTrunk((dataLen / shard_byte_count) + 1);
        } else {
            int downStarLen = dataLen / shard_byte_count;
            if (downStarLen > 0) {
                trunkFile.setTrunk(downStarLen);
            } else {
                trunkFile.setTrunk(1);
            }
        }
        return Result.ok(trunkFile);
    }

    @Auth(level = Level.QUERY, check = false)
    @GetMapping("/downloadTrunk")
    public Result downloadTrunk(@ModelAttribute FileModel fileModel) {
        TrunkFile trunkFile = new TrunkFile();
        trunkFile.setUrl(fileModel.getUrl());
        trunkFile.setTrunk(fileModel.getTrunk());
        //TODO 建议放入redis或者其他地方、不用每次下载都在程序中去下载
        byte[] fileData = dfsClient.downloadFile(fileModel.getUrl());
        String data = Base64.encodeBase64String(fileData);
        int dataLen = data.length();
        int beginSubLen = (fileModel.getTrunk() - 1) * shard_byte_count;
        if (beginSubLen > dataLen) {
            return new Result(500, "当前长度大于可取的长度");
        } else {
            int nextSubLen = fileModel.getTrunk() * shard_byte_count;
            if (nextSubLen >= dataLen) {
                nextSubLen = dataLen - 1;
            }
            trunkFile.setData(data.substring(beginSubLen, nextSubLen));
            return Result.ok(trunkFile);
        }
    }

    @Auth(level = Level.QUERY, check = false)
    @GetMapping("/download")
    public ResponseEntity<byte[]> download(@ModelAttribute FileModel fileModel) throws UnsupportedEncodingException {
        if(StringUtils.isEmpty(fileModel.getId())){
            throw new BusinessException(Result.SYS_FAILD, "请传递下载的文件id");
        }
        AttachDetail detail = attachService.getDetailById(fileModel);
        if (detail == null) {
            throw new BusinessException(Result.SYS_FAILD, "文件不存在");
        }
        String source = detail.getSource();
        String url = detail.getUrl();
        //若此文件是分片、则必须带有下载分片的属性
        if ("0".equals(detail.getParentId()) && detail.getTrunk() > 1) {
            AttachDetail shardDetail = attachService.getShard(fileModel.getId(), fileModel.getTrunk());
            if (shardDetail == null) {
                throw new BusinessException(Result.SYS_FAILD, "文件中的分片不存在");
            }
            url = shardDetail.getUrl();
        }
        byte[] fileData = dfsClient.downloadFile(url);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment",
                new String(source.getBytes("UTF-8"), "iso-8859-1"));
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(fileData, headers, HttpStatus.CREATED);
    }
}
