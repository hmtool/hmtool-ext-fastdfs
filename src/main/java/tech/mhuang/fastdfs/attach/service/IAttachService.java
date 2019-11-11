package tech.mhuang.fastdfs.attach.service;

import java.util.List;

import tech.mhuang.fastdfs.attach.dto.FileModel;
import tech.mhuang.fastdfs.attach.dto.OperatorFile;
import tech.mhuang.fastdfs.attach.dto.OperatorFileVO;
import tech.mhuang.fastdfs.attach.model.AttachDetail;

public interface IAttachService {

    OperatorFileVO save(FileModel fileModel, List<OperatorFile> dataList);
	
    OperatorFileVO update(FileModel fileModel,List<OperatorFile> dataList);
	
	void remove(FileModel fileModel);
	
	OperatorFileVO getById(FileModel fileModel);
	
	List<OperatorFileVO> findBySysFunc(FileModel fileModel);

	List<OperatorFileVO> queryByIds(FileModel fileModel);
	
	AttachDetail getDetailById(FileModel fileModel);
	
    AttachDetail getShard(String id, Integer trunk);
}
