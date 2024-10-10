package ncu.software.config;

import ncu.software.interceptor.JwtTokenAdminInterceptor;
import ncu.software.interceptor.JwtTokenUserInterceptor;
import ncu.software.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.*;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;


import java.util.List;

/**
 * 配置类，注册web层相关组件
 */
@Configuration
@Slf4j
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;
    @Autowired
    private JwtTokenUserInterceptor jwtTokenUserInterceptor;

    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/employee/login");

        registry.addInterceptor(jwtTokenUserInterceptor)
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/user/register")
                .excludePathPatterns("/user/user/login")
                .excludePathPatterns("/user/shop/status");
    }

    /**
     * 静态资源映射
     *
     * @param registry
     */
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//        registry.addResourceHandler("favicon.ico").addResourceLocations("classpath:/static/");
//    }


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


    /**
     * 扩展springMVC的消息转换器
     *
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("开始扩展消息转换器...");
        //创建一个消息转换器对象
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层使用Jackson将Java对象转为json
        mappingJackson2HttpMessageConverter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转换器对象追加到mvc框架的转换器集合中
        //追加到第0个位置，因为默认的转换器是StringHttpMessageConverter，默认的转换器转换字符串，我们自己转换对象
        converters.add(0, mappingJackson2HttpMessageConverter);
    }
}
