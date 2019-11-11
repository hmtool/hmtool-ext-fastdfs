package tech.mhuang.fastdfs.system.model;

import tech.mhuang.common.IdEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * 系统管理
 *
 * @author mhuang
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysSystem extends IdEntity{

	/**
	 * 系统名称
	 */
	private String name;

	/**
	 * 系统token
	 */
	private String token;

	/**
	 * 父级系统
	 */
	private SysSystem parent;

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
