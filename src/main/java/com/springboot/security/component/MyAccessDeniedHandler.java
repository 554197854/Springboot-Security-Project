package com.springboot.security.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.security.bean.Msg;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author N
 * @create 2018/12/14 -- 19:26
 * @email 554197854@qq.com
 */
@Component
public class MyAccessDeniedHandler  implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        PrintWriter out = httpServletResponse.getWriter();
        Msg msg=Msg.fail();
        Map map= new HashMap();
        map.put("error","权限不足，请联系管理员!");
        msg.setExtend(map);
        out.write(new ObjectMapper().writeValueAsString(msg));
        out.flush();
        out.close();
    }
}
