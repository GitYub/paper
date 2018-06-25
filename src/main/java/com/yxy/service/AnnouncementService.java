package com.yxy.service;

import com.yxy.beans.PageQuery;
import com.yxy.beans.PageResult;
import com.yxy.dao.SysAnnouncementMapper;
import com.yxy.model.SysAnnouncement;
import com.yxy.param.AnnouncementParam;
import com.yxy.util.BeanValidator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

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

    public void save(AnnouncementParam param) {

        BeanValidator.check(param);
        SysAnnouncement announcement = SysAnnouncement.builder()
                //.authorId(RequestHolder.getCurrentUser().getId())
                .authorId(1)
                .content(param.getContent())
                //.operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()))
                .operateIp("")
                .operateTime(new Date())
                //.operator(RequestHolder.getCurrentUser().getUsername())
                .operator("")
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

    public PageResult<SysAnnouncement> getPage(PageQuery page) {

        int count = sysAnnouncementMapper.count(page);
        if (count > 0) {

            List<SysAnnouncement> announcementList = sysAnnouncementMapper.getPage(page);

            return PageResult.<SysAnnouncement>builder()
                    .data(announcementList)
                    .total(count)
                    .build();

        }

        return PageResult.<SysAnnouncement>builder().build();

    }

}
