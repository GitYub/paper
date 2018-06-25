package com.yxy.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.yxy.common.RequestHolder;
import com.yxy.dao.SysRoleAclMapper;
import com.yxy.dao.SysRoleMapper;
import com.yxy.dao.SysRoleUserMapper;
import com.yxy.dao.SysUserMapper;
import com.yxy.exception.ParamException;
import com.yxy.model.SysRole;
import com.yxy.model.SysUser;
import com.yxy.param.RoleParam;
import com.yxy.util.BeanValidator;
import com.yxy.util.IpUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SysRoleService
 *
 * @author 余昕宇
 * @date 2018-06-21 15:26
 **/
@Service
public class SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysRoleUserMapper sysRoleUserMapper;

    @Resource
    private SysRoleAclMapper sysRoleAclMapper;

    @Resource
    private SysLogService sysLogService;

    @Resource
    private SysUserMapper sysUserMapper;

    public void save(RoleParam param) {

        BeanValidator.check(param);
        if (checkExit(param.getName(), param.getId())) {

            throw new ParamException("角色名称已存在");

        }

        SysRole role = SysRole.builder()
                .name(param.getName())
                .status(param.getStatus())
                .type(param.getType())
                .remark(param.getRemark())
                .build();
        role.setOperator(RequestHolder.getCurrentUser().getUsername());
        role.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        role.setOperateTime(new Date());

        sysRoleMapper.insertSelective(role);
        sysLogService.saveRoleLog(null, role);

    }

    public void update(RoleParam param) {

        BeanValidator.check(param);
        if (checkExit(param.getName(), param.getId())) {

            throw new ParamException("角色名称已存在");

        }

        SysRole before = sysRoleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的角色不存在");

        SysRole after = SysRole.builder()
                .id(param.getId())
                .name(param.getName())
                .status(param.getStatus())
                .type(param.getType())
                .remark(param.getRemark())
                .build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());

        sysRoleMapper.updateByPrimaryKeySelective(after);
        sysLogService.saveRoleLog(before, after);

    }

    public List<SysRole> getAll() {

        return sysRoleMapper.getAll();

    }

    private boolean checkExit(String name, Integer id) {

        return sysRoleMapper.countByName(name, id) > 0;

    }

    public List<SysRole> getRoleListByUserId(int userId) {

        List<Integer> roleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(roleIdList)) {

            return Lists.newArrayList();

        }

        return sysRoleMapper.getByIdList(roleIdList);

    }

    public List<SysRole> getRoleListByAclId(int aclId) {

        List<Integer> roleIdList = sysRoleAclMapper.getRoleIdListByAclId(aclId);
        if (CollectionUtils.isEmpty(roleIdList)) {

            return Lists.newArrayList();

        }

        return sysRoleMapper.getByIdList(roleIdList);

    }

    public List<SysUser> getUserListByRoleList(List<SysRole> roleList) {
        if (CollectionUtils.isEmpty(roleList)) {
            return Lists.newArrayList();
        }
        List<Integer> roleIdList = roleList.stream().map(SysRole::getId).collect(Collectors.toList());
        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleIdList(roleIdList);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }
        return sysUserMapper.getByIdList(userIdList);
    }

}
