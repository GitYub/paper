package com.yxy.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * OssUtil
 *
 * @author 余昕宇
 * @date 2018-06-26 0:31
 **/
public class OssUtil {

    // endpoint以杭州为例，其它region请按实际情况填写。
    private static final String ENDPOINT = "http://oss-cn-hangzhou.aliyuncs.com";

    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
    private static final String ACCESS_KEY_ID = "LTAIQfY1B2WSGiYU";

    private static final String ACCESS_KEY_SECRET = "Q6zA2VEvo5kmluqugxGOY1kz4VQlge";

    private static final String BUCKET_NAME = "yuxinyu";

    public static void save(File file) {

        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        // 使用访问OSS。
        ossClient.putObject(BUCKET_NAME, file.getName(), file);
        // 关闭ossClient。
        ossClient.shutdown();

    }

    public static void download(HttpServletResponse response, String key) {

        OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        OSSObject ossObject = ossClient.getObject(BUCKET_NAME, key.substring(key.lastIndexOf("/") + 1));
        InputStream inputStream = ossObject.getObjectContent();
        ossClient.shutdown();

        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os;

        try {

            os = response.getOutputStream();
            bis = new BufferedInputStream(inputStream);
            int i = bis.read(buff);

            while (i != -1) {

                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);

            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            if (bis != null) {

                try {

                    bis.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }

            }

        }

    }

}
