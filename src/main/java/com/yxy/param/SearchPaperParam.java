package com.yxy.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * SerachLogParam
 *
 * @author 余昕宇
 * @date 2018-06-23 11:37
 **/
@Getter
@Setter
@ToString
public class SearchPaperParam {

    private String title;

    private String author;

    private Integer moduleId;

    private String operator;

    // yyyy-MM-dd HH:mm:ss
    private String fromTime;

    private String toTime;

}
