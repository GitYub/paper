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
 * AnnouncementParam
 *
 * @author 余昕宇
 * @date 2018-06-23 23:20
 **/
@Getter
@Setter
@ToString
public class AnnouncementParam {

    private Integer id;

    @NotBlank(message = "公告标题为空")
    @Length(min = 1, max = 20, message = "公告标题需要在20个字以内")
    private String title;

    @NotBlank(message = "公告内容不能为空")
    private String content;

    private Integer authorId;

}
