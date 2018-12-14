package com.springboot.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author N
 * @create 2018/12/14 -- 1:14
 */
    @Configuration
    public class WebMvcConfig implements WebMvcConfigurer{


        //线程池 暂时未用 研究中
        @Bean
        public ExecutorService executorService() {
            return Executors.newCachedThreadPool();
        }


}
