package com.springboot.security.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.security.bean.Msg;
import com.springboot.security.bean.User;
import com.springboot.security.common.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author N
 * @create 2018/12/18 -- 23:28
 * @email 554197854@qq.com
 */
@Component
public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    //或者直接实现AuthenticationSuccessHandler的方式来重写onAuthenticationSuccess方法
    //日志
    private final static Logger logger = LoggerFactory.getLogger(MyAuthenticationSuccessHandler.class);



    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        User user = UserUtils.getUserDetils();
        request.getSession().setAttribute("username",user.getUsername());
        request.getSession().setAttribute("userId",user.getId());
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        if(iterator.hasNext()){
            GrantedAuthority next = iterator.next();
            if("ROLE_ACTIVE".equals(next.getAuthority())){
                getRedirectStrategy().sendRedirect(request,response,"/active");
                return;
            };

        }else {
            getRedirectStrategy().sendRedirect(request, response, "/index");
            return;
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
/**
 * 以下有其他两种版本的登录成功后的逻辑实现，根据需求使用
 *
 * **/
//    @Override JSON数据返回版本
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
//        response.setContentType("application/json;charset=utf-8");
//        Msg msg = Msg.success();
//        Map map =new HashMap();
//        map.put("登录成功!", UserUtils.getUserDetils());
//        msg.setExtend(map);
//        ObjectMapper om = new ObjectMapper();
//        PrintWriter out = response.getWriter();
//        out.write(om.writeValueAsString(msg));
//        out.flush();
//        out.close();
//    }



//跳转回登录之前请求页面版本
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
//        RequestCache requestCache = new HttpSessionRequestCache();
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        User user = null;
//        try {
//
//            user = userService.getUserByMail(userDetails.getUsername());
//            request.getSession().setAttribute("username",user.getUsername());
//            request.getSession().setAttribute("userId",user.getId());
//            logService.addLog("myUserDetailsService.loadUserByUsername","认证模块","低",
//                    "登录","成功","邮箱为" + user.getMail() + "的用户登录成功，登录IP为" + request.getRemoteAddr(),user.getId());
//        }catch (Exception e){
//            logService.addLog("MyAuthenticationSuccessHandler.onAuthenticationSuccess","认证模块","高","登录","失败","保存session失败,mail为" + user.getMail(),user.getId());
//        }
//        String url = null;
       /**当在ExceptionTranslationFilter中拦截时，
        * 会调用HttpSessionRequestCache保存原始的请求信息。
        * 在UsernamePasswordAuthenticationFilter过滤器登录成功后，
        * 会调用SavedRequestAwareAuthenticationSuccessHandler
        * 所以我们可以通过以下方法取出登录之前的求请url，如果没有再转发重定向到指定页面
**/
       //        SavedRequest savedRequest = requestCache.getRequest(request,response);
//        if(savedRequest != null){
//            url = savedRequest.getRedirectUrl();
//        }
//        if(url == null){
//            getRedirectStrategy().sendRedirect(request,response,"/admin/adminIndex.htm");
//        }
//        super.onAuthenticationSuccess(request, response, authentication);
//    }
}