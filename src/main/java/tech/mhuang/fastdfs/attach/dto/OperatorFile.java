package tech.mhuang.fastdfs.attach.dto;

import lombok.Data;

/**
 *
 * 操作文件类
 *
 * @author mhuang
 * @since 1.0.0
 */
@Data
public class OperatorFile {

	/**
	 * id
	 */
	private String id;

	/**
	 * 文件路径
	 */
	private String url;

	/**
	 * 文件大小
	 */
	private Long size;

	/**
	 * 源文件
	 */
	private String source;

	/**
	 * 文件后缀
	 */
	private String suffix;

	/**
	 * 页数
	 */
	private Integer trunk;

	/**
	 * 父级id
	 */
	private String parentId;
}
