package ncu.software.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Slf4j
//@Configuration
//@EnableWebMvc implements WebMvcConfigurer
public class SwaggerConfig   {
    /**
     * 配置静态资源映射
     *
     * @param registry
     */
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        log.info("静态资源映射");
//        registry.addResourceHandler("/doc.html").addResourceLocations(
//                "classpath:/META-INF/resources/");
//        registry.addResourceHandler("swagger-ui.html").addResourceLocations(
//                "classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**").addResourceLocations(
//                "classpath:/META-INF/resources/webjars/");
//        WebMvcConfigurer.super.addResourceHandlers(registry);
//    }

    /**
     * 通过knife4j生成接口文档
     *
     * @return
     */
//    @Bean
//    public OpenAPI springShopOpenApi() {
//        log.info("swagger配置成功");
//        return new OpenAPI()
//                // 接口文档标题
//                .info(new Info().title("光明牛奶订购系统")
//                        // 接口文档简介
//                        .description("这是企业课程大作业")
//                        // 接口文档版本
//                        .version("1.0版本")
//                        // 开发者联系方式
//                        .contact(new Contact().name("hw")
//                                .email("2890716703@qq.com")));
//    }
}