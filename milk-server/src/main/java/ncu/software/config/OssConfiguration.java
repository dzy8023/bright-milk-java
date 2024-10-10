package ncu.software.config;

import ncu.software.properties.MinOssProperties;
import ncu.software.utils.MinOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类，用于创建AliOssUtil对象
 */
@Configuration
@Slf4j
public class OssConfiguration {
    @Bean
    @ConditionalOnMissingBean
    // 只有当系统中没有该对象时，才会创建该对象
    public MinOssUtil minOssUtil(MinOssProperties minOssProperties){
        log.info("开始创建MinIO文件上传工具类对象：{}",minOssProperties);
        return new MinOssUtil(minOssProperties.getEndpoint(),minOssProperties.getAccessKeyId(),
                minOssProperties.getAccessKeySecret(),minOssProperties.getBucketName());
    }
}
