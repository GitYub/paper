package com.yxy.dao;

import com.yxy.beans.PageQuery;
import com.yxy.dto.SearchPaperDto;
import com.yxy.model.SysPaper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysPaperMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(SysPaper record);

    int insertSelective(SysPaper record);

    SysPaper selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysPaper record);

    int updateByPrimaryKey(SysPaper record);

    List<SysPaper> getPageListBySearchDto(@Param("idList") List<Integer> idList, @Param("dto") SearchPaperDto dto, @Param("page") PageQuery page);

    int countBySearchDto(@Param("idList") List<Integer> idList, @Param("dto")SearchPaperDto dto);

}