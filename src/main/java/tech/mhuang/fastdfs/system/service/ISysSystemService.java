package tech.mhuang.fastdfs.system.service;

import tech.mhuang.fastdfs.system.dto.SysSystemDTO;

public interface ISysSystemService {

	SysSystemDTO getToken(String token);
	
	
	SysSystemDTO getByTokenFunc(String token,String funcId);
}
