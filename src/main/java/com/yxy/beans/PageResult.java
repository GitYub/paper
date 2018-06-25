package com.yxy.beans;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * PageResult
 *
 * @author 余昕宇
 * @date 2018-06-20 20:28
 **/
@Getter
@Setter
@ToString
@Builder
public class PageResult<T> {

    private List<T> data;

    private int total;

}
