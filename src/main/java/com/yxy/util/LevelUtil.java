package com.yxy.util;

import org.apache.commons.lang3.StringUtils;

/**
 * LevelUtil
 *
 * @author 余昕宇
 * @date 2018-06-18 12:35
 **/
public class LevelUtil {

    public final static String SEPARATOR = ".";

    public final static String ROOT = "0";

    public static String calculateLevel(String parentLevel, Integer parentId) {

        if (StringUtils.isBlank(parentLevel)) {

            return ROOT;

        } else {

            return StringUtils.join(parentLevel, SEPARATOR, parentId);

        }

    }

}
