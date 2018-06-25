package com.yxy.service;

import com.google.common.base.Preconditions;
import com.yxy.common.RequestHolder;
import com.yxy.dao.SysAclMapper;
import com.yxy.dao.SysAclModuleMapper;
import com.yxy.exception.ParamException;
import com.yxy.model.SysAclModule;
import com.yxy.param.AclModuleParam;
import com.yxy.util.BeanValidator;
import com.yxy.util.IpUtil;
import com.yxy.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * SysAclModuleService
 *
 * @author 余昕宇
 * @date 2018-06-21 1:47
 **/
@Service
public class SysAclModuleService {

    @Resource
    private SysAclModuleMapper sysAclModuleMapper;

    @Resource
    private SysAclMapper sysAclMapper;

    @Resource
    private SysLogService sysLogService;

    public void save(AclModuleParam param) {

        BeanValidator.check(param);
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的权限模块");
        }

        SysAclModule sysAclModule = SysAclModule.builder()
                .name(param.getName())
                .parentId(param.getParentId())
                .seq(param.getSeq())
                .status(param.getStatus())
                .remark(param.getRemark())
                .build();
        sysAclModule.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        sysAclModule.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysAclModule.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysAclModule.setOperateTime(new Date());

        sysAclModuleMapper.insertSelective(sysAclModule);
        sysLogService.saveAclModuleLog(null, sysAclModule);

    }

    public void update(AclModuleParam param) {

        BeanValidator.check(param);
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {

            throw new ParamException("同一层级下存在相同名称的权限模块");

        }

        SysAclModule before = sysAclModuleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的权限模块不存在");

        SysAclModule after = SysAclModule.builder()
                .name(param.getName())
                .parentId(param.getParentId())
                .seq(param.getSeq())
                .status(param.getStatus())
                .remark(param.getRemark())
                .id(param.getId())
                .build();
        after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());

        updateWithChild(before, after);
        sysLogService.saveAclModuleLog(before, after);

    }

    @Transactional(rollbackFor = Exception.class)
    void updateWithChild(SysAclModule before, SysAclModule after) {

        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();

        if (!after.getLevel().equals(before.getLevel())) {

            List<SysAclModule> aclModuleList = sysAclModuleMapper.getChildAclModuleByLevel(before.getLevel());
            if (CollectionUtils.isNotEmpty(aclModuleList)) {

                for (SysAclModule aclModule : aclModuleList
                        ) {
                    String level = aclModule.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0) {

                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        aclModule.setLevel(level);

                    }
                }

            }

            sysAclModuleMapper.batchUpdateLevel(aclModuleList);

        }

        sysAclModuleMapper.updateByPrimaryKey(after);

    }

    private boolean checkExist(Integer parentId, String name, Integer aclModuleId) {

        return sysAclModuleMapper.countByNameAndParentId(parentId, name, aclModuleId) > 0;

    }

    private String getLevel(Integer aclModuleId) {

        SysAclModule aclModule = sysAclModuleMapper.selectByPrimaryKey(aclModuleId);
        if (aclModule == null) {

            return null;

        }
        return aclModule.getLevel();

    }

    public void delete(int aclModuleId) {

        SysAclModule sysAclModule = sysAclModuleMapper.selectByPrimaryKey(aclModuleId);
        Preconditions.checkNotNull(sysAclModule, "待删除的权限模块不存在，无法删除");

        if (sysAclModuleMapper.countByParentId(aclModuleId) > 0) {

            throw new ParamException("当前模块下有子模块，无法删除");

        }

        if (sysAclMapper.countByAclModuleId(aclModuleId) > 0) {

            throw new ParamException("当前模块下有权限点，无法删除");

        }

        sysAclModuleMapper.deleteByPrimaryKey(aclModuleId);

    }

}
