package tech.mhuang.fastdfs.function.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import tech.mhuang.fastdfs.function.model.SysFunction;

/**
 *
 * 功能Mapper
 *
 * @author mhuang
 * @since 1.0.0
 */
@Mapper
public interface SysFunctionMapper {

	@Select("SELECT func.* FROM sys_function func "
			+ "INNER JOIN sys_system_function system_func on system_func.function_id = func.id "
			+ "where system_func.system_id = #{systemId} and func.parent_id = #{parentId} and func.name=#{name}")
	SysFunction getBySystemPatentName(@Param("systemId") String systemId,@Param("parentId") String parentId,@Param("name") String name);


	@Insert("INSERT INTO sys_function(id,name,update_time,parent_id) values(#{id},#{name},#{updateTime},#{parentId})")
	int save(SysFunction sysFunction);
	
	@Insert("INSERT INTO sys_system_function(system_id,function_id) values(#{systemId},#{functionId})")
	int saveSystemFunc(@Param("systemId") String systemId,@Param("functionId") String functionId);
}
