package com.yxy.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * ModuleParam
 *
 * @author 余昕宇
 * @date 2018-06-18 12:25
 **/
@Getter
@Setter
@ToString
public class ModuleParam {

    private Integer id;

    @NotBlank(message = "模块名称不能为空")
    @Length(max = 20, min = 2, message = "模块名称需要在2-20个字之间")
    private String name;

    private Integer parentId = 0;

    @NotNull(message = "展示顺序不能为空")
    private Integer seq;

    @Length(max = 200, message = "备注的长度需要在200个字以内")
    private String remark;

}
