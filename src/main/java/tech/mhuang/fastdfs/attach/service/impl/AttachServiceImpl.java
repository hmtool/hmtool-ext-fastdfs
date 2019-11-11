package tech.mhuang.fastdfs.attach.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.mhuang.core.check.CheckAssert;
import tech.mhuang.core.id.BaseIdeable;
import tech.mhuang.ext.interchan.core.exception.BusinessException;
import tech.mhuang.ext.interchan.protocol.InsertInto;
import tech.mhuang.ext.interchan.protocol.Result;
import tech.mhuang.ext.spring.util.DataUtil;
import tech.mhuang.fastdfs.attach.dto.FileModel;
import tech.mhuang.fastdfs.attach.dto.OperatorFile;
import tech.mhuang.fastdfs.attach.dto.OperatorFileVO;
import tech.mhuang.fastdfs.attach.mapper.AttachDetailMapper;
import tech.mhuang.fastdfs.attach.mapper.AttachMapper;
import tech.mhuang.fastdfs.attach.model.Attach;
import tech.mhuang.fastdfs.attach.model.AttachDetail;
import tech.mhuang.fastdfs.attach.service.IAttachService;
import tech.mhuang.fastdfs.function.model.SysFunction;
import tech.mhuang.fastdfs.function.service.ISysFunctionService;
import tech.mhuang.fastdfs.system.dto.SysSystemDTO;
import tech.mhuang.fastdfs.system.service.ISysSystemService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName: AttachServiceImpl
 * @Description:附件Service
 * @author: mhuang
 * @date: 2017年12月4日 下午6:05:50
 */
@Service("/attachService")
@Transactional(readOnly = true)
public class AttachServiceImpl implements IAttachService {

    @Autowired
    private ISysSystemService sysSystemService;

    @Autowired
    private ISysFunctionService sysFunctionService;

    @Autowired
    private AttachMapper attachMapper;

    @Autowired
    private AttachDetailMapper attachDetailMapper;

    @Autowired
    private BaseIdeable<String> ideable;

    private final String NO_RESULT = "-1";

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public String checkFunc(String systemId, String funcs, Boolean created) {
        List<String> funcList = Arrays.asList(funcs.split("①"));
        return funcList.stream().reduce("0", (current, next) -> {
            if (Boolean.TRUE.equals(created) && NO_RESULT.equals(current)) {
                return current;
            }
            SysFunction func = sysFunctionService.getByTokenPatentName(systemId, current, next);
            if (func == null) {
                if (Boolean.TRUE.equals(created)) {
                    func = new SysFunction();
                    func.setId(ideable.generateId());
                    func.setName(next);
                    func.setUpdateTime(new Date());
                    func.setParentId(current);
                    int count = sysFunctionService.save(func);
                    checkZeroException(count);
                    count = sysFunctionService.saveSystemFunc(systemId, func.getId());
                    checkZeroException(count);
                } else {
                    return NO_RESULT;
                }
            }
            return func.getId();
        });
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public OperatorFileVO save(FileModel fileModel, List<OperatorFile> dataList) {
        OperatorFileVO result = new OperatorFileVO();
        Attach attachQry = null;
        if (StringUtils.isEmpty(fileModel.getId())) {
            SysSystemDTO sysSystemDTO = sysSystemService.getToken(fileModel.getToken());
            String funcId = checkFunc(sysSystemDTO.getId(), fileModel.getFunc(), true);
            attachQry = new Attach();
            attachQry.setId(ideable.generateId());
            result.setId(attachQry.getId());
            attachQry.setSystemId(sysSystemDTO.getId());
            attachQry.setFunctionId(funcId);
            attachQry.setUpdateTime(new Date());
            attachQry.setCreateTime(new Date());
            int count = attachMapper.save(attachQry);
            checkZeroException(count);
            InsertInto<String> record = new InsertInto<>();
            record.setId(attachQry.getId());
            record.setReqNo(ideable.generateId());
            record.setStatus(InsertInto.ADD);
            count = attachMapper.insertIntoRecord(record);
            checkZeroException(count);
        } else {
            result.setId(fileModel.getId());
            attachQry = attachMapper.getById(fileModel.getId());
            if (attachQry == null) {
                SysSystemDTO sysSystemDTO = sysSystemService.getToken(fileModel.getToken());
                String funcId = checkFunc(sysSystemDTO.getId(), fileModel.getFunc(), true);
                attachQry = new Attach();
                attachQry.setId(fileModel.getId());
                attachQry.setSystemId(sysSystemDTO.getId());
                attachQry.setFunctionId(funcId);
                attachQry.setUpdateTime(new Date());
                attachQry.setCreateTime(new Date());
                int count = attachMapper.save(attachQry);
                checkZeroException(count);
                InsertInto<String> record = new InsertInto<>();
                record.setId(attachQry.getId());
                record.setReqNo(ideable.generateId());
                record.setStatus(InsertInto.ADD);
                count = attachMapper.insertIntoRecord(record);
                checkZeroException(count);
            }
        }

        List<OperatorFile> operatorFileList = null;
        //若分片发送只能同时发送一个文件
        if (fileModel.getTrunks() > 1) {
            operatorFileList = new ArrayList<>();
            OperatorFile data = dataList.get(0);
            String parentId = null;
            AttachDetail checkDetail = attachDetailMapper.getAttachShard(fileModel.getId(), fileModel.getTrunk());
            CheckAssert.check(checkDetail,"已存在对应分片");
            //当前若是分片数1
            if (fileModel.getTrunk() == 1) {
                parentId = ideable.generateId();
                //先保存完全体
                AttachDetail detail = new AttachDetail();
                detail.setTrunk(fileModel.getTrunks());
                detail.setParentId("0");
                detail.setId(parentId);
                detail.setCreateTime(new Date());
                detail.setUpdateTime(new Date());
                detail.setSource(fileModel.getSource());
                detail.setSuffix(fileModel.getSuffix());
                detail.setSize(fileModel.getSize());
                detail.setDel(0);
                detail.setAttachId(attachQry.getId());
                int dCount = attachDetailMapper.save(detail);
                checkZeroException(dCount);
                InsertInto<String> detailRecord = new InsertInto<>();
                detailRecord.setId(detail.getId());
                detailRecord.setReqNo(ideable.generateId());
                detailRecord.setStatus(InsertInto.ADD);
                dCount = attachDetailMapper.insertIntoRecord(detailRecord);
                checkZeroException(dCount);
            } else {
                List<AttachDetail> detailList = attachDetailMapper.findByAttachId(fileModel);
                if (CollectionUtils.isEmpty(detailList)) {
                    throw new BusinessException(Result.SYS_FAILD, "当前分片没有对应的文件");
                }
                if (detailList.get(0).getTrunk() < fileModel.getTrunk()) {
                    throw new BusinessException(Result.SYS_FAILD, "分片大于原分片数");
                }
                parentId = detailList.get(0).getId();
            }
            //保存分片
            AttachDetail detail = new AttachDetail();
            detail.setTrunk(fileModel.getTrunk());
            detail.setParentId(parentId);
            detail.setUrl(data.getUrl());
            detail.setId(ideable.generateId());
            detail.setCreateTime(new Date());
            detail.setUpdateTime(new Date());
            detail.setSize(data.getSize());
            detail.setDel(0);
            detail.setAttachId(attachQry.getId());
            int dCount = attachDetailMapper.save(detail);
            checkZeroException(dCount);

            InsertInto<String> detailRecord = new InsertInto<>();
            detailRecord.setId(detail.getId());
            detailRecord.setReqNo(ideable.generateId());
            detailRecord.setStatus(InsertInto.ADD);
            dCount = attachDetailMapper.insertIntoRecord(detailRecord);
            checkZeroException(dCount);
        } else {
            final Attach attach = attachQry;
            operatorFileList = dataList.parallelStream().map(data -> {
                AttachDetail detail = DataUtil.copyTo(data, AttachDetail.class);
                detail.setId(ideable.generateId());
                detail.setTrunk(1);
                detail.setParentId("0");
                detail.setAttachId(attach.getId());
                detail.setCreateTime(attach.getUpdateTime());
                detail.setUpdateTime(attach.getUpdateTime());
                int dCount = attachDetailMapper.save(detail);
                checkZeroException(dCount);
                InsertInto<String> detailRecord = new InsertInto<>();
                detailRecord.setId(detail.getId());
                detailRecord.setReqNo(ideable.generateId());
                detailRecord.setStatus(InsertInto.ADD);
                dCount = attachDetailMapper.insertIntoRecord(detailRecord);
                checkZeroException(dCount);
                return DataUtil.copyTo(detail, OperatorFile.class);
            }).collect(Collectors.toList());
        }

        result.setChild(operatorFileList);
        return result;
    }

    private void checkZeroException(int count){
        checkZeroException(count,Result.FAILD_MSG);
    }

    private void checkZeroException(int count,String failMsg){
        if (count == 0) {
            throw new BusinessException(Result.SYS_FAILD, failMsg);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public OperatorFileVO update(FileModel fileModel, List<OperatorFile> dataList) {
        OperatorFileVO result = new OperatorFileVO();
        SysSystemDTO sysSystemDTO = sysSystemService.getToken(fileModel.getToken());
        Attach attach = new Attach();
        attach.setId(fileModel.getId());
        result.setId(attach.getId());
        if (StringUtils.isNotBlank(fileModel.getFunc())) {
            String funcId = checkFunc(sysSystemDTO.getId(), fileModel.getFunc(), true);
            attach.setFunctionId(funcId);
        }
        attach.setUpdateTime(new Date());
        int count = attachMapper.update(attach);
        checkZeroException(count);

        InsertInto<String> record = new InsertInto<>();
        record.setId(attach.getId());
        record.setReqNo(ideable.generateId());
        record.setStatus(InsertInto.UPDATE);
        count = attachMapper.insertIntoRecord(record);
        checkZeroException(count);

        fileModel.getDeleteIdList().parallelStream().forEachOrdered(delete -> {
            AttachDetail detail = new AttachDetail();
            detail.setId(delete);
            detail.setUpdateTime(attach.getUpdateTime());
            attachDetailMapper.delete(detail);


            InsertInto<String> detailRecord = new InsertInto<>();
            detailRecord.setId(detail.getId());
            detailRecord.setReqNo(ideable.generateId());
            detailRecord.setStatus(InsertInto.DELETE);
            attachDetailMapper.insertIntoRecord(detailRecord);
        });

        List<OperatorFile> operatorFileList = dataList.parallelStream().map(data -> {
            AttachDetail detail = DataUtil.copyTo(data, AttachDetail.class);
            detail.setId(ideable.generateId());
            detail.setTrunk(1);
            detail.setParentId("0");
            detail.setAttachId(attach.getId());
            detail.setCreateTime(attach.getUpdateTime());
            detail.setUpdateTime(attach.getUpdateTime());
            int dCount = attachDetailMapper.save(detail);
            checkZeroException(dCount);

            InsertInto<String> detailRecord = new InsertInto<>();
            detailRecord.setId(detail.getId());
            detailRecord.setReqNo(ideable.generateId());
            detailRecord.setStatus(InsertInto.ADD);
            dCount = attachDetailMapper.insertIntoRecord(detailRecord);
            checkZeroException(dCount);

            return DataUtil.copyTo(detail, OperatorFile.class);
        }).collect(Collectors.toList());

        result.setChild(operatorFileList);
        return result;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void remove(FileModel fileModel) {
        Attach attach = new Attach();
        attach.setId(fileModel.getId());
        attach.setUpdateTime(new Date());
        int count = attachMapper.delete(attach);
        checkZeroException(count, "文件组不存在");


        InsertInto<String> record = new InsertInto<>();
        record.setId(attach.getId());
        record.setReqNo(ideable.generateId());
        record.setStatus(InsertInto.DELETE);
        count = attachMapper.insertIntoRecord(record);
        checkZeroException(count, "文件组不存在");

        AttachDetail detail = new AttachDetail();
        detail.setAttachId(fileModel.getId());
        detail.setUpdateTime(attach.getUpdateTime());
        count = attachDetailMapper.delete(detail);
        checkZeroException(count, "文件不存在");

        List<AttachDetail> detailList = attachDetailMapper.findByAttachId(fileModel);

        detailList.parallelStream().forEachOrdered(aDetail -> {
            InsertInto<String> detailRecord = new InsertInto<>();
            detailRecord.setId(aDetail.getId());
            detailRecord.setReqNo(ideable.generateId());
            detailRecord.setStatus(InsertInto.DELETE);

            int aCount = attachDetailMapper.insertIntoRecord(detailRecord);
            checkZeroException(aCount);
        });
    }

    @Override
    public OperatorFileVO getById(FileModel fileModel) {
        List<AttachDetail> detailList = attachDetailMapper.findByAttachId(fileModel);
        OperatorFileVO operatorFileVO = new OperatorFileVO();
        operatorFileVO.setId(fileModel.getId());
        operatorFileVO.setChild(DataUtil.copyTo(detailList, OperatorFile.class));
        return operatorFileVO;
    }

    @Override
    public List<OperatorFileVO> findBySysFunc(FileModel fileModel) {
        SysSystemDTO dto = sysSystemService.getToken(fileModel.getToken());
        String funcId = checkFunc(dto.getId(), fileModel.getFunc(), false);
        if (NO_RESULT.equals(funcId)) {
            return Collections.emptyList();
        }
        List<AttachDetail> detailList = attachDetailMapper.findBySysFunc(dto.getId(), funcId);

        Map<String, List<AttachDetail>> detailMap = detailList.stream().collect(
                Collectors.groupingBy(AttachDetail::getAttachId)
        );
        List<OperatorFileVO> rt = new ArrayList<>(detailMap.size());

        detailMap.forEach((id, list) -> {
            OperatorFileVO operatorFileVO = new OperatorFileVO();
            operatorFileVO.setId(id);
            operatorFileVO.setChild(DataUtil.copyTo(list, OperatorFile.class));
            rt.add(operatorFileVO);
        });
        return rt;
    }

    @Override
    public List<OperatorFileVO> queryByIds(FileModel fileModel) {
        List<AttachDetail> detailList = attachDetailMapper.findByAttachIds(fileModel);
        Map<String, List<AttachDetail>> detailMap = detailList.stream().collect(
                Collectors.groupingBy(AttachDetail::getAttachId)
        );
        List<OperatorFileVO> rt = new ArrayList<>(detailMap.size());

        detailMap.forEach((id, list) -> {
            OperatorFileVO operatorFileVO = new OperatorFileVO();
            operatorFileVO.setId(id);
            operatorFileVO.setChild(DataUtil.copyTo(list, OperatorFile.class));
            rt.add(operatorFileVO);
        });
        return rt;
    }

    @Override
    public AttachDetail getShard(String id, Integer trunk) {
        return attachDetailMapper.getShard(id, trunk);
    }

    @Override
    public AttachDetail getDetailById(FileModel fileModel) {
        return attachDetailMapper.getById(fileModel);
    }
}
