package com.yxy.common;

import com.yxy.exception.ParamException;
import com.yxy.exception.PermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * SpringExceptionResolver
 *
 * @author 余昕宇
 * @date 2018-06-18 2:03
 **/
@Slf4j
public class SpringExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {

        String url = httpServletRequest.getRequestURL().toString();
        ModelAndView modelAndView;
        String defaultMsg = "System Error";

        // 项目中所有请求json数据的请求都用.json结尾，请求页面都有.page结尾
        if (url.endsWith(".json")) {

            if (e instanceof PermissionException || e instanceof ParamException) {

                JsonData result = JsonData.fail(e.getMessage());
                modelAndView = new ModelAndView("jsonView", result.toMap());

            } else {

                log.error("unknow json exception, url:" + url, e);
                JsonData result = JsonData.fail(defaultMsg);
                modelAndView = new ModelAndView("jsonView", result.toMap());

            }

        } else if (url.endsWith(".page")) {

            log.error("unknow page exception, url:" + url, e);
            JsonData result = JsonData.fail(defaultMsg);
            modelAndView = new ModelAndView("exception", result.toMap());

        } else {

            log.error("unknow exception, url:" + url, e);
            JsonData result = JsonData.fail(defaultMsg);
            modelAndView = new ModelAndView("jsonView", result.toMap());

        }

        return modelAndView;

    }

}
