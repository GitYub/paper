package com.yxy.common;

import com.yxy.model.SysUser;

import javax.servlet.http.HttpServletRequest;

/**
 * RequestHolder
 *
 * @author 余昕宇
 * @date 2018-06-21 0:38
 **/
public class RequestHolder {

    private static final ThreadLocal<SysUser> USER_HOLDER = new ThreadLocal<>();

    private static final ThreadLocal<HttpServletRequest> REQUEST_HOLDER = new ThreadLocal<>();

    public static void add(SysUser user) {

        USER_HOLDER.set(user);

    }

    public static void add(HttpServletRequest request) {

        REQUEST_HOLDER.set(request);

    }

    public static SysUser getCurrentUser() {

        return USER_HOLDER.get();

    }

    public static HttpServletRequest getCurrentRequest() {

        return REQUEST_HOLDER.get();

    }

    public static void remove() {

        USER_HOLDER.remove();
        REQUEST_HOLDER.remove();

    }

}
