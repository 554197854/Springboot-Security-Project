package com.springboot.security.service;

import com.springboot.security.bean.User;
import com.springboot.security.bean.UserExample;
import com.springboot.security.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author N
 * @create 2018/12/13 -- 16:39
 */
@EnableTransactionManagement(proxyTargetClass = true)
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    //检测用户名是否已被注册
    @Transactional(readOnly = true)
    public Boolean checkUsername(String username){
        UserExample example = new UserExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<User> users = userMapper.selectByExample(example);
        if(users.isEmpty()){
            return true;
        }
        return false;


    }

    //检测邮箱是否已被注册
    @Transactional(readOnly = true)
    public Boolean checkEmail(String email){
        UserExample example = new UserExample();
        example.createCriteria().andEmailEqualTo(email);
        List<User> users = userMapper.selectByExample(example);
        if(users.isEmpty()){
            return true;
        }
        return false;
    }

    //注册用户
    public void insertUser(User user, HttpServletRequest request) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();//security官方推荐加密方式
        String encode = encoder.encode(user.getPassword());
        user.setPassword(encode);
        if(user.getIcon().isEmpty()){
            String realPath = request.getServletContext().getRealPath("");

            user.setIcon(realPath+"resources/static/images/default_icon.png");
        }
        user.setActive(0);//设置未激活状态
        userMapper.insert(user);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectByUsername(username);
        if(user==null){
            throw  new UsernameNotFoundException("用户名错误！");
        }
        return user;
    }
}
