package com.springboot.security.service;

import com.springboot.security.bean.Menu;
import com.springboot.security.dao.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author N
 * @create 2018/12/14 -- 17:42
 * @email 554197854@qq.com
 */
@Service
public class MenuServiceImpl implements  MenuService {

    @Autowired
    MenuMapper menuMapper;

    public List<Menu> getAllMenu() {
        return menuMapper.getAllMenu();
    }
}
