package com.yxy.service;

import com.google.common.base.Preconditions;
import com.yxy.beans.PageQuery;
import com.yxy.beans.PageResult;
import com.yxy.common.RequestHolder;
import com.yxy.dao.SysAclMapper;
import com.yxy.exception.ParamException;
import com.yxy.model.SysAcl;
import com.yxy.param.AclParam;
import com.yxy.util.BeanValidator;
import com.yxy.util.IpUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * SysAclService
 *
 * @author 余昕宇
 * @date 2018-06-21 3:03
 **/
@Service
public class SysAclService {

    @Resource
    private SysAclMapper sysAclMapper;

    @Resource
    private SysLogService sysLogService;

    public void save(AclParam param) {

        BeanValidator.check(param);
        if (checkExist(param.getAclModuleId(), param.getName(), param.getId())) {

            throw new ParamException("当前权限模块下已存在相同名称的权限点");

        }

        SysAcl sysAcl = SysAcl.builder()
                .name(param.getName())
                .aclModuleId(param.getAclModuleId())
                .url(param.getUrl())
                .type(param.getType())
                .status(param.getStatus())
                .seq(param.getSeq())
                .remark(param.getRemark())
                .build();
        sysAcl.setCode(generateCode());
        sysAcl.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysAcl.setOperateTime(new Date());
        sysAcl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));

        sysAclMapper.insertSelective(sysAcl);
        sysLogService.saveAclLog(null, sysAcl);

    }

    public void update(AclParam param) {

        BeanValidator.check(param);
        if (checkExist(param.getAclModuleId(), param.getName(), param.getId())) {

            throw new ParamException("当前权限模块下已存在相同名称的权限点");

        }

        SysAcl before = sysAclMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的权限点不存在");

        SysAcl after = SysAcl.builder()
                .id(param.getId())
                .name(param.getName())
                .aclModuleId(param.getAclModuleId())
                .url(param.getUrl())
                .type(param.getType())
                .status(param.getStatus())
                .seq(param.getSeq())
                .remark(param.getRemark())
                .build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateTime(new Date());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));

        sysAclMapper.updateByPrimaryKeySelective(after);
        sysLogService.saveAclLog(before, after);

    }

    private boolean checkExist(Integer aclModuleId, String name, Integer id) {

        return sysAclMapper.countByNameAndAclModuleId(aclModuleId, name, id) > 0;

    }

    private String generateCode() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date()) + "_" + new Random().nextInt(100);

    }

    public PageResult<SysAcl> getPageByAclModuleId(int aclModuleId, PageQuery pageQuery) {

        BeanValidator.check(pageQuery);
        int count = sysAclMapper.countByAclModuleId(aclModuleId);

        if (count > 0) {

            List<SysAcl> aclList = sysAclMapper.getPageByAclModuleId(aclModuleId, pageQuery);
            return PageResult.<SysAcl>builder().data(aclList).total(count).build();

        }

        return PageResult.<SysAcl>builder().build();

    }

}
