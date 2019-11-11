package tech.mhuang.fastdfs.attach.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import lombok.Data;

/**
 *
 * 文件类
 *
 * @author mhuang
 * @since 1.0.0
 */
@Data
public class OperatorFileVO  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private List<OperatorFile> child = Collections.emptyList();
}
