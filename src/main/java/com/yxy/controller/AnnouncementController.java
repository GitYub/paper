package com.yxy.controller;

import com.yxy.beans.PageQuery;
import com.yxy.common.JsonData;
import com.yxy.common.RequestHolder;
import com.yxy.param.AnnouncementParam;
import com.yxy.service.AnnouncementService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * AnnouncementController
 *
 * @author 余昕宇
 * @date 2018-06-23 23:47
 **/
@RestController
@RequestMapping("/sys/announcement")
public class AnnouncementController {

    @Resource
    private AnnouncementService announcementService;

    @RequestMapping("/announcement.page")
    public ModelAndView announcement() {

        return new ModelAndView("announcement");

    }

    @RequestMapping("/submit.page")
    public ModelAndView submit() {

        return new ModelAndView("submitA");

    }

    @RequestMapping("/save.json")
    public JsonData save(AnnouncementParam param) {

        announcementService.save(param);
        return JsonData.success();

    }

    @RequestMapping("/disable.json")
    public JsonData disable(@RequestParam("announcementId") int announcementId) {

        announcementService.disable(announcementId);
        return JsonData.success();

    }

    @RequestMapping("/page.json")
    public JsonData page(PageQuery pageQuery) {

        return JsonData.success(announcementService.getPage(pageQuery));

    }

}
