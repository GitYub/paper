package com.yxy.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.yxy.beans.LogType;
import com.yxy.common.RequestHolder;
import com.yxy.dao.SysLogMapper;
import com.yxy.dao.SysRoleUserMapper;
import com.yxy.dao.SysUserMapper;
import com.yxy.model.SysLogWithBLOBs;
import com.yxy.model.SysRoleAcl;
import com.yxy.model.SysRoleUser;
import com.yxy.model.SysUser;
import com.yxy.util.IpUtil;
import com.yxy.util.JsonMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * SysRoleUserService
 *
 * @author 余昕宇
 * @date 2018-06-21 21:15
 **/
@Service
public class SysRoleUserService {

    @Resource
    private SysRoleUserMapper sysRoleUserMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysLogMapper sysLogMapper;

    public List<SysUser> getListByRoleId(int roleId) {

        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)) {

            return Lists.newArrayList();

        }

        return sysUserMapper.getByIdList(userIdList);

    }

    public void changeRoleUsers(int roleId, List<Integer> userIdList) {

        List<Integer> originUserIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        if (originUserIdList.size() == userIdList.size()) {

            Set<Integer> originUserIdSet = Sets.newHashSet(originUserIdList);
            Set<Integer> aclIdSet = Sets.newHashSet(userIdList);
            originUserIdSet.removeAll(aclIdSet);
            if (CollectionUtils.isEmpty(originUserIdSet)) {

                return;

            }
        }

        updateRoleUsers(roleId, userIdList);
        saveRoleUserLog(roleId, originUserIdList, userIdList);

    }

    @Transactional(rollbackFor = Exception.class)
    public void updateRoleUsers(Integer roleId, List<Integer> userIdList) {

        sysRoleUserMapper.deleteByRoleId(roleId);

        if (CollectionUtils.isEmpty(userIdList)) {

            return;

        }

        List<SysRoleUser> roleUserList = Lists.newArrayList();
        for (Integer userId : userIdList) {

            SysRoleUser sysRoleUser = SysRoleUser.builder()
                    .roleId(roleId)
                    .userId(userId)
                    .operator(RequestHolder.getCurrentUser().getUsername())
                    .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()))
                    .operateTime(new Date())
                    .build();

            roleUserList.add(sysRoleUser);

        }

        sysRoleUserMapper.batchInsert(roleUserList);

    }

    @Transactional(rollbackFor = Exception.class)
    public void insertRoleUsers(Integer roleId, List<Integer> userIdList) {

        if (CollectionUtils.isEmpty(userIdList)) {

            return;

        }

        List<SysRoleUser> roleUserList = Lists.newArrayList();
        for (Integer userId : userIdList) {

            SysRoleUser sysRoleUser = SysRoleUser.builder()
                    .roleId(roleId)
                    .userId(userId)
                    .operator("user register")
                    .operateIp("user ip")
                    .operateTime(new Date())
                    .build();

            roleUserList.add(sysRoleUser);

        }

        sysRoleUserMapper.batchInsert(roleUserList);

    }

    private void saveRoleUserLog(int roleId, List<Integer> before, List<Integer> after) {

        SysLogWithBLOBs sysLogWithBLOBs = new SysLogWithBLOBs();
        sysLogWithBLOBs.setType(LogType.TYPE_ROLE_USER);
        sysLogWithBLOBs.setTargetId(roleId);
        sysLogWithBLOBs.setOldValue(before == null? "" : JsonMapper.obj2String(before));
        sysLogWithBLOBs.setNewValue(after == null? "" : JsonMapper.obj2String(after));
        sysLogWithBLOBs.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLogWithBLOBs.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLogWithBLOBs.setOperateTime(new Date());
        sysLogWithBLOBs.setStatus(1);

        sysLogMapper.insertSelective(sysLogWithBLOBs);

    }

}
