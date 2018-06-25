package com.yxy.dto;

import com.google.common.collect.Lists;
import com.yxy.model.SysModule;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * ModuleLevelDto
 *
 * @author 余昕宇
 * @date 2018-06-18 12:52
 **/
@Getter
@Setter
@ToString
public class ModuleLevelDto extends SysModule {

    private List<ModuleLevelDto> moduleList = Lists.newArrayList();

    public static ModuleLevelDto adapt(SysModule module) {

        ModuleLevelDto dto = new ModuleLevelDto();
        BeanUtils.copyProperties(module, dto);
        return dto;

    }

}
