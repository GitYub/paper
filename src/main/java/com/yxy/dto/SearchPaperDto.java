package com.yxy.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * SearchLogDto
 *
 * @author 余昕宇
 * @date 2018-06-23 11:40
 **/
@Getter
@Setter
@ToString
public class SearchPaperDto {

    private String title;

    private Integer count;

    private Integer moduleId;

    private String operator;

    // yyyy-MM-dd HH:mm:ss
    private Date fromTime;

    private Date toTime;

}
