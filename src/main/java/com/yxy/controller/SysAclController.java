package com.yxy.controller;

import com.google.common.collect.Maps;
import com.yxy.beans.PageQuery;
import com.yxy.common.JsonData;
import com.yxy.model.SysRole;
import com.yxy.param.AclParam;
import com.yxy.service.SysAclService;
import com.yxy.service.SysRoleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * SysAclController
 *
 * @author 余昕宇
 * @date 2018-06-21 1:36
 **/

@RestController
@RequestMapping("/sys/acl")
public class SysAclController {

    @Resource
    private SysAclService sysAclService;

    @Resource
    private SysRoleService sysRoleService;

    @RequestMapping("/save.json")
    public JsonData save(AclParam param) {

        sysAclService.save(param);
        return JsonData.success();

    }

    @RequestMapping("/update.json")
    public JsonData update(AclParam param) {

        sysAclService.update(param);
        return JsonData.success();

    }

    @RequestMapping("/page.json")
    public JsonData list(@RequestParam("aclModuleId") Integer aclModuleId, PageQuery pageQuery) {

        return JsonData.success(sysAclService.getPageByAclModuleId(aclModuleId, pageQuery));

    }

    @RequestMapping("/acls.json")
    public JsonData acls(@RequestParam("aclId") int aclId) {

        Map<String, Object> map = Maps.newHashMap();
        List<SysRole> roleList = sysRoleService.getRoleListByAclId(aclId);
        map.put("roles", roleList);
        map.put("users", sysRoleService.getUserListByRoleList(roleList));

        return JsonData.success(map);

    }

}
