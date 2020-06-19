package cn.sicnu.ming.config;

/**
 * @author frank ming
 * @createTime 20200618 11:41
 * @description 静态资源配置类
 */

import cn.sicnu.ming.config.converter.ConverterHand;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("").setViewName("index");
    }

    @Bean
    public ConverterHand hand(){
        return new ConverterHand();
    }

}