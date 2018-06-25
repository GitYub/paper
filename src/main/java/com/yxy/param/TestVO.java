package com.yxy.param;

import com.sun.xml.internal.org.jvnet.staxex.Base64EncoderStream;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Encoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * TestVO
 *
 * @author 余昕宇
 * @date 2018-06-18 11:06
 **/
@Getter
@Setter
@Slf4j
public class TestVO {

    @NotBlank
    private String msg;

    @NotNull
    private Integer id;

    public static void main(String[] args) throws NoSuchAlgorithmException {

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        String resulr = base64Encoder.encode(md5.digest("123456".getBytes()));
        log.info("res:{}", resulr);

    }

}
