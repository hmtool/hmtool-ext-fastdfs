package tech.mhuang.fastdfs.system.dto;

import lombok.Data;

/**
 *
 * 系统显示VO
 *
 * @author mhuang
 * @since 1.0.0
 */
@Data
public class SysSystemVO {

	/**
	 * 系统id
	 */
	private String id;

	/**
	 * 系统名称
	 */
	private String name;

	/**
	 * 系统token
	 */
	private String token;

	/**
	 * 状态
	 */
	private String status;

	/**
	 * 查询权限
	 */
	private String qAuth;

	/**
	 * 插入权限
	 */
	private String sAuth;

	/**
	 * 修改权限
	 */
	private String uAuth;

	/**
	 * 删除权限
	 */
	private String dAuth;
}
