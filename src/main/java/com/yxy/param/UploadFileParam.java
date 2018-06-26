package com.yxy.param;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * UploadFileParam
 *
 * @author 余昕宇
 * @date 2018-06-26 21:19
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UploadFileParam {

    private MultipartFile file;

    private Integer type;

    private String title;

}
