package com.yxy.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * AdminController
 *
 * @author 余昕宇
 * @date 2018-06-20 19:54
 **/
@RestController
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping("/index.page")
    public ModelAndView index() {

        return new ModelAndView("admin");

    }

}
