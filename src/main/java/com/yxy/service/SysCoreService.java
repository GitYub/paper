package com.yxy.service;

import com.google.common.collect.Lists;
import com.yxy.beans.CacheKeyConstants;
import com.yxy.common.RequestHolder;
import com.yxy.dao.SysAclMapper;
import com.yxy.dao.SysRoleAclMapper;
import com.yxy.dao.SysRoleUserMapper;
import com.yxy.model.SysAcl;
import com.yxy.model.SysUser;
import com.yxy.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * SysCoreService
 *
 * @author 余昕宇
 * @date 2018-06-21 16:02
 **/
@Service
@Slf4j
public class SysCoreService {

    @Resource
    private SysAclMapper sysAclMapper;

    @Resource
    private SysRoleUserMapper sysRoleUserMapper;

    @Resource
    private SysRoleAclMapper sysRoleAclMapper;

    @Resource
    private SysCacheService sysCacheService;

    public List<SysAcl> getCurrentUserAclList() {

        int userId = RequestHolder.getCurrentUser().getId();
        return getUserAclList(userId);

    }

    public List<SysAcl> getRoleAclList(int roleId) {

        List<Integer> aclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.newArrayList(roleId));
        if (CollectionUtils.isEmpty(aclIdList)) {

            return Lists.newArrayList();

        }

        return sysAclMapper.getByIdList(aclIdList);

    }

    public List<SysAcl> getUserAclList(int userId) {

        if (isSuperAdmin()) {

            return sysAclMapper.getAll();

        }

        List<Integer> userRoleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(userRoleIdList)) {

            return Lists.newArrayList();

        }

        List<Integer> userAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(userRoleIdList);
        if (CollectionUtils.isEmpty(userAclIdList)) {

            return Lists.newArrayList();

        }

        return sysAclMapper.getByIdList(userAclIdList);

    }

    private boolean isSuperAdmin() {

        SysUser user = RequestHolder.getCurrentUser();
        return "Admin".equals(user.getUsername());

    }

    public boolean hasUrlAcl(String url) {

        if (isSuperAdmin()) {

            return true;

        }

        List<SysAcl> aclList = sysAclMapper.getByUrl(url);
        if (CollectionUtils.isEmpty(aclList)) {

            return true;

        }

        List<SysAcl> userAclList = getCurrentUserAclListFromCache();
        Set<Integer> userAclSet = userAclList.stream().map(SysAcl::getId).collect(Collectors.toSet());

        // 规则：只要有一个权限点有权限，那么我们就认为有访问权限
        boolean hasValidAcl = false;
         for (SysAcl acl : aclList) {

            // 判断一个用户是否具有某个权限点的访问权限
            if (acl == null || acl.getStatus() != 1) {

                continue;

            }

            hasValidAcl = true;
            if (userAclSet.contains(acl.getId())) {

                return true;

            }

        }

        return !hasValidAcl;

    }

    private List<SysAcl> getCurrentUserAclListFromCache() {

        int userId = RequestHolder.getCurrentUser().getId();

        log.info("Get from cache now");
        String cacheValue = sysCacheService.getFromCache(CacheKeyConstants.USER_ACLS, String.valueOf(userId));
        if (StringUtils.isBlank(cacheValue)) {

            List<SysAcl> aclList = getCurrentUserAclList();
            if (CollectionUtils.isNotEmpty(aclList)) {

                sysCacheService.saveCache(JsonMapper.obj2String(aclList), 600, CacheKeyConstants.USER_ACLS, String.valueOf(userId));

            }

            return aclList;

        }

        return JsonMapper.String2Obj(cacheValue, new TypeReference<List<SysAcl>>() {});

    }

}
