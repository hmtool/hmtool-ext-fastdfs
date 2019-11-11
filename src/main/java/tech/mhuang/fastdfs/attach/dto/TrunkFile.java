package tech.mhuang.fastdfs.attach.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 *
 * 分片文件
 *
 * @author mhuang
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TrunkFile implements Serializable {

    private String url;

    private int trunk;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String data;
}
