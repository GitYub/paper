package com.yxy.dto;

import com.google.common.collect.Lists;
import com.yxy.model.SysAclModule;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * AclModuleLevelDto
 *
 * @author 余昕宇
 * @date 2018-06-21 2:18
 **/
@Getter
@Setter
@ToString
public class AclModuleLevelDto extends SysAclModule {

    private List<AclModuleLevelDto> aclModuleList = Lists.newArrayList();

    private List<AclDto> aclList = Lists.newArrayList();

    public static AclModuleLevelDto adapt(SysAclModule module) {

        AclModuleLevelDto dto = new AclModuleLevelDto();
        BeanUtils.copyProperties(module, dto);
        return dto;

    }

}
