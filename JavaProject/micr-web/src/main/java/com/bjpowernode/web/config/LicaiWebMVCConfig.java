package com.bjpowernode.web.config;

import com.bjpowernode.web.handler.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LicaiWebMVCConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加拦截器对象
        String addPath[] = { "/loan/**"};
        String excludePath [] =
                  {   "/loan/checkPhone",
                      "/send/sms",
                      "/loan/loan",
                      "/loan/loanInfo",
                      "/loan/login",
                      "/loan/logout",
                      "/loan/page/login",
                      "/loan/page/register",
                      "/loan/register"
                  };
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns(addPath)
                .excludePathPatterns(excludePath);
    }
}
