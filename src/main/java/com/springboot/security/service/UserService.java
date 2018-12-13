package com.springboot.security.service;

import com.springboot.security.bean.User;
import com.springboot.security.bean.UserExample;
import com.springboot.security.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author N
 * @create 2018/12/13 -- 16:39
 */
@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    //检测用户名是否已被注册
    public Boolean checkUsername(String username){
        UserExample example = new UserExample();
        example.createCriteria().andUsernameEqualTo(username).;
        List<User> users = userMapper.selectByExample(example);
        if(users.isEmpty()){
            return true;
        }
        return false;


    }

    //检测邮箱是否已被注册
    public Boolean checkEmail(String email){
        UserExample example = new UserExample();
        example.createCriteria().andEmailEqualTo(email).;
        List<User> users = userMapper.selectByExample(example);
        if(users.isEmpty()){
            return true;
        }
        return false;
    }
}
