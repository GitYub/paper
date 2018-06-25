package com.yxy.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * SearchLogDto
 *
 * @author 余昕宇
 * @date 2018-06-23 11:40
 **/
@Getter
@Setter
@ToString
public class SearchLogDto {

    // LogType
    private Integer type;

    private String beforeSeq;

    private String afterSeq;

    private String operator;

    // yyyy-MM-dd HH:mm:ss
    private Date fromTime;

    private Date toTime;

}
