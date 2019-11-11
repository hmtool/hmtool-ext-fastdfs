package tech.mhuang.fastdfs.attach.model;

import java.util.Date;

import tech.mhuang.common.IdEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @ClassName:  AttachDetail   
 * @Description:附件详情表  
 * @author: mhuang
 * @date:   2017年12月4日 下午5:50:36
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AttachDetail extends IdEntity {

	private String attachId; //附件组id
	
	private String url;//附件url
	
	private Long size;//附件大小
	
	private String source;//附件源

	private Integer trunk = 1;//分片数
	
	private String suffix;//附件后缀名

	private String parentId = "0";//关联附件组id、出现多个分片的存储的情况存在

	private Integer del;//是否删除
	
	private Date createTime;//创建时间
	
	private Date updateTime;//修改时间
}
