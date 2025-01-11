package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.LocalFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类，用于创建文件上传配置
 */
@Configuration
@Slf4j
public class OSSConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public LocalFileUtil getLocalFileUtil(AliOssProperties aliOssProperties){
        log.info("创建文件上传工具类: {}", aliOssProperties);
        LocalFileUtil localFileUtil = new LocalFileUtil();
        BeanUtils.copyProperties(aliOssProperties, localFileUtil);
        return localFileUtil;
    }

}
