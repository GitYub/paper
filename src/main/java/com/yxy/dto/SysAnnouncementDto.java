package com.yxy.dto;

import lombok.*;

import java.util.Date;

/**
 * SysAnnouncementDto
 *
 * @author 余昕宇
 * @date 2018-06-25 22:50
 **/
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysAnnouncementDto {

    private Integer id;

    private String title;

    private String author;

    private Date uploadTime;

    private String content;

}
