package com.yxy.dao;

import com.yxy.model.SysPaper;

public interface SysPaperMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysPaper record);

    int insertSelective(SysPaper record);

    SysPaper selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysPaper record);

    int updateByPrimaryKey(SysPaper record);
}