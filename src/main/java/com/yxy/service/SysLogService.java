package com.yxy.service;

import com.google.common.base.Preconditions;
import com.yxy.beans.LogType;
import com.yxy.beans.PageQuery;
import com.yxy.beans.PageResult;
import com.yxy.common.RequestHolder;
import com.yxy.dao.*;
import com.yxy.dto.SearchLogDto;
import com.yxy.exception.ParamException;
import com.yxy.model.*;
import com.yxy.param.SearchLogParam;
import com.yxy.util.BeanValidator;
import com.yxy.util.IpUtil;
import com.yxy.util.JsonMapper;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * SysLogService
 *
 * @author 余昕宇
 * @date 2018-06-23 11:01
 **/
@Service
public class SysLogService {

    @Resource
    private SysLogMapper sysLogMapper;

    @Resource
    private SysModuleMapper sysModuleMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysAclModuleMapper sysAclModuleMapper;

    @Resource
    private SysAclMapper sysAclMapper;

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysRoleAclService sysRoleAclService;

    @Resource
    private SysRoleUserService sysRoleUserService;

    @Resource
    private SysAnnouncementMapper sysAnnouncementMapper;

    public void recover(int id) {

        SysLogWithBLOBs sysLogWithBLOBs = sysLogMapper.selectByPrimaryKey(id);
        Preconditions.checkNotNull(sysLogWithBLOBs, "待还原的记录不存在");

        switch (sysLogWithBLOBs.getType()) {

            case LogType.TYPE_MODULE:
                SysModule beforeModule = sysModuleMapper.selectByPrimaryKey(sysLogWithBLOBs.getTargetId());
                Preconditions.checkNotNull(beforeModule, "待还原的论文模块不存在");
                if (StringUtils.isBlank(sysLogWithBLOBs.getNewValue()) || StringUtils.isBlank(sysLogWithBLOBs.getOldValue())) {

                    throw  new ParamException("新增和删除操作不做还原");

                }

                SysModule afterModule = JsonMapper.String2Obj(sysLogWithBLOBs.getOldValue(), new TypeReference<SysModule>() {
                });
                afterModule.setOperator(RequestHolder.getCurrentUser().getUsername());
                afterModule.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                afterModule.setOperateTime(new Date());

                sysModuleMapper.updateByPrimaryKeySelective(afterModule);
                saveModuleLog(beforeModule, afterModule);
                break;

            case LogType.TYPE_USER:
                SysUser beforeUser = sysUserMapper.selectByPrimaryKey(sysLogWithBLOBs.getTargetId());
                Preconditions.checkNotNull(beforeUser, "待还原的用户不存在");
                if (StringUtils.isBlank(sysLogWithBLOBs.getNewValue()) || StringUtils.isBlank(sysLogWithBLOBs.getOldValue())) {

                    throw new ParamException("新增和删除操作不做还原");

                }

                SysUser afterUser = JsonMapper.String2Obj(sysLogWithBLOBs.getOldValue(), new TypeReference<SysUser>() {
                });
                afterUser.setOperator(RequestHolder.getCurrentUser().getUsername());
                afterUser.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                afterUser.setOperateTime(new Date());

                sysUserMapper.updateByPrimaryKeySelective(afterUser);
                saveUserLog(beforeUser, afterUser);
                break;

            case LogType.TYPE_ACL_MODULE:
                SysAclModule beforeAclModule = sysAclModuleMapper.selectByPrimaryKey(sysLogWithBLOBs.getTargetId());
                Preconditions.checkNotNull(beforeAclModule, "待还原的权限模块不存在");
                if (StringUtils.isBlank(sysLogWithBLOBs.getNewValue()) || StringUtils.isBlank(sysLogWithBLOBs.getOldValue())) {

                    throw  new ParamException("新增和删除操作不做还原");

                }

                SysAclModule afterAclModule = JsonMapper.String2Obj(sysLogWithBLOBs.getOldValue(), new TypeReference<SysAclModule>() {
                });
                afterAclModule.setOperator(RequestHolder.getCurrentUser().getUsername());
                afterAclModule.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                afterAclModule.setOperateTime(new Date());

                sysAclModuleMapper.updateByPrimaryKeySelective(afterAclModule);
                saveAclModuleLog(beforeAclModule, afterAclModule);
                break;

            case LogType.TYPE_ACL:
                SysAcl beforeAcl = sysAclMapper.selectByPrimaryKey(sysLogWithBLOBs.getTargetId());
                Preconditions.checkNotNull(beforeAcl, "待还原的权限点不存在");
                if (StringUtils.isBlank(sysLogWithBLOBs.getNewValue()) || StringUtils.isBlank(sysLogWithBLOBs.getOldValue())) {

                    throw  new ParamException("新增和删除操作不做还原");

                }

                SysAcl afterAcl = JsonMapper.String2Obj(sysLogWithBLOBs.getOldValue(), new TypeReference<SysAcl>() {
                });
                afterAcl.setOperator(RequestHolder.getCurrentUser().getUsername());
                afterAcl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                afterAcl.setOperateTime(new Date());

                sysAclMapper.updateByPrimaryKeySelective(afterAcl);
                saveAclLog(beforeAcl, afterAcl);
                break;

            case LogType.TYPE_ROLE:
                SysRole beforeRole = sysRoleMapper.selectByPrimaryKey(sysLogWithBLOBs.getTargetId());
                Preconditions.checkNotNull(beforeRole, "待还原的角色不存在");
                if (StringUtils.isBlank(sysLogWithBLOBs.getNewValue()) || StringUtils.isBlank(sysLogWithBLOBs.getOldValue())) {

                    throw  new ParamException("新增和删除操作不做还原");

                }

                SysRole afterRole = JsonMapper.String2Obj(sysLogWithBLOBs.getOldValue(), new TypeReference<SysRole>() {
                });
                afterRole.setOperator(RequestHolder.getCurrentUser().getUsername());
                afterRole.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                afterRole.setOperateTime(new Date());

                sysRoleMapper.updateByPrimaryKeySelective(afterRole);
                saveRoleLog(beforeRole, afterRole);
                break;

            case LogType.TYPE_ROLE_ACL:
                SysRole aclRole = sysRoleMapper.selectByPrimaryKey(sysLogWithBLOBs.getTargetId());
                Preconditions.checkNotNull(aclRole, "待还原的角色不存在");

                sysRoleAclService.changeRoleAcls(sysLogWithBLOBs.getTargetId(), JsonMapper.String2Obj(sysLogWithBLOBs.getOldValue(), new TypeReference<List<Integer>>() {
                }));

                break;

            case LogType.TYPE_ROLE_USER:
                SysRole userRole = sysRoleMapper.selectByPrimaryKey(sysLogWithBLOBs.getTargetId());
                Preconditions.checkNotNull(userRole, "待还原的角色不存在");

                sysRoleUserService.changeRoleUsers(sysLogWithBLOBs.getTargetId(), JsonMapper.String2Obj(sysLogWithBLOBs.getOldValue(), new TypeReference<List<Integer>>() {
                }));

                break;

            default:
        }

    }

    public PageResult searchPageList(SearchLogParam param, PageQuery pageQuery) {

        BeanValidator.check(pageQuery);
        SearchLogDto dto = new SearchLogDto();
        dto.setType(param.getType());
        if (StringUtils.isNotBlank(param.getBeforeSeq())) {

            dto.setBeforeSeq("%" + param.getBeforeSeq() + "%");

        }

        if (StringUtils.isNotBlank(param.getAfterSeq())) {

            dto.setAfterSeq("%" + param.getAfterSeq() + "%");

        }

        if (StringUtils.isNotBlank(param.getOperator())) {

            dto.setOperator("%" + param.getOperator() + "%");

        }

        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (StringUtils.isNotBlank(param.getFromTime())) {

                dto.setFromTime(dateFormat.parse(param.getFromTime()));

            }

            if (StringUtils.isNotBlank(param.getToTime())) {

                dto.setToTime(dateFormat.parse(param.getToTime()));

            }

        } catch (Exception e) {

            throw new ParamException("传入的日期格式错误，正确格式为：yyyy-MM-dd HH:mm:ss");

        }

        int count = sysLogMapper.countBySearchDto(dto);
        if (count > 0) {

            List<SysLogWithBLOBs> logList = sysLogMapper.getPageListBySearchDto(dto, pageQuery);
            return PageResult.<SysLogWithBLOBs>builder().total(count).data(logList).build();

        }

        return PageResult.builder().build();

    }

    void saveModuleLog(SysModule before, SysModule after) {

        SysLogWithBLOBs sysLogWithBLOBs = new SysLogWithBLOBs();
        sysLogWithBLOBs.setType(LogType.TYPE_MODULE);
        sysLogWithBLOBs.setTargetId(after == null ? before.getId() : after.getId());
        sysLogWithBLOBs.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLogWithBLOBs.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLogWithBLOBs.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLogWithBLOBs.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLogWithBLOBs.setOperateTime(new Date());
        sysLogWithBLOBs.setStatus(1);

        sysLogMapper.insertSelective(sysLogWithBLOBs);

    }

    void saveUserLog(SysUser before, SysUser after) {

        SysLogWithBLOBs sysLogWithBLOBs = new SysLogWithBLOBs();
        sysLogWithBLOBs.setType(LogType.TYPE_USER);
        sysLogWithBLOBs.setTargetId(after == null ? before.getId() : after.getId());
        sysLogWithBLOBs.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLogWithBLOBs.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLogWithBLOBs.setOperator(RequestHolder.getCurrentUser() == null ? "" : RequestHolder.getCurrentUser().getUsername());
        sysLogWithBLOBs.setOperateIp(RequestHolder.getCurrentRequest() == null ? "" : IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLogWithBLOBs.setOperateTime(new Date());
        sysLogWithBLOBs.setStatus(1);

        sysLogMapper.insertSelective(sysLogWithBLOBs);

    }

    void saveAclModuleLog(SysAclModule before, SysAclModule after) {

        SysLogWithBLOBs sysLogWithBLOBs = new SysLogWithBLOBs();
        sysLogWithBLOBs.setType(LogType.TYPE_ACL_MODULE);
        sysLogWithBLOBs.setTargetId(after == null ? before.getId() : after.getId());
        sysLogWithBLOBs.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLogWithBLOBs.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLogWithBLOBs.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLogWithBLOBs.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLogWithBLOBs.setOperateTime(new Date());
        sysLogWithBLOBs.setStatus(1);

        sysLogMapper.insertSelective(sysLogWithBLOBs);

    }

    void saveAclLog(SysAcl before, SysAcl after) {

        SysLogWithBLOBs sysLogWithBLOBs = new SysLogWithBLOBs();
        sysLogWithBLOBs.setType(LogType.TYPE_ACL);
        sysLogWithBLOBs.setTargetId(after == null ? before.getId() : after.getId());
        sysLogWithBLOBs.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLogWithBLOBs.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLogWithBLOBs.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLogWithBLOBs.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLogWithBLOBs.setOperateTime(new Date());
        sysLogWithBLOBs.setStatus(1);

        sysLogMapper.insertSelective(sysLogWithBLOBs);

    }

    void saveRoleLog(SysRole before, SysRole after) {

        SysLogWithBLOBs sysLogWithBLOBs = new SysLogWithBLOBs();
        sysLogWithBLOBs.setType(LogType.TYPE_ROLE);
        sysLogWithBLOBs.setTargetId(after == null ? before.getId() : after.getId());
        sysLogWithBLOBs.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLogWithBLOBs.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLogWithBLOBs.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLogWithBLOBs.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLogWithBLOBs.setOperateTime(new Date());
        sysLogWithBLOBs.setStatus(1);

        sysLogMapper.insertSelective(sysLogWithBLOBs);

    }

    void savePaperLog(SysPaper before, SysPaper after) {

        SysLogWithBLOBs sysLogWithBLOBs = new SysLogWithBLOBs();
        sysLogWithBLOBs.setType(LogType.TYPE_PAPER);
        sysLogWithBLOBs.setTargetId(after == null ? before.getId() : after.getId());
        sysLogWithBLOBs.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLogWithBLOBs.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLogWithBLOBs.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLogWithBLOBs.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLogWithBLOBs.setOperateTime(new Date());
        sysLogWithBLOBs.setStatus(1);

        sysLogMapper.insertSelective(sysLogWithBLOBs);

    }

    void saveAnnouncementLog(SysAnnouncement before, SysAnnouncement after) {

        SysLogWithBLOBs sysLogWithBLOBs = new SysLogWithBLOBs();
        sysLogWithBLOBs.setType(LogType.TYPE_ANNOUNCEMENT);
        sysLogWithBLOBs.setTargetId(after == null ? before.getId() : after.getId());
        sysLogWithBLOBs.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLogWithBLOBs.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLogWithBLOBs.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLogWithBLOBs.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLogWithBLOBs.setOperateTime(new Date());
        sysLogWithBLOBs.setStatus(1);

        sysLogMapper.insertSelective(sysLogWithBLOBs);

    }

}
