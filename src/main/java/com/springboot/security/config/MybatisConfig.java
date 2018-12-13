package com.springboot.security.config;

import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;

/**
 * @author N
 * @create 2018/12/3 -- 18:33
 */
@org.springframework.context.annotation.Configuration
public class MybatisConfig {

    @Bean
    public ConfigurationCustomizer configurationCustomizer(){
        return  new ConfigurationCustomizer(){

            @Override
            public void customize(Configuration configuration) {
                 configuration.setMapUnderscoreToCamelCase(true);
            }
        };
    }
}
