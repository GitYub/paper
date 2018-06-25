package com.yxy.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.yxy.dao.SysAclMapper;
import com.yxy.dao.SysAclModuleMapper;
import com.yxy.dao.SysModuleMapper;
import com.yxy.dto.AclDto;
import com.yxy.dto.AclModuleLevelDto;
import com.yxy.dto.ModuleLevelDto;
import com.yxy.model.SysAcl;
import com.yxy.model.SysAclModule;
import com.yxy.model.SysModule;
import com.yxy.model.SysRole;
import com.yxy.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * SysTreeService
 *
 * @author 余昕宇
 * @date 2018-06-18 12:56
 **/
@Service
public class SysTreeService {

    @Resource
    private SysModuleMapper sysModuleMapper;

    @Resource
    private SysAclModuleMapper sysAclModuleMapper;

    @Resource
    private SysCoreService sysCoreService;

    @Resource
    private SysAclMapper sysAclMapper;

    public List<AclModuleLevelDto> userAclTree(int userId) {

        // 1.当前用户已经分配的权限点
        List<SysAcl> userAclList = sysCoreService.getUserAclList(userId);
        List<AclDto> aclDtoList = Lists.newArrayList();

        for (SysAcl acl : userAclList) {

            AclDto dto = AclDto.adapt(acl);
            dto.setHasAcl(true);
            dto.setChecked(true);
            aclDtoList.add(dto);

        }

        return aclListToTree(aclDtoList);

    }

    public List<AclModuleLevelDto> roleTree(int roleId) {

        // 1.当前用户已经分配的权限点
        List<SysAcl> userAclList = sysCoreService.getCurrentUserAclList();

        // 2.当前角色已经分配的权限点
        List<SysAcl> roleAclList = sysCoreService.getRoleAclList(roleId);

        // 3.当前系统所有的权限点
        List<AclDto> aclDtoList = Lists.newArrayList();

        Set<Integer> userAclIdSet = userAclList.stream().map(SysAcl::getId).collect(Collectors.toSet());
        Set<Integer> roleAclIdSet = roleAclList.stream().map(SysAcl::getId).collect(Collectors.toSet());

        List<SysAcl> allAclList = sysAclMapper.getAll();

        for (SysAcl acl : allAclList) {

            AclDto dto = AclDto.adapt(acl);
            if (userAclIdSet.contains(acl.getId())) {

                dto.setHasAcl(true);

            }

            if (roleAclIdSet.contains(acl.getId())) {

                dto.setChecked(true);

            }
            aclDtoList.add(dto);

        }

        return aclListToTree(aclDtoList);

    }

    private List<AclModuleLevelDto> aclListToTree(List<AclDto> aclDtoList) {

        if (CollectionUtils.isEmpty(aclDtoList)) {

            return Lists.newArrayList();

        }

        List<AclModuleLevelDto> aclModuleLevelList = aclModuleTree();

        Multimap<Integer, AclDto> multimap = ArrayListMultimap.create();
        for (AclDto acl : aclDtoList) {

            if (acl.getStatus() == 1) {

                multimap.put(acl.getAclModuleId(), acl);

            }

        }

        bindAclWithOrder(aclModuleLevelList, multimap);
        return aclModuleLevelList;

    }

    private void bindAclWithOrder(List<AclModuleLevelDto> aclModuleLevelDtoList, Multimap<Integer, AclDto> moduleMap) {

        if (CollectionUtils.isEmpty(aclModuleLevelDtoList)) {

            return;

        }

        for (AclModuleLevelDto aclModuleLevelDto : aclModuleLevelDtoList) {

            List<AclDto> aclDtoList = (List<AclDto>) moduleMap.get(aclModuleLevelDto.getId());
            if (CollectionUtils.isNotEmpty(aclDtoList)) {

                aclDtoList.sort(aclDtoComparator);
                aclModuleLevelDto.setAclList(aclDtoList);

            }
            bindAclWithOrder(aclModuleLevelDto.getAclModuleList(), moduleMap);

        }

    }

    public List<AclModuleLevelDto> aclModuleTree() {

        List<SysAclModule> aclModuleList = sysAclModuleMapper.getAllAclModule();
        List<AclModuleLevelDto> aclModuleLevelDtoList = Lists.newArrayList();

        for (SysAclModule aclModule : aclModuleList) {
            AclModuleLevelDto dto = AclModuleLevelDto.adapt(aclModule);
            aclModuleLevelDtoList.add(dto);
        }

        return aclModuleListToTree(aclModuleLevelDtoList);

    }

    private List<AclModuleLevelDto> aclModuleListToTree(List<AclModuleLevelDto> aclModuleLevelDtoList) {

        if (CollectionUtils.isEmpty(aclModuleLevelDtoList)) {
            return Lists.newArrayList();
        }

        Multimap<String, AclModuleLevelDto> aclModuleLevelDtoMultimap = ArrayListMultimap.create();
        List<AclModuleLevelDto> rootList = Lists.newArrayList();

        for (AclModuleLevelDto dto : aclModuleLevelDtoList) {
            aclModuleLevelDtoMultimap.put(dto.getLevel(), dto);
            if (LevelUtil.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }

        // 按照seq从小到大排序
        rootList.sort(Comparator.comparingInt(SysAclModule::getSeq));

        // 递归生成树
        transformAclModuleTree(rootList, LevelUtil.ROOT, aclModuleLevelDtoMultimap);
        return rootList;

    }

    public List<ModuleLevelDto> moduleTree() {

        List<SysModule> moduleList = sysModuleMapper.getAllModule();
        List<ModuleLevelDto> moduleLevelDtoList = Lists.newArrayList();

        for (SysModule module : moduleList) {
            ModuleLevelDto dto = ModuleLevelDto.adapt(module);
            moduleLevelDtoList.add(dto);
        }

        return moduleListToTree(moduleLevelDtoList);

    }

    private List<ModuleLevelDto> moduleListToTree(List<ModuleLevelDto> moduleLevelDtoList) {

        if (CollectionUtils.isEmpty(moduleLevelDtoList)) {
            return Lists.newArrayList();
        }

        Multimap<String, ModuleLevelDto> moduleLevelDtoMultimap = ArrayListMultimap.create();
        List<ModuleLevelDto> rootList = Lists.newArrayList();

        for (ModuleLevelDto dto : moduleLevelDtoList) {
            moduleLevelDtoMultimap.put(dto.getLevel(), dto);
            if (LevelUtil.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }

        // 按照seq从小到大排序
        rootList.sort(Comparator.comparingInt(SysModule::getSeq));

        // 递归生成树
        transformModuleTree(rootList, LevelUtil.ROOT, moduleLevelDtoMultimap);
        return rootList;

    }

    // level:0, 0, all 0 -> 0.1, 0.2
    // level:0.1
    // level:0.2
    private void transformModuleTree(List<ModuleLevelDto> moduleLevelDtoList, String level, Multimap<String, ModuleLevelDto> moduleLevelDtoMultimap) {

        for (ModuleLevelDto moduleLevelDto : moduleLevelDtoList) {

            // 遍历该层的每个元素
            // 处理当前层级的数据
            String nextLevel = LevelUtil.calculateLevel(level, moduleLevelDto.getId());
            // 处理下一层
            List<ModuleLevelDto> tmpModuleList = (List<ModuleLevelDto>) moduleLevelDtoMultimap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tmpModuleList)) {

                // 排序
                tmpModuleList.sort(moduleLevelDtoComparator);
                // 设置下一模块
                moduleLevelDto.setModuleList(tmpModuleList);
                // 进入到下一层处理
                transformModuleTree(tmpModuleList, nextLevel, moduleLevelDtoMultimap);

            }

        }

    }

    private void transformAclModuleTree(List<AclModuleLevelDto> aclModuleLevelDtoList, String level, Multimap<String, AclModuleLevelDto> aclModuleLevelDtoMultimap) {

        for (AclModuleLevelDto aclModuleLevelDto : aclModuleLevelDtoList) {

            // 遍历该层的每个元素
            // 处理当前层级的数据
            String nextLevel = LevelUtil.calculateLevel(level, aclModuleLevelDto.getId());
            // 处理下一层
            List<AclModuleLevelDto> tmpAclModuleList = (List<AclModuleLevelDto>) aclModuleLevelDtoMultimap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tmpAclModuleList)) {

                // 排序
                tmpAclModuleList.sort(aclModuleLevelDtoComparator);
                // 设置下一模块
                aclModuleLevelDto.setAclModuleList(tmpAclModuleList);
                // 进入到下一层处理
                transformAclModuleTree(tmpAclModuleList, nextLevel, aclModuleLevelDtoMultimap);
            }

        }

    }

    private Comparator<ModuleLevelDto> moduleLevelDtoComparator = Comparator.comparingInt(SysModule::getSeq);

    private Comparator<AclModuleLevelDto> aclModuleLevelDtoComparator = Comparator.comparingInt(SysAclModule::getSeq);

    private Comparator<AclDto> aclDtoComparator = Comparator.comparingInt(SysAcl::getSeq);

}
