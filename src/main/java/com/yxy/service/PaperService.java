package com.yxy.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yxy.beans.PageQuery;
import com.yxy.beans.PageResult;
import com.yxy.common.RequestHolder;
import com.yxy.dao.SysPaperMapper;
import com.yxy.dao.SysUserMapper;
import com.yxy.dto.SearchPaperDto;
import com.yxy.dto.SysPaperDto;
import com.yxy.exception.ParamException;
import com.yxy.model.SysPaper;
import com.yxy.model.SysUser;
import com.yxy.param.PaperParam;
import com.yxy.param.SearchPaperParam;
import com.yxy.util.BeanValidator;
import com.yxy.util.IpUtil;
import com.yxy.util.OssUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * PaperService
 *
 * @author 余昕宇
 * @date 2018-06-24 2:37
 **/
@Service
@Slf4j
public class PaperService {

    @Resource
    private SysPaperMapper sysPaperMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysLogService sysLogService;

    public void save(MultipartFile file, PaperParam param) {

        BeanValidator.check(param);
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        String name = UUID.randomUUID().toString() + suffix;
        File wordFile = new File(name);

        log.info("{}", wordFile.getName());

        try {

            file.transferTo(wordFile);
            OssUtil.save(wordFile);

            String url = "https://view.officeapps.live.com/op/view.aspx?src=https://yuxinyu.oss-cn-hangzhou.aliyuncs.com/"
                    + wordFile.getName();

            SysPaper paper = SysPaper.builder()
                    .authorId(RequestHolder.getCurrentUser().getId())
                    .moduleId(param.getModuleId())
                    .status(0)
                    .title(param.getTitle())
                    .uploadTime(new Date())
                    .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()))
                    .operator(RequestHolder.getCurrentUser().getUsername())
                    .operateTime(new Date())
                    .url(url)
                    .build();

            sysPaperMapper.insertSelective(paper);
            sysLogService.savePaperLog(null, paper);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    public void disable(int id) {

        SysPaper before = sysPaperMapper.selectByPrimaryKey(id);

        try {

            SysPaper after = before.clone();
            after.setStatus(1);
            sysPaperMapper.updateByPrimaryKeySelective(after);
            sysLogService.savePaperLog(before, after);

        } catch (CloneNotSupportedException e) {

            e.printStackTrace();

        }

    }

    public String download(int id) {

        return sysPaperMapper.selectByPrimaryKey(id).getUrl();

    }

    public PageResult searchPageList(SearchPaperParam param, PageQuery page) {

        BeanValidator.check(page);
        SearchPaperDto dto = new SearchPaperDto();

        dto.setModuleId(param.getModuleId());
        if (StringUtils.isNotBlank(param.getTitle())) {

            dto.setTitle("%" + param.getTitle() + "%");

        }

        List<Integer> userList = Lists.newArrayList();
        if (StringUtils.isNotBlank(param.getAuthor())) {

            userList = sysUserMapper.getIdByUsername("%" + param.getAuthor() + "%");
            if (CollectionUtils.isNotEmpty(userList)) {

                dto.setCount(userList.size());

            } else {

                dto.setCount(0);
                userList.add(0);

            }

        }

        if (StringUtils.isNotBlank(param.getOperator())) {

            dto.setOperator("%" + param.getOperator() + "%");

        }

        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (StringUtils.isNotBlank(param.getFromTime())) {

                dto.setFromTime(dateFormat.parse(param.getFromTime()));

            }

            if (StringUtils.isNotBlank(param.getToTime())) {

                dto.setToTime(dateFormat.parse(param.getToTime()));

            }

        } catch (Exception e) {

            throw new ParamException("传入的日期格式错误，正确格式为：yyyy-MM-dd HH:mm:ss");

        }

        int count = sysPaperMapper.countBySearchDto(userList, dto);
        if (count > 0) {

            List<SysPaper> paperList = sysPaperMapper.getPageListBySearchDto(userList, dto, page);
            List<SysUser> users= sysUserMapper.getByIdList(paperList.stream().map(SysPaper::getAuthorId).collect(Collectors.toList()));

            HashMap<Integer, String> hashMap = Maps.newHashMap();
            for (SysUser user : users) {

                hashMap.put(user.getId(), user.getUsername());

            }

            List<SysPaperDto> paperDtoList = Lists.newArrayList();
            for (SysPaper paper : paperList) {

                SysPaperDto announcementDto = SysPaperDto.builder()
                        .author(hashMap.get(paper.getAuthorId()))
                        .id(paper.getId())
                        .moduleId(paper.getModuleId())
                        .title(paper.getTitle())
                        .uploadTime(paper.getUploadTime())
                        .url(paper.getUrl())
                        .build();
                paperDtoList.add(announcementDto);

            }

            return PageResult.<SysPaperDto>builder().total(count).data(paperDtoList).build();

        }

        return PageResult.builder().build();

    }

}
