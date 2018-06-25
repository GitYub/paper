package com.yxy.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * ApplicationContextHelper
 *
 * @author 余昕宇
 * @date 2018-06-18 11:49
 **/
@Component("applicationContextHelper")
public class ApplicationContextHelper implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {

        applicationContext = context;

    }

    public static <T> T popBean(Class<T> tClass) {

        if (applicationContext == null) {

            return null;

        }

        return applicationContext.getBean(tClass);

    }

    public static <T> T popBean(String name, Class<T> tClass) {

        if (applicationContext == null) {

            return null;

        }

        return applicationContext.getBean(name,  tClass);

    }

}
