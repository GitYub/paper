package com.yxy.dao;

import com.yxy.model.SysModule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysModuleMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(SysModule record);

    int insertSelective(SysModule record);

    SysModule selectByPrimaryKey(@Param("id")Integer id);

    int updateByPrimaryKeySelective(SysModule record);

    int updateByPrimaryKey(SysModule record);

    List<SysModule> getAllModule();

    List<SysModule> getChildModuleByLevel(@Param("level") String level);

    void batchUpdateLevel(@Param("sysModuleList") List<SysModule> moduleList);

    int countByNameAndParentId(@Param("parentId") Integer parentId, @Param("name") String name, @Param("id") Integer id);

    int countByParentId(@Param("moduleId") int moduleId);

}