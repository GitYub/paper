package com.yxy.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * PaperParam
 *
 * @author 余昕宇
 * @date 2018-06-23 23:08
 **/
@Getter
@Setter
@ToString
public class PaperParam {

    private Integer id;

    @NotBlank(message = "论文标题为空")
    @Length(min = 1, max = 20, message = "论文标题需要在20个字以内")
    private String title;

    @NotNull(message = "必须提供论文的作者")
    private Integer authorId;

    @NotNull(message = "必须提供论文的模块分类")
    private Integer moduleId;

    @NotNull(message = "必须指定论文的状态")
    @Min(value = 0, message = "论文状态不合法")
    @Max(value = 1, message = "论文状态不合法")
    private Integer status = 0;

}
