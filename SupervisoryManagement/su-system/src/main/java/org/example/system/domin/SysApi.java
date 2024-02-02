package org.example.system.domin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SysApi implements Serializable {

    private static final Long serialVersionUID = 1L;

    private Long apiId;

    //角色名称
    @NotBlank(message = "模型名称不能为空")
    private String modelName;

    //角色描述
    @NotBlank(message = "API KEY不能为空")
    private String apiKey;

}
