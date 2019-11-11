package tech.mhuang.fastdfs.function.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.mhuang.fastdfs.function.mapper.SysFunctionMapper;
import tech.mhuang.fastdfs.function.model.SysFunction;
import tech.mhuang.fastdfs.function.service.ISysFunctionService;

/**
 *
 * 功能Service
 *
 * @author mhuang
 * @since 1.0.0
 */
@Service("sysFunctionService")
public class SysFunctionServiceImpl implements ISysFunctionService{

	@Autowired
	private SysFunctionMapper sysFunctionMapper;
	@Override
	public SysFunction getByTokenPatentName(String systemId, String parentId, String name) {
		return sysFunctionMapper.getBySystemPatentName(systemId, parentId, name);
	}

	@Override
	public int save(SysFunction sysFunction) {
		return sysFunctionMapper.save(sysFunction);
	}

	@Override
	public int saveSystemFunc(String systemId, String functionId) {
		return sysFunctionMapper.saveSystemFunc(systemId, functionId);
	}
}