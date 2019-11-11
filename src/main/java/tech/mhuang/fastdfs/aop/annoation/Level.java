package tech.mhuang.fastdfs.aop.annoation;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @ClassName:  Level   
 * @Description:等级
 * @author: mhuang
 * @date:   2017年12月5日 上午11:16:19
 */
public enum Level {

    /**
     * ALL
     */
	ALL(0),
	QUERY(1),
	SAVE(2),
	UPDATE(3),
	DELETE(4);
	
    /**
     *  value
     */
	@Setter
	@Getter
	public Integer value; 
	
    Level(Integer value){
    	this.value = value;
	}
    
    public static Level getName(int value) {
    	for(Level level : Level.values()){
    		if(level.value == value){
    			return level;
    		}
    	}
    	return null;
    }
    
}
