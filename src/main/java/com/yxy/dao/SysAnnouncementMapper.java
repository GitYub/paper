package com.yxy.dao;

import com.yxy.beans.PageQuery;
import com.yxy.model.SysAnnouncement;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAnnouncementMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(SysAnnouncement record);

    int insertSelective(SysAnnouncement record);

    SysAnnouncement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAnnouncement record);

    int updateByPrimaryKeyWithBLOBs(SysAnnouncement record);

    int updateByPrimaryKey(SysAnnouncement record);

    List<SysAnnouncement> getPage(@Param("page") PageQuery page);

    int count(@Param("page") PageQuery page);

}