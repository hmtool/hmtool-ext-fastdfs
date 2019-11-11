package tech.mhuang.fastdfs.attach.model;

import java.util.Date;

import tech.mhuang.common.IdEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @ClassName:  AttachRecord   
 * @Description: 附件历史表
 * @author: mhuang
 * @date:   2017年12月4日 下午5:14:14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AttachRecord extends IdEntity {

	private String seqId;//序列id
	
	private Integer systemId;//系统id
	
	private Integer functionId;//功能id
	
	private Date createTime;//新增时间

	private Date updateTime;//修改时间
	
	private String del;//是否删除
	
	private Date operatorTime;
	
	private Integer operatorStatus;
}
