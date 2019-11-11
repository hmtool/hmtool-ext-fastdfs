package tech.mhuang.fastdfs.aop;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import tech.mhuang.ext.interchan.core.exception.BusinessException;
import tech.mhuang.ext.interchan.protocol.Result;
import tech.mhuang.fastdfs.aop.annoation.Auth;
import tech.mhuang.fastdfs.aop.annoation.Level;
import tech.mhuang.fastdfs.aop.annoation.Type;
import tech.mhuang.fastdfs.attach.dto.FileModel;
import tech.mhuang.fastdfs.attach.mapper.AttachMapper;
import tech.mhuang.fastdfs.attach.model.Attach;
import tech.mhuang.fastdfs.system.dto.SysSystemDTO;
import tech.mhuang.fastdfs.system.service.ISysSystemService;


/**
 *
 * 权限验证
 *
 * @author mhuang
 * @since 1.0.0
 */
@Component
@Aspect
public class AuthAspect implements Ordered{

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ISysSystemService sysSystemService;
	
	@Autowired
	private AttachMapper attachMapper;
	
	private final String FAILD =  "0";
	@Pointcut("@annotation(tech.mhuang.fastdfs.aop.annoation.Auth)")
	private void auth() { }  
	
	@Before("auth()")
	public void doBefore(JoinPoint joinPoint) throws Throwable {
		Object[] obj = joinPoint.getArgs();
		MethodSignature method = (MethodSignature)joinPoint.getSignature();   
		Auth auth = getMethodByAuth(method);
		logger.debug("正在打印参数值：",obj);
		logger.debug("打印auth：{}",auth);
		checkType(auth,obj);
	}
	
	/**
	 * 
	 * @Title: checkType   
	 * @Description: 检测类型
	 * @param auth
	 * @param obj
	 * @return void
	 */
	private void checkType(Auth auth,Object[] obj){
		if(auth.type() == Type.File){
			checkFile(auth,(FileModel) obj[0]);
		}
	}
	/**
	 * 
	 * @Title: checkFile   
	 * @Description: 文件权限
	 * @param auth
	 * @param fileModel
	 * @return void
	 */
	private void checkFile(Auth auth,FileModel fileModel){
		SysSystemDTO systemDTO = sysSystemService.getToken(fileModel.getToken());
		if(systemDTO == null){
			throw new BusinessException(Result.SYS_FAILD,"系统不存在");
		}else if(FAILD.equals(systemDTO.getStatus())){
			throw new BusinessException(Result.SYS_FAILD,"系统权限关闭");
		}else{
			Level level = auth.level();
			if(level == Level.QUERY){
				if(FAILD.equals(systemDTO.getQAuth())){
					throw new BusinessException(Result.SYS_FAILD,"没有操作权限");
				}
			}else if(level == Level.SAVE){
				if(FAILD.equals(systemDTO.getSAuth())){
					throw new BusinessException(Result.SYS_FAILD,"没有操作权限");
				}
			}else if(level == Level.UPDATE){
				if(FAILD.equals(systemDTO.getUAuth())){
					throw new BusinessException(Result.SYS_FAILD,"没有操作权限");
				}
			}else if(level == Level.DELETE){
				if(FAILD.equals(systemDTO.getDAuth())){
					throw new BusinessException(Result.SYS_FAILD,"没有操作权限");
				}
			}
			if(auth.check()){
				if(StringUtils.isEmpty(fileModel.getId())){
					throw new BusinessException(Result.SYS_FAILD,"附件组id不存在");
				}
				Attach t = attachMapper.getById(fileModel.getId());
				if(t == null){
					throw new BusinessException(Result.SYS_FAILD,  "附件组不存在");
				}else if(!StringUtils.equals(systemDTO.getId(), t.getSystemId())){
					throw new BusinessException(Result.SYS_FAILD,  "附件于系统不一致");
				}
			}
		}
	}
	
	private Auth getMethodByAuth(MethodSignature methodSignature){
	    Method method0 =  methodSignature.getMethod();
	    return method0.getAnnotation(Auth.class);
    }    
	
	@Override
	public int getOrder() {
		return 10;
	}
}
