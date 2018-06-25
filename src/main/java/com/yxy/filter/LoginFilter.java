package com.yxy.filter;

import com.yxy.common.RequestHolder;
import com.yxy.model.SysUser;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * LoginFilter
 *
 * @author 余昕宇
 * @date 2018-06-21 0:50
 **/
@Slf4j
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {



    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        SysUser sysUser = (SysUser) request.getSession().getAttribute("user");

        if (sysUser == null) {

            String path = "/signin.jsp";
            response.sendRedirect(path);

        }

        RequestHolder.add(sysUser);
        RequestHolder.add(request);

        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {



    }

}
