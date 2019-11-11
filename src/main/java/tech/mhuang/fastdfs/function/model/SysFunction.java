package tech.mhuang.fastdfs.function.model;

import java.util.Date;

import tech.mhuang.common.IdEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @ClassName:  SysFunction   
 * @Description:功能
 * @author: mhuang
 * @date:   2017年12月4日 下午7:20:20
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysFunction extends IdEntity{

	private String name;//功能id
	
	private Date createTime;//创建时间

	private Date updateTime;//创建时间
	
	private SysFunction parent;//上级功能
	
	private String parentId; //上级
}
