package com.springboot.security;

import com.springboot.security.bean.Menu;
import com.springboot.security.dao.UserMapper;
import com.springboot.security.service.MenuService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SecurityApplicationTests {
    @Autowired
    UserMapper userMapper;
    @Autowired
    MenuService menuService;
    @Test
    public void contextLoads() {
        System.out.println(userMapper.selectByUsername("ni"));
        List<Menu> allMenu = menuService.getAllMenu();
        System.out.println(allMenu.size());
    }

}

