package tech.mhuang.fastdfs;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.util.unit.DataSize;
import tech.mhuang.ext.interchan.core.application.BaseApplication;

/**
 *
 * Fastdfs启动程序
 *
 * @author mhuang
 * @since 1.0.0
 */
@Import(FdfsClientConfig.class)
@SpringBootApplication
public class FastDFSClientApplication extends BaseApplication
{
	
	@Bean
	public MultipartConfigElement multipartConfigElement(){ 
		MultipartConfigFactory config = new MultipartConfigFactory(); 
		config.setMaxFileSize(DataSize.ofMegabytes(80L));
		config.setMaxRequestSize(DataSize.ofMegabytes(80L));
		//文件上传默认备份路径，建议在发布环境自行指定
//		config.setLocation("/mnt/backup");
		return config.createMultipartConfig(); 
	} 
	
    public static void main( String[] args )
    {
    	SpringApplication.run(FastDFSClientApplication.class, args);
    }
}
