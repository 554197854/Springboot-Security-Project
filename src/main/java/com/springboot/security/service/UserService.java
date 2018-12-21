package com.springboot.security.service;
import com.springboot.security.bean.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService{
    public Boolean checkUsername(String username);
    public Boolean checkEmail(String email);
    public void insertUser(User user, HttpServletRequest request);
}