package tech.mhuang.fastdfs.attach.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import tech.mhuang.ext.interchan.protocol.InsertInto;
import tech.mhuang.fastdfs.attach.dto.FileModel;
import tech.mhuang.fastdfs.attach.model.AttachDetail;

/**
 *
 * 附件详情
 *
 * @author mhuang
 * @since 1.0.0
 */
@Mapper
public interface AttachDetailMapper {

	@Insert("INSERT INTO bs_attach_detail(id,attach_id,url,size,source,suffix,create_time,update_time,trunk,parent_id) "
			+ "VALUES(#{id},#{attachId},#{url},#{size},#{source},#{suffix},#{createTime},#{updateTime},#{trunk},#{parentId})")
	int save(AttachDetail detail);
	
	@Update("<script>update bs_attach_detail set del = '1',update_time=#{updateTime} "
		+ "where del = '0' "
		+ 	"<if test=\"id != null\">"
		+ 		"and id=#{id}"
		+ 	"</if>"
		+ 	"<if test=\"attachId != null\">"
		+ 		"and attach_id = #{attachId}"
		+ 	"</if></script>"
	)
	int delete(AttachDetail detail);
	
	@Insert("INSERT INTO bs_attach_detail_record(seq_id,id,attach_id,url,size,source,suffix,del,trunk,parent_id,create_time,update_time,operator_status) "
			+ "SELECT #{reqNo},id,attach_id,url,size,source,suffix,del,trunk,parent_id,create_time,update_time,#{status} FROM bs_attach_detail where id=#{id}")
	int insertIntoRecord(InsertInto<String> insertInto);
	
	@Select("select detail.* from "
			+ "bs_attach attach "
			+ "inner join bs_attach_detail detail on detail.attach_id = attach.id "
			+ "inner join sys_system_function sys_func ON sys_func.function_id = attach.function_id "
			+ "and sys_func.system_id = attach.system_id "
			+ "inner join sys_system system on system.id = sys_func.system_id and system.token = #{token} "
			+ "where detail.attach_id = #{id} "
			+ "and detail.del = '0' and detail.parent_id = '0' ")
	List<AttachDetail> findByAttachId(FileModel fileModel);

	@Select("select detail.* from "
			+ "bs_attach attach  "
			+ "inner join bs_attach_detail detail on detail.attach_id = attach.id "
			+ "inner join sys_system_function sys_func ON sys_func.function_id = attach.function_id "
			+ "and sys_func.system_id = attach.system_id "
			+ "inner join sys_system system on system.id = sys_func.system_id and system.token = #{token} "
			+ "where detail.id = #{id} "
			+ "and detail.del = '0' and attach.del = '0' and detail.parent_id = '0'")
	AttachDetail getById(FileModel fileModel);
	
	@Select("<script>select detail.* from "
			+ "bs_attach attach  "
			+ "inner join bs_attach_detail detail on detail.attach_id = attach.id "
			+ "inner join sys_system_function sys_func ON sys_func.function_id = attach.function_id "
			+ "and sys_func.system_id = attach.system_id "
			+ "inner join sys_system system on system.id = sys_func.system_id and system.token = #{token} "
			+ "where detail.del = '0' and attach.del = '0' and detail.parent_id = '0' and detail.attach_id in "
			+ "<foreach collection=\"idList\" item=\"item\" separator=\",\" open=\"(\" close=\")\" index=\"index\">#{item}</foreach></script>")
	List<AttachDetail> findByAttachIds(FileModel fileModel);

	@Select("select detail.* from bs_attach_detail detail inner join bs_attach attach on attach.id= detail.attach_id  where system_id = #{systemId} and function_id=#{funcId} and detail.del = '0' and detail.parent_id = '0' and attach.del='0'")
	List<AttachDetail> findBySysFunc(@Param(value = "systemId") String systemId,@Param(value = "funcId") String funcId);

	@Select("select detail.* from "
			+ "bs_attach attach "
			+ "inner join bs_attach_detail detail on detail.attach_id = attach.id "
			+ "where detail.parent_id = #{parentId} and trunk = #{trunk} "
			+ "and detail.del = '0' and attach.del = '0'")
	AttachDetail getShard(@Param(value = "parentId") String id, @Param("trunk") Integer trunk);

	@Select("select detail.* from bs_attach attach " +
			"inner join bs_attach_detail detail on detail.attach_id = attach.id " +
			"where detail.attach_id = #{attachId} and trunk = #{trunk} and detail.del = '0' " +
			"and attach.del = '0' and parent_id != '0'")
	AttachDetail getAttachShard(@Param(value = "attachId") String attachId, @Param(value = "trunk") Integer trunk);
}