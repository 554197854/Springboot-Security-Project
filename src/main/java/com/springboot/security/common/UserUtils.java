package com.springboot.security.common;

import com.springboot.security.bean.User;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author N
 * @create 2018/12/14 -- 2:50
 */

public class UserUtils {
    public static User getUserDetils(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();//返回的是UserDetails接口的实现类，即用户身份信息
    }
}
