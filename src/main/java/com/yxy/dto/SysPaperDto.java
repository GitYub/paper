package com.yxy.dto;

import lombok.*;

import java.util.Date;

/**
 * SysPaperDto
 *
 * @author 余昕宇
 * @date 2018-06-25 23:23
 **/
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysPaperDto {

    private Integer id;

    private String title;

    private String author;

    private Date uploadTime;

    private Integer moduleId;

    private String url;

}
