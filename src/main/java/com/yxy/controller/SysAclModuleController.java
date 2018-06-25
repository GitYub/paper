package com.yxy.controller;

import com.yxy.common.JsonData;
import com.yxy.dto.AclModuleLevelDto;
import com.yxy.param.AclModuleParam;
import com.yxy.service.SysAclModuleService;
import com.yxy.service.SysTreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * SysAclModuleController
 *
 * @author 余昕宇
 * @date 2018-06-21 1:36
 **/
@RestController
@RequestMapping("/sys/aclModule")
@Slf4j
public class SysAclModuleController {

    @Resource
    private SysAclModuleService sysAclModuleService;

    @Resource
    private SysTreeService sysTreeService;

    @RequestMapping("/acl.page")
    public ModelAndView page() {

        return new ModelAndView("acl");

    }

    @RequestMapping("/save.json")
    public JsonData saveAclModule(AclModuleParam param) {

        sysAclModuleService.save(param);
        return JsonData.success();

    }

    @RequestMapping("/update.json")
    public JsonData updateAclModule(AclModuleParam param) {

        sysAclModuleService.update(param);
        return JsonData.success();

    }

    @RequestMapping("/tree.json")
    public JsonData tree() {

        List<AclModuleLevelDto> dtoList = sysTreeService.aclModuleTree();
        return JsonData.success(dtoList);

    }

    @RequestMapping("/delete.json")
    public JsonData delete(@RequestParam("id") int id) {

        sysAclModuleService.delete(id);
        return JsonData.success();

    }

}
