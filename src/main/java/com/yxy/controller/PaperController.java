package com.yxy.controller;

import com.yxy.beans.PageQuery;
import com.yxy.common.JsonData;
import com.yxy.param.PaperParam;
import com.yxy.param.SearchPaperParam;
import com.yxy.param.UploadFileParam;
import com.yxy.service.PaperService;
import com.yxy.util.OssUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * PaperController
 *
 * @author 余昕宇
 * @date 2018-06-23 16:46
 **/
@RestController
@RequestMapping("/sys/paper")
@Slf4j
public class PaperController {

    @Resource
    private PaperService paperService;

    @RequestMapping("/paper.page")
    public ModelAndView paper() {

        return new ModelAndView("paper");

    }

    @RequestMapping("/manage.page")
    public ModelAndView manage(){

        return new ModelAndView("manage");

    }

    @RequestMapping("/submit.page")
    public ModelAndView submit(){

        return new ModelAndView("submitP");

    }

    @RequestMapping(value = "/save.json")
    public JsonData save(UploadFileParam uploadFileParam) {

        PaperParam param = new PaperParam();
        param.setTitle(uploadFileParam.getTitle());
        param.setModuleId(uploadFileParam.getType());
        paperService.save(uploadFileParam.getFile(), param);
        return JsonData.success();

    }

    @RequestMapping("/download.json")
    public JsonData download(HttpServletResponse response, @RequestParam("id") Integer id) {

        OssUtil.download(response, paperService.download(id));
        return JsonData.success();

    }

    @RequestMapping("/disable.json")
    public JsonData disable(@RequestParam("paperId") Integer paperId) {

        paperService.disable(paperId);
        return JsonData.success();

    }

    @RequestMapping("/page.json")
    public JsonData searchPage(SearchPaperParam param, PageQuery pageQuery) {

        return JsonData.success(paperService.searchPageList(param, pageQuery));

    }

}
