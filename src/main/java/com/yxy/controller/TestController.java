package com.yxy.controller;

import com.yxy.common.ApplicationContextHelper;
import com.yxy.common.JsonData;
import com.yxy.dao.SysAclModuleMapper;
import com.yxy.exception.ParamException;
import com.yxy.exception.PermissionException;
import com.yxy.model.SysAclModule;
import com.yxy.param.TestVO;
import com.yxy.util.BeanValidator;
import com.yxy.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * TestController
 *
 * @author 余昕宇
 * @date 2018-06-17 17:44
 **/
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @RequestMapping("/hello.json")
    public JsonData hello() {
        log.info("hello");
        throw new RuntimeException("test Exception");
        //return JsonData.success("hello, permission");
    }

    @RequestMapping("/validate.json")
    public JsonData validate(TestVO vo) throws ParamException {
        log.info("validate");
        SysAclModuleMapper moduleMapper = ApplicationContextHelper.popBean(SysAclModuleMapper.class);
        SysAclModule module = moduleMapper.selectByPrimaryKey(1);
        log.info(JsonMapper.obj2String(module));
        BeanValidator.check(vo);

        return JsonData.success("test validate");
    }

}
