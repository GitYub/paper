package com.yxy.common;

import com.yxy.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * HttpInterceptor
 *
 * @author 余昕宇
 * @date 2018-06-18 12:04
 **/
@Slf4j
public class HttpInterceptor extends HandlerInterceptorAdapter {

    private final static String START_TIME="requestStartTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String url = request.getRequestURL().toString();
        Map param = request.getParameterMap();
        log.info("request start. url:{}, params:{}", url, JsonMapper.obj2String(param));
        long start = System.currentTimeMillis();
        request.setAttribute(START_TIME, start);
        return true;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

//        String url = request.getRequestURL().toString();
//        long start = (Long) request.getAttribute(START_TIME);
//        long end = System.currentTimeMillis();
//        log.info("request finished. url:{}, cost:{}", url, end - start);

        removeThreadLocalInfo();

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        String url = request.getRequestURL().toString();
        long start = (Long) request.getAttribute(START_TIME);
        long end = System.currentTimeMillis();
        log.info("request complete. url:{}, cost:{}", url, end - start);
        removeThreadLocalInfo();

    }

    private void removeThreadLocalInfo() {

        RequestHolder.remove();

    }

}
