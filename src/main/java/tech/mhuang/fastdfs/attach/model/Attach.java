package tech.mhuang.fastdfs.attach.model;

import java.util.Date;

import tech.mhuang.common.IdEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @ClassName:  Attach   
 * @Description:附件中间件   
 * @author: mhuang
 * @date:   2017年12月4日 下午5:09:10
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Attach extends IdEntity {

	private String systemId;//系统id
	
	private String functionId;//功能id
	
	private Date createTime;//新增时间

	private Date updateTime;//修改时间
	
	private Integer del;//是否删除
}
