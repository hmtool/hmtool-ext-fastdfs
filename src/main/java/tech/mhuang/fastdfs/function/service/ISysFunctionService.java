package tech.mhuang.fastdfs.function.service;

import tech.mhuang.fastdfs.function.model.SysFunction;

public interface ISysFunctionService {

	SysFunction getByTokenPatentName(String systemId, String parentId, String name);
	
	int save(SysFunction sysFunction);
	
	int saveSystemFunc(String systemId,String functionId);
}
