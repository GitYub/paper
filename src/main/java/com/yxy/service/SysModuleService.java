package com.yxy.service;

import com.google.common.base.Preconditions;
import com.yxy.common.RequestHolder;
import com.yxy.dao.SysModuleMapper;
import com.yxy.dao.SysUserMapper;
import com.yxy.exception.ParamException;
import com.yxy.model.SysModule;
import com.yxy.param.ModuleParam;
import com.yxy.util.BeanValidator;
import com.yxy.util.IpUtil;
import com.yxy.util.LevelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * SysModuleService
 *
 * @author 余昕宇
 * @date 2018-06-18 12:31
 **/
@Service
public class SysModuleService {

    @Resource
    private SysModuleMapper sysModuleMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysLogService sysLogService;

    public void save(ModuleParam param) {

        BeanValidator.check(param);
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的模块");
        }

        SysModule module = SysModule.builder().name(param.getName())
                .parentId(param.getParentId())
                .seq(param.getSeq())
                .remark(param.getRemark()).build();

        module.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        module.setOperator(RequestHolder.getCurrentUser().getUsername());
        module.setOperateTime(new Date());
        module.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysModuleMapper.insertSelective(module);
        sysLogService.saveModuleLog(null, module);

    }

    public void update(ModuleParam param) {

        BeanValidator.check(param);
        if (checkExist(param.getParentId(), param.getName(), param.getId())) {

            throw new ParamException("同一层级下存在相同名称的模块");

        }

        SysModule before = sysModuleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的模块不存在");

        SysModule after = SysModule.builder().id(param.getId())
                .name(param.getName())
                .parentId(param.getParentId())
                .seq(param.getSeq())
                .remark(param.getRemark()).build();
        after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateTime(new Date());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));

        updateWithChild(before, after);
        sysLogService.saveModuleLog(before, after);

    }

    @Transactional(rollbackFor = Exception.class)
    void updateWithChild(SysModule before, SysModule after) {

        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();

        if (!after.getLevel().equals(before.getLevel())) {

            List<SysModule> moduleList = sysModuleMapper.getChildModuleByLevel(before.getLevel());
            if (CollectionUtils.isNotEmpty(moduleList)) {

                for (SysModule module : moduleList
                     ) {
                    String level = module.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0) {

                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        module.setLevel(level);

                    }
                }

            }

            sysModuleMapper.batchUpdateLevel(moduleList);

        }

        sysModuleMapper.updateByPrimaryKey(after);

    }

    private boolean checkExist(Integer parentId, String name, Integer moduleId) {

        return sysModuleMapper.countByNameAndParentId(parentId, name, moduleId) > 0;

    }

    private String getLevel(Integer moduleId) {

        SysModule module = sysModuleMapper.selectByPrimaryKey(moduleId);
        if (module == null) {

            return null;

        }
        return module.getLevel();

    }

    public void delete(int id) {

        SysModule module = sysModuleMapper.selectByPrimaryKey(id);
        Preconditions.checkNotNull(module, "待删除的模块不存在，无法删除");

        if (sysModuleMapper.countByParentId(id) > 0) {

            throw new ParamException("当前模块下有子模块，无法删除");

        }

        if (sysUserMapper.countByModuleId(id) > 0) {

            throw new ParamException("当前模块下有用户，无法删除");

        }

        sysModuleMapper.deleteByPrimaryKey(id);

    }

}
