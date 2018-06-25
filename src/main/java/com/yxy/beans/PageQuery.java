package com.yxy.beans;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

/**
 * PageQuery
 *
 * @author 余昕宇
 * @date 2018-06-20 20:28
 **/
public class PageQuery {

    @Getter
    @Setter
    @Min(value = 1, message = "当前页码不合法")
    private int pageNo = 1;

    @Getter
    @Setter
    @Min(value = 1, message = "每页展示的数量不合法")
    private int pageSize = 10;

    @Setter
    private int offset;

    public int getOffset() {

        return (pageNo - 1) * pageSize;

    }

}
