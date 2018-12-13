package com.springboot.security.controller;

import com.springboot.security.bean.Msg;
import com.springboot.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author N
 * @create 2018/12/13 -- 16:55
 */
@RestController
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/user/checkUsername")
    public Msg checkUsername(String username){
        String regex="(^[a-zA-Z0-9]{4,16}$)|(^[\u2E80-\u9FFF]{2,5}$)";
        if(!username.matches(regex)){
            return Msg.fail().add("vn_msg", "用户名格式错误！");
        }
        boolean b = userService.checkUsername(username);
        if(b){
            return Msg.success().add("vn_msg", "用户名可用！");

        }else{
            return Msg.fail().add("vn_msg", "用户名不可用！");
        }
    }

    @GetMapping("/user/checkEmail")
    public Msg checkEmail(String email){
        String regex="^([a-z0-9A-Z]+[-|\\\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\\\.)+[a-zA-Z]{2,}$";
        if(!email.matches(regex)){
            return Msg.fail().add("vm_msg", "邮箱格式错误！");
        }
        boolean b = userService.checkEmail(email);
        if(b){
            return Msg.success().add("vm_msg", "邮箱可用！");

        }else{
            return Msg.fail().add("vm_msg", "邮箱不可用！");
        }
    }
}
