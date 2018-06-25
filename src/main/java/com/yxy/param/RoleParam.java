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
 * RoleParam
 *
 * @author 余昕宇
 * @date 2018-06-21 15:28
 **/
@Getter
@Setter
@ToString
public class RoleParam {

    private Integer id;

    @NotBlank(message = "角色名称不能为空")
    @Length(min = 2, max = 30, message = "角色名称需在2到30个字之间")
    private String name;

    @Max(value = 2, message = "角色类型不合法")
    @Min(value = 1, message = "角色类型不合法")
    private Integer type = 1;

    @NotNull(message = "必须指定角色的状态")
    @Max(value = 1, message = "角色状态不合法")
    @Min(value = 0, message = "角色状态不合法")
    private Integer status;

    @Length(max = 250, message = "角色的备注长度需在250个字以内")
    private String remark;

}
