package com.chen.stencil.service;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.chen.stencil.common.response.Result;
import com.chen.stencil.common.response.ResultCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileService {


    @Value("${file.uploadFolder}")
    private String uploadFolder;
    @Value("${file.clientUrl}")
    private String clientUrl;

    public Result upload(MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            return new Result(ResultCode.FILE_EMPTY);
        }
        long size = file.getSize();
        if (size / 1024 > 1024 * 5) { //判断大小 单位Kb
            return new Result(ResultCode.FILE_MAX_SIZE_OVERFLOW);
        }
        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        fileName = UUID.randomUUID() + suffixName; // 新文件名

        String fullPath = uploadFolder + fileName;
        String client = StrUtil.removeSuffix(clientUrl, "**");
        FileUtil.writeBytes(file.getBytes(), fullPath);
        String localPath = client + fileName;

        return Result.SUCCESS(localPath);
    }
}


