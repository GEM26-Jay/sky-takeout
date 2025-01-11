package com.sky.controller.admin;


import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.LocalFileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * 通用接口
 */
@RestController
@Slf4j
@RequestMapping("/admin/common")
@Api(tags="通用接口")
public class CommonController {

    @Autowired
    private LocalFileUtil localFileUtil;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){

        try {
            String originalFileName = file.getOriginalFilename();
            log.info("文件上传{}", originalFileName);
            // 截取原始文件名的后缀
            String extention = originalFileName.substring(originalFileName.lastIndexOf("."));
            String objectName = UUID.randomUUID().toString() + extention;
            String filePath = localFileUtil.upload(file.getBytes(), objectName);
            return Result.success(filePath);
        } catch (IOException e) {
            log.error("文件上传失败{}", e.getMessage());
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
