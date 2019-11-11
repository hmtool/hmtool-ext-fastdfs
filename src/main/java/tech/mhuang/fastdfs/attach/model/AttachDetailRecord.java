package tech.mhuang.fastdfs.attach.model;

import java.util.Date;

import tech.mhuang.common.IdEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AttachDetailRecord extends IdEntity {

	private String seqId;//序列id
	
	private String attachId; //附件组id
	
	private String url;//附件url
	
	private String size;//附件大小
	
	private String source;//附件源
	
	private Integer trunk = 1;//分片数

	private String suffix;//附件后缀名

	private String parentId = "0";//关联附件组id、出现多个分片的存储的情况存在

	private String del;//是否删除
	
	private Date createTime;//创建时间
	
	private Date updateTime;//修改时间
	
	private Date operatorTime;
	
	private Integer operatorStatus;
}
