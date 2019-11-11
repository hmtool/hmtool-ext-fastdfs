package tech.mhuang.fastdfs.aop.annoation;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @ClassName:  Type   
 * @Description:类型
 * @author: mhuang
 * @date:   2017年12月5日 下午2:35:39
 */
public enum Type {

    /**
     * File
     */
	File(1),System(2),Function(3);
	

	@Setter
	@Getter
	public Integer value;
	
	Type(Integer value){
    	this.value = value;
	}
	
	 public static Type getName(int value) {
    	for(Type type : Type.values()){
    		if(type.value == value){
    			return type;
    		}
    	}
    	return null;
    }
}
