package tech.mhuang.fastdfs.attach.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import tech.mhuang.ext.interchan.protocol.InsertInto;
import tech.mhuang.fastdfs.attach.model.Attach;

/**
 *
 * 附件 组
 *
 * @author mhuang
 * @since 1.0.0
 */
@Mapper
public interface AttachMapper {

	@Insert("INSERT INTO bs_attach(id,system_id,function_id,create_time,update_time) "
		+ "VALUES(#{id},#{systemId},#{functionId},#{createTime},#{updateTime})")
	int save(Attach attach);
	
	@Select("select * from bs_attach where id = #{id} and del = '0'")
	Attach getById(String id);

	@Update("<script>UPDATE bs_attach set "
			+ "<if test=\"functionId != null\">"
			+ 	"function_id=#{functionId},"
			+ "</if>"
			+ "update_time=#{updateTime} where id = #{id}</script>")
	int update(Attach attach);
	
	@Update("UPDATE bs_attach set update_time=#{updateTime},del='1' where id = #{id}")
	int delete(Attach attach);
	
	@Insert("INSERT INTO bs_attach_record(seq_id,id,system_id,function_id,create_time,update_time,del,operator_status) "
			+ "SELECT #{reqNo},id,system_id,function_id,create_time,update_time,del,#{status} FROM bs_attach where id=#{id}")
	int insertIntoRecord(InsertInto<String> insertInto);
}
