package com.yxy.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yxy.beans.PageQuery;
import com.yxy.beans.PageResult;
import com.yxy.common.RequestHolder;
import com.yxy.dao.SysAnnouncementMapper;
import com.yxy.dao.SysUserMapper;
import com.yxy.dto.SysAnnouncementDto;
import com.yxy.model.SysAnnouncement;
import com.yxy.model.SysUser;
import com.yxy.param.AnnouncementParam;
import com.yxy.util.BeanValidator;
import com.yxy.util.IpUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * AnnouncementService
 *
 * @author 余昕宇
 * @date 2018-06-24 0:02
 **/
@Service
public class AnnouncementService {

    @Resource
    private SysAnnouncementMapper sysAnnouncementMapper;

    @Resource
    private SysLogService sysLogService;

    @Resource
    private SysUserMapper sysUserMapper;

    public void save(AnnouncementParam param) {

        BeanValidator.check(param);
        SysAnnouncement announcement = SysAnnouncement.builder()
                .authorId(RequestHolder.getCurrentUser().getId())
                .content(param.getContent())
                .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()))
                .operateTime(new Date())
                .operator(RequestHolder.getCurrentUser().getUsername())
                .status(0)
                .title(param.getTitle())
                .uploadTime(new Date())
                .build();

        sysAnnouncementMapper.insertSelective(announcement);
        sysLogService.saveAnnouncementLog(null, announcement);

    }

    public void disable(int id) {

        SysAnnouncement before = sysAnnouncementMapper.selectByPrimaryKey(id);
        try {

            SysAnnouncement after = before.clone();
            after.setStatus(1);
            sysAnnouncementMapper.updateByPrimaryKeyWithBLOBs(after);
            sysLogService.saveAnnouncementLog(before, after);

        } catch (CloneNotSupportedException e) {

            e.printStackTrace();

        }

    }

    public PageResult<SysAnnouncementDto> getPage(PageQuery page) {

        int count = sysAnnouncementMapper.count(page);
        if (count > 0) {

            List<SysAnnouncement> announcementList = sysAnnouncementMapper.getPage(page);
            List<SysUser> userList = sysUserMapper.getByIdList(announcementList.stream().map(SysAnnouncement::getAuthorId).collect(Collectors.toList()));

            HashMap<Integer, String> hashMap = Maps.newHashMap();
            for (SysUser user : userList) {

                hashMap.put(user.getId(), user.getUsername());

            }

            List<SysAnnouncementDto> announcementDtoList = Lists.newArrayList();
            for (SysAnnouncement announcement : announcementList) {

                SysAnnouncementDto announcementDto = SysAnnouncementDto.builder()
                        .author(hashMap.get(announcement.getAuthorId()))
                        .title(announcement.getTitle())
                        .id(announcement.getId())
                        .content(announcement.getContent())
                        .uploadTime(announcement.getUploadTime())
                        .build();
                announcementDtoList.add(announcementDto);

            }

            return PageResult.<SysAnnouncementDto>builder()
                    .data(announcementDtoList)
                    .total(count)
                    .build();

        }

        return PageResult.<SysAnnouncementDto>builder().build();

    }

}
