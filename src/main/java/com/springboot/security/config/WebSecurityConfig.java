package com.springboot.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.security.bean.Msg;
import com.springboot.security.common.UserUtils;
import com.springboot.security.component.MyAccessDecisionManager;
import com.springboot.security.component.MyAccessDeniedHandler;
import com.springboot.security.component.MySecurityMetadataSource;
import com.springboot.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author N
 * @create 2018/12/14 -- 13:36
 */

//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser("admin").password("admin").roles("USER");
//    }
//}

//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser("admin").password("admin").roles("USER");
//    }
//}
/**
 *上述两种配置方式 一种是protect 一种是@Autowired方式
 * 1.@Autowired注入的是全局的身份认证器，作用域可以跨越多个WebSecurityConfigurerAdapter，以及影响到基于Method的安全控制；
 * 2.protected configure()的方式则类似于一个匿名内部类，它的作用域局限于一个WebSecurityConfigurerAdapter内部。
 *
 *
* */

@Configuration
@EnableWebSecurity //会多import一些类
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
    //重写三个configure相关的配置

    @Autowired
    UserService userService;

    @Autowired
    MyAccessDecisionManager myAccessDecisionManager;

    @Autowired
    MySecurityMetadataSource mySecurityMetadataSource;

    @Autowired
    MyAccessDeniedHandler myAccessDeniedHandler;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {//自定义验证配置
        auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());//验证基于数据库自定义UserDetailsService，
        // 也就是对我们自己写的实现了对应接口的UserService和User这个类和同一加密方法进行验证

        //auth.authenticationProvider(authenticationProvider()); 上述一句代码是这句代码+下面的@Bean的简写
    }


    //@Bean
    //public DaoAuthenticationProvider authenticationProvider() {
    //    DaoAuthenticationProvider authProvider
    //      = new DaoAuthenticationProvider();
    //    authProvider.setUserDetailsService(userDetailsService);
    //    authProvider.setPasswordEncoder(encoder());
    //    return authProvider;
    //}
    //


    @Override
    public void configure(WebSecurity web) throws Exception { //配置一些可忽略拦截的静态资源
        web.ignoring()
                .antMatchers("/assets/**", "/login.html","/register.html");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception { //对http的配置，是配置中最复杂的一部分，因为本项目模块涉及到细粒度的权限资源访问管理。即要与数据库ROLE权限相关联
        System.out.println("123");
        http.authorizeRequests()
                .antMatchers("/druid/**","/register","/checkUsername","/checkEmail").permitAll()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {

                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setAccessDecisionManager(myAccessDecisionManager);//定义用户和权限的关系
                        o.setSecurityMetadataSource(mySecurityMetadataSource);//定义权限和资源的关系
                        return o;
                    }
                })
        /**
         * Security有一个springSecurityFilterChain的过滤器链条，
         * withObjectPostProcessor则是可以在这个链条执行过程中自己配置其中的某些过滤拦截器
         * 而FilterSecurityInterceptor正是与我们需要细粒度控制用户权限资源相关，它从SecurityContextHolder中获取Authentication对象，
         * 然后比对用户和资源所需的权限（数据库配置类）。User对象可以通过Authentication对象直接获得，
         * 而对应的资源用户权限则需要引入两个类：SecurityMetadataSource，AccessDecisionManager，来进行匹配。
         *
         */
                .and()
                .formLogin().loginPage("/login_p").loginProcessingUrl("/login")
                .usernameParameter("username").passwordParameter("password")
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest req,
                                                        HttpServletResponse resp,
                                                        AuthenticationException e) throws IOException {
                        resp.setContentType("application/json;charset=utf-8");
                        Msg msg = Msg.fail();
                        Map map =new HashMap();
                        if (e instanceof BadCredentialsException ||
                                e instanceof UsernameNotFoundException) {
                        } else if (e instanceof LockedException) {
                           map.put("loginError","账户被锁定，请联系管理员!");
                        } else if (e instanceof CredentialsExpiredException) {
                            map.put("loginError","密码过期，请联系管理员!");
                        } else if (e instanceof AccountExpiredException) {
                            map.put("loginError","账户过期，请联系管理员!");
                        } else if (e instanceof DisabledException) {
                            map.put("loginError","账户被禁用，请联系管理员!");
                        } else {
                            map.put("loginError","登录失败!");
                        }
                        msg.setExtend(map);
                        resp.setStatus(401);
                        ObjectMapper om = new ObjectMapper();
                        PrintWriter out = resp.getWriter();
                        out.write(om.writeValueAsString(msg));
                        out.flush();
                        out.close();
                    }
                })
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest req,
                                                        HttpServletResponse resp,
                                                        Authentication auth) throws IOException {
                        resp.setContentType("application/json;charset=utf-8");
                        Msg msg = Msg.success();
                        Map map =new HashMap();
                        map.put("登录成功!", UserUtils.getUserDetils());
                        msg.setExtend(map);
                        ObjectMapper om = new ObjectMapper();
                        PrintWriter out = resp.getWriter();
                        out.write(om.writeValueAsString(msg));
                        out.flush();
                        out.close();
                    }
                })
                .permitAll()
                .and()
                .logout().permitAll()
                .and().csrf().disable()
                .exceptionHandling().accessDeniedHandler(myAccessDeniedHandler);

    }
}
