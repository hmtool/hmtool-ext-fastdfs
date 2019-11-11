package tech.mhuang.fastdfs.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import tech.mhuang.fastdfs.system.model.SysSystem;

/**
 *
 * 系统Mapper
 *
 * @author mhuang
 * @since 1.0.0
 */
@Mapper
public interface SysSystemMapper {

	@Select("select * from sys_system where token = #{token}")
	SysSystem getToken(String token);

	@Select("select system.* from sys_system_function systemFunc "
			+ "inner join sys_system system on systemFunc.system_id = system.id and systemFunc.function_id = #{funcId} "
			+ "where system.token = #{token}")
	SysSystem getByTokenFunc(@Param(value = "token") String token,@Param(value = "funcId") String funcId);
}
