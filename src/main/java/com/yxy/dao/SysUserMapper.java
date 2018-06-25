package com.yxy.dao;

import com.yxy.beans.PageQuery;
import com.yxy.model.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser findByKeyword(@Param("keyword") String keyword);

    int countByEmail(@Param("email") String email, @Param("id") Integer id);

    int countByTelephone(@Param("telephone") String telephone, @Param("id") Integer id);

    int countByModuleId(@Param("moduleId") Integer moduleId);

    List<SysUser> getPageByModuleId(@Param("moduleId") Integer moduleId, @Param("page") PageQuery page);

    List<SysUser> getByIdList(@Param("idList") List<Integer> idList);

    List<Integer> getIdByUsername(@Param("username") String username);

    List<SysUser> getAll();

}