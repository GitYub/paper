package com.yxy.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.sun.org.apache.xerces.internal.impl.xpath.XPath;
import com.yxy.beans.PageQuery;
import com.yxy.beans.PageResult;
import com.yxy.common.RequestHolder;
import com.yxy.dao.SysRoleUserMapper;
import com.yxy.dao.SysUserMapper;
import com.yxy.exception.ParamException;
import com.yxy.model.SysRoleUser;
import com.yxy.model.SysUser;
import com.yxy.param.UserParam;
import com.yxy.util.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * SysUserService
 *
 * @author 余昕宇
 * @date 2018-06-20 18:58
 **/
@Service
public class SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysLogService sysLogService;

    @Resource
    private SysRoleUserService sysRoleUserService;

    public void save(UserParam param, String password) {

        String encryptedPassword = MD5Util.encrypt(password);

        SysUser user = SysUser.builder().username(param.getUsername())
                .telephone(param.getTelephone())
                .email(param.getEmail())
                .password(encryptedPassword)
                .moduleId(param.getModuleId())
                .status(param.getStatus())
                .remark(param.getRemark())
                .build();

        user.setOperator(RequestHolder.getCurrentUser() == null ? "" : RequestHolder.getCurrentUser().getUsername());
        user.setOperateTime(new Date());
        user.setOperateIp(RequestHolder.getCurrentRequest() == null ? "" : IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        // TODO: 2018-06-20 sendEmail

        sysUserMapper.insertSelective(user);
        sysLogService.saveUserLog(null, user);

        List<Integer> list = Lists.newArrayList();
        list.add(user.getId());

        sysRoleUserService.insertRoleUsers(8, list);

    }

    public void save(UserParam param) {

        BeanValidator.check(param);

        if (checkTelephoneExist(param.getTelephone(), param.getId())) {

            throw new ParamException("电话已被占用");

        }

        if (checkEmailExist(param.getEmail(), param.getId())) {

            throw new ParamException("邮箱已被占用");

        }

        // String password = PasswordUtil.randomPassword();
        String password = "123456";

        String encryptedPassword = MD5Util.encrypt(password);

        SysUser user = SysUser.builder().username(param.getUsername())
                .telephone(param.getTelephone())
                .email(param.getEmail())
                .password(encryptedPassword)
                .moduleId(param.getModuleId())
                .status(param.getStatus())
                .remark(param.getRemark())
                .build();

        user.setOperator(RequestHolder.getCurrentUser().getUsername());
        user.setOperateTime(new Date());
        user.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));

        // TODO: 2018-06-20 sendEmail

        sysUserMapper.insertSelective(user);
        sysLogService.saveUserLog(null, user);

        List<Integer> list = Lists.newArrayList();
        list.add(user.getId());

        sysRoleUserService.insertRoleUsers(8, list);

    }

    public void update(UserParam param) {

        BeanValidator.check(param);

        if (checkTelephoneExist(param.getTelephone(), param.getId())) {

            throw new ParamException("电话已被占用");

        }

        if (checkEmailExist(param.getEmail(), param.getId())) {

            throw new ParamException("邮箱已被占用");

        }

        SysUser before = sysUserMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的用户不存在");
        SysUser after = SysUser.builder().id(param.getId())
                .username(param.getUsername())
                .telephone(param.getTelephone())
                .email(param.getEmail())
                .password(before.getPassword())
                .moduleId(param.getModuleId())
                .status(param.getStatus())
                .remark(param.getRemark())
                .build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());

        sysUserMapper.updateByPrimaryKeySelective(after);
        sysLogService.saveUserLog(before, after);

    }

    private boolean checkEmailExist(String email, Integer userId) {

        return sysUserMapper.countByEmail(email, userId) > 0;

    }

    private boolean checkTelephoneExist(String telephone, Integer userId) {

        return sysUserMapper.countByTelephone(telephone, userId) > 0;

    }

    public SysUser findByKeyword(String keyword) {

        return sysUserMapper.findByKeyword(keyword);

    }

    public PageResult<SysUser> getPageByModuleId(int moduleId, PageQuery pageQuery) {

        BeanValidator.check(pageQuery);

        int count = sysUserMapper.countByModuleId(moduleId);
        if (count > 0) {

            List<SysUser> list = sysUserMapper.getPageByModuleId(moduleId, pageQuery);
            return PageResult.<SysUser>builder().total(count).data(list).build();

        }

        return PageResult.<SysUser>builder().build();

    }

    public List<SysUser> getAll() {

        return sysUserMapper.getAll();

    }

}
