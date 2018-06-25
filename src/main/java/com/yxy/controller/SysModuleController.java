package com.yxy.controller;

import com.yxy.common.JsonData;
import com.yxy.dto.ModuleLevelDto;
import com.yxy.param.ModuleParam;
import com.yxy.service.SysModuleService;
import com.yxy.service.SysTreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * SysModuleController
 *
 * @author 余昕宇
 * @date 2018-06-18 12:29
 **/
@RestController
@RequestMapping("/sys/module")
@Slf4j
public class SysModuleController {

    @Resource
    private SysModuleService sysModuleService;

    @Resource
    private SysTreeService sysTreeService;

    @RequestMapping("/module.page")
    public ModelAndView page() {

        return new ModelAndView("module");

    }

    @RequestMapping("/save.json")
    public JsonData saveModule(ModuleParam param) {

        sysModuleService.save(param);
        return JsonData.success();

    }

    @RequestMapping("/tree.json")
    public JsonData tree() {

        List<ModuleLevelDto> dtoList = sysTreeService.moduleTree();
        return JsonData.success(dtoList);

    }

    @RequestMapping("/update.json")
    public JsonData updateModule(ModuleParam param) {

        sysModuleService.update(param);
        return JsonData.success();

    }

    @RequestMapping("/delete.json")
    public JsonData delete(@RequestParam("id") int id) {

        sysModuleService.delete(id);
        return JsonData.success();

    }

}
