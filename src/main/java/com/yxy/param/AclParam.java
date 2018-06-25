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
 * AclParam
 *
 * @author 余昕宇
 * @date 2018-06-21 2:56
 **/
@Getter
@Setter
@ToString
public class AclParam {

    private Integer id;

    @NotBlank(message = "权限点名称不能为空")
    @Length(min = 2, max = 30, message = "权限点名称需在2到30个字之间")
    private String name;

    @NotNull(message = "必须指定权限模块")
    private Integer aclModuleId;

    @Length(min = 6, max = 250, message = "权限点URL长度需在6到250个字符之间")
    private String url;

    @NotNull(message = "必须指定权限点的类型")
    @Max(value = 3, message = "权限点类型不合法")
    @Min(value = 1, message = "权限点类型不合法")
    private Integer type;

    @NotNull(message = "必须指定权限点的状态")
    @Max(value = 1, message = "权限点状态不合法")
    @Min(value = 0, message = "权限点状态不合法")
    private Integer status;

    @NotNull(message = "必须指定权限点的展示顺序")
    private Integer seq;

    @Length(max = 250, message = "权限点的备注长度需在250个字以内")
    private String remark;

}
