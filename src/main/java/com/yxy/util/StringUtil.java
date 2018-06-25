package com.yxy.util;

import com.google.common.base.Splitter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * StringUtil
 *
 * @author 余昕宇
 * @date 2018-06-21 21:11
 **/
public class StringUtil {

    public static List<Integer> splitToListInt(String str) {

        List<String> stringList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(str);
        return stringList.stream().map(Integer::parseInt).collect(Collectors.toList());

    }

}
