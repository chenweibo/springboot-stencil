package com.chen.stencil.service;


import cn.hutool.core.io.FileUtil;
import com.chen.stencil.common.response.Result;
import com.chen.stencil.common.response.ResultCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileService {


    @Value("${file.uploadFolder}")
    private String uploadFolder;

    public Result upload(MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            return new Result(ResultCode.FILE_EMPTY);
        }
        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        fileName = UUID.randomUUID() + suffixName; // 新文件名

        String fullPath = uploadFolder + fileName;

        File file1 = FileUtil.writeBytes(file.getBytes(), fullPath);
        String localPath = "/static/upload/" + fileName;
        return Result.SUCCESS(localPath);
    }
}
