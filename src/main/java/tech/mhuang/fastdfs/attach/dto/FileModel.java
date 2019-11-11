package tech.mhuang.fastdfs.attach.dto;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import tech.mhuang.common.IdEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 *
 * 文件核心类
 *
 * @author mhuang
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FileModel extends IdEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 功能id
	 */
	private String funcId;

	/**
	 * 目录名
	 */
	private String func;

	/**
	 * 系统token
	 */
	private String token;
	
	private List<String> idList = new LinkedList<>();
	
	private List<OperatorFile> addUrl = new LinkedList<>();
	
	private List<String> deleteIdList = new LinkedList<>();
	
	private Integer watermark = 0;

	/**
	 * 水印url
	 */
	private String watermarkUrl;

	/**
	 * 请求类型（1代表二维码、其他代表默认上传）
	 */
	private Integer reqType;

	/**
	 * 二维码的信息
	 */
	private String reqParams;

	/**
	 * 路径
	 */
	private String url;

	/**
	 * 当前分片数
	 */
	private Integer trunk = 1;

	/**
	 * 总分片数
	 */
	private Integer trunks = 1;

	/**
	 * 分片时使用--总大小
	 */
	private Long size;

	/**
	 * 分片时使用--文件名
	 */
	private String source;

	/**
	 * 分片时使用-后缀名
	 */
	private String suffix;
}
