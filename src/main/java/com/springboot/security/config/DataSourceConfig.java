package com.springboot.security.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author N
 * @create 2018/12/3 -- 17:36
 */
@Configuration
@MapperScan(basePackages = {"com.springboot.security.dao"},sqlSessionFactoryRef = "mySqlSessionFactory")
public class DataSourceConfig {

    @Primary
    @Bean(name="myDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druid(){
        return new DruidDataSource();
    }

    //配置Druid后台监听器
    //1.配置一个servlet
    @Bean
    public ServletRegistrationBean statViewServlet(){
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
        Map init = new HashMap();
        init.put("loginUsername","admin");
        init.put("loginPassword","123456");
        bean.setInitParameters(init);
        return bean;
    }
    //2.配置一个filter
    @Bean
    public FilterRegistrationBean webStatFilter(){
        FilterRegistrationBean bean = new FilterRegistrationBean(new WebStatFilter());
        Map init = new HashMap();
        init.put("exclusions","*.js,*.css,/druid/*");
        bean.setInitParameters(init);
        bean.setUrlPatterns(Arrays.asList("/*"));
        return bean;
    }

    //配置一个SqlSessionFactory
    @Primary
    @Bean(name = "mySqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("myDataSource") DataSource druid) throws Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(druid);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/*.xml"));
        return sqlSessionFactoryBean.getObject();

    }



}
