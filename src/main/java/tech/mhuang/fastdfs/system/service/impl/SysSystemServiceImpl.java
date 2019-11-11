package tech.mhuang.fastdfs.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.mhuang.ext.spring.util.DataUtil;
import tech.mhuang.fastdfs.system.dto.SysSystemDTO;
import tech.mhuang.fastdfs.system.mapper.SysSystemMapper;
import tech.mhuang.fastdfs.system.service.ISysSystemService;

/**
 *
 * 系统Service
 *
 * @author mhuang
 * @since 1.0.0
 */
@Service("sysSystemService")
public class SysSystemServiceImpl implements ISysSystemService {

    @Autowired
    private SysSystemMapper sysSystemMapper;

    @Override
    public SysSystemDTO getToken(String token) {
        return DataUtil.copyTo(sysSystemMapper.getToken(token), SysSystemDTO.class);
    }

    @Override
    public SysSystemDTO getByTokenFunc(String token, String funcId) {
        return DataUtil.copyTo(sysSystemMapper.getByTokenFunc(token, funcId), SysSystemDTO.class);
    }

}
