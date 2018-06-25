package com.yxy.controller;

import com.yxy.beans.PageQuery;
import com.yxy.common.JsonData;
import com.yxy.param.SearchLogParam;
import com.yxy.service.SysLogService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * SysLogController
 *
 * @author 余昕宇
 * @date 2018-06-23 11:00
 **/
@RestController
@RequestMapping("/sys/log")
public class SysLogController {

    @Resource
    private SysLogService sysLogService;

    @RequestMapping("/log.page")
    public ModelAndView page() {

        return new ModelAndView("log");

    }

    @RequestMapping("/recover.json")
    public JsonData recover(@RequestParam("id") int id) {

        sysLogService.recover(id);
        return JsonData.success();

    }

    @RequestMapping("/page.json")
    public JsonData searchPage(SearchLogParam param, PageQuery pageQuery) {

        return JsonData.success(sysLogService.searchPageList(param, pageQuery));

    }



}
