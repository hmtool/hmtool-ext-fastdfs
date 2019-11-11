package tech.mhuang.fastdfs.aop.annoation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/**
 *
 * 权限
 *
 * @author mhuang
 * @since 1.0.0
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface Auth {

	/**
	 * 类型
	 * @return 文件
	 */
	Type type() default Type.File;

	/**
	 * 权限等级
	 * query代表查看  save代表新增、update代表修改、delete代表删除
	 * @return 所有
	 */
	Level level() default Level.ALL;

	/**
	 * 是否检测详情
	 * @return true
	 */
	boolean check() default true;
}
