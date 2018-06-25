package com.yxy.controller;

import com.google.common.collect.Maps;
import com.yxy.beans.PageQuery;
import com.yxy.beans.PageResult;
import com.yxy.common.JsonData;
import com.yxy.model.SysUser;
import com.yxy.param.UserParam;
import com.yxy.service.SysRoleService;
import com.yxy.service.SysTreeService;
import com.yxy.service.SysUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Map;

/**
 * SysUserController
 *
 * @author 余昕宇
 * @date 2018-06-20 17:01
 **/
@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysTreeService sysTreeService;

    @Resource
    private SysRoleService sysRoleService;

    @RequestMapping("/noAuth.page")
    public ModelAndView noAuth() {

        return new ModelAndView("noAuth");

    }

    @RequestMapping("/save.json")
    public JsonData saveUser(UserParam param) {

        sysUserService.save(param);
        return JsonData.success();

    }

    @RequestMapping("/update.json")
    public JsonData updateUser(UserParam param) {

        sysUserService.update(param);
        return JsonData.success();

    }

    @RequestMapping("/page.json")
    public JsonData page(@RequestParam("moduleId") Integer moduleId, PageQuery pageQuery) {

        PageResult<SysUser> pageResult = sysUserService.getPageByModuleId(moduleId, pageQuery);
        return JsonData.success(pageResult);

    }

    @RequestMapping("/acls.json")
    public JsonData acls(@RequestParam("userId") int userId) {

        Map<String, Object> map = Maps.newHashMap();
        map.put("acls", sysTreeService.userAclTree(userId));
        map.put("roles", sysRoleService.getRoleListByUserId(userId));
        return JsonData.success(map);

    }

    @RequestMapping("/index.page")
    public ModelAndView index() {

        return new ModelAndView("index");

    }

}
