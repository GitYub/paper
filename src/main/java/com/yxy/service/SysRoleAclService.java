package com.yxy.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.yxy.beans.LogType;
import com.yxy.common.RequestHolder;
import com.yxy.dao.SysLogMapper;
import com.yxy.dao.SysRoleAclMapper;
import com.yxy.model.SysLogWithBLOBs;
import com.yxy.model.SysRoleAcl;
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
 * SysRoleAclService
 *
 * @author 余昕宇
 * @date 2018-06-21 21:14
 **/
@Service
public class SysRoleAclService {

    @Resource
    private SysRoleAclMapper sysRoleAclMapper;

    @Resource
    private SysLogMapper sysLogMapper;

    public void changeRoleAcls(Integer roleId, List<Integer> aclIdList) {

        List<Integer> originAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.newArrayList(roleId));
        if (originAclIdList.size() == aclIdList.size()) {

            Set<Integer> originAclIdSet = Sets.newHashSet(originAclIdList);
            Set<Integer> aclIdSet = Sets.newHashSet(aclIdList);
            originAclIdSet.removeAll(aclIdSet);
            if (CollectionUtils.isEmpty(originAclIdSet)) {

                return;

            }
        }

        updateRoleAcls(roleId, aclIdList);
        saveRoleAclLog(roleId, originAclIdList, aclIdList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateRoleAcls(Integer roleId, List<Integer> aclIdList) {

        sysRoleAclMapper.deleteByRoleId(roleId);

        if (CollectionUtils.isEmpty(aclIdList)) {

            return;

        }

        List<SysRoleAcl> roleAclList = Lists.newArrayList();
        for (Integer aclId : aclIdList) {

            SysRoleAcl sysRoleAcl = SysRoleAcl.builder()
                    .roleId(roleId)
                    .aclId(aclId)
                    .operator(RequestHolder.getCurrentUser().getUsername())
                    .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()))
                    .operateTime(new Date())
                    .build();

            roleAclList.add(sysRoleAcl);

        }

        sysRoleAclMapper.batchInsert(roleAclList);

    }

    private void saveRoleAclLog(int roleId, List<Integer> before, List<Integer> after) {

        SysLogWithBLOBs sysLogWithBLOBs = new SysLogWithBLOBs();
        sysLogWithBLOBs.setType(LogType.TYPE_ROLE_ACL);
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
