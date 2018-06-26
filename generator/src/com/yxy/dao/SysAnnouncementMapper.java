package com.yxy.dao;

import com.yxy.model.SysAnnouncement;

public interface SysAnnouncementMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAnnouncement record);

    int insertSelective(SysAnnouncement record);

    SysAnnouncement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAnnouncement record);

    int updateByPrimaryKey(SysAnnouncement record);
}