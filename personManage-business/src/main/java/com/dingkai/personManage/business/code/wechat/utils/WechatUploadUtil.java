package com.dingkai.personManage.business.code.wechat.utils;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author dingkai1
 * @desc
 * @date 2021/3/12 17:49
 */
public class WechatUploadUtil {

    public static MultiValueMap<String, Object> getParams(String filePath, String fileType) throws IOException {
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        FileSystemResource fileResource = new FileSystemResource(new File(filePath));
        param.add(fileType, fileResource);
        return param;
    }

    public static MultiValueMap<String, Object> getParams(File file, String fileType) throws IOException {
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        FileSystemResource fileResource = new FileSystemResource(file);
        param.add(fileType, fileResource);
        return param;
    }

}
