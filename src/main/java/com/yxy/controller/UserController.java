package com.yxy.controller;

import com.yxy.model.SysUser;
import com.yxy.param.UserParam;
import com.yxy.service.SysUserService;
import com.yxy.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * UserController
 *
 * @author 余昕宇
 * @date 2018-06-20 19:42
 **/
@RestController
public class UserController {

    @Resource
    private SysUserService sysUserService;

    @RequestMapping("/logout.page")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {

        request.getSession().invalidate();
        String path = "signin.jsp";
        response.sendRedirect(path);

    }

    @RequestMapping("/login.page")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        SysUser sysUser = sysUserService.findByKeyword(username);
        String errorMsg = "";
        String ret = request.getParameter("ret");

        if (StringUtils.isBlank(username)) {

            errorMsg = "用户名不能为空";

        } else if (StringUtils.isBlank(password)) {

            errorMsg = "密码不能为空";

        } else if (sysUser == null) {

            errorMsg = "用户不存在";

        } else if (!sysUser.getPassword().equals(MD5Util.encrypt(password))) {

            errorMsg = "用户名或密码错误";

        } else if (sysUser.getStatus() != 1) {

            errorMsg = "用户已被冻结，请联系管理员";

        } else {

            request.getSession().setAttribute("user", sysUser);
            if (StringUtils.isNotBlank(ret)) {

                response.sendRedirect(ret);

            } else {

                response.sendRedirect("/admin/index.page");

            }

            return;

        }

        request.setAttribute("error", errorMsg);
        request.setAttribute("username", username);
        if (StringUtils.isNotBlank(ret)) {

            request.setAttribute("ret", ret);

        }
        String path = "signin.jsp";
        request.getRequestDispatcher(path).forward(request, response);

    }

    @RequestMapping("/register.page")
    public void register(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String telephone = request.getParameter("telephone");

        String errorMsg = "";

        if (StringUtils.isBlank(username)) {

            errorMsg = "用户名不能为空";

        } else if (StringUtils.isBlank(password)) {

            errorMsg = "密码不能为空";

        } else if (StringUtils.isBlank(email)) {

            errorMsg = "邮箱不能为空";

        } else if (StringUtils.isBlank(telephone)) {

            errorMsg = "电话不能为空";

        } else if (sysUserService.findByKeyword(telephone) != null
                || sysUserService.findByKeyword(email) != null) {

            errorMsg = "用户名/电话/邮箱已存在";

        } else if (StringUtils.trimToEmpty(username).equals("Admin")) {

            errorMsg = "用户名非法";

        } else {

            response.sendRedirect("signin.jsp");
            UserParam param = new UserParam();
            param.setEmail(email);
            param.setModuleId(0);
            param.setRemark("");
            param.setUsername(username);
            param.setTelephone(telephone);
            param.setStatus(1);

            sysUserService.save(param, password);
            return;

        }

        request.setAttribute("error", errorMsg);

        String path = "register.jsp";
        request.getRequestDispatcher(path).forward(request, response);

    }

}
