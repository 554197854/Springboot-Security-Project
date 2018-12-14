package com.springboot.security.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class User implements UserDetails, Serializable { //实现UserDetails 配合security的用户验证
    private Integer id;

    @Pattern(regexp="(^[a-zA-Z0-9]{4,16}$)|(^[\\u2E80-\\u9FFF]{2,5}$)",message="用户名格式错误！")
    private String username;
    @Length(min = 6,max = 16)
    private String password;
    @Email(message = "邮箱格式错误！")
    private String email;

    private Timestamp createTime;

    private Integer active;
    @Pattern(regexp = "1[3|4|5|7|8][0-9]\\d{8}",message = "用户手机号输入错误！")
    private String phone;

    private String icon;

    private List<Role> roles;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    @JsonIgnore//序列化时忽略注解
    @Override
    public boolean isAccountNonExpired() { //是否没有用户存活时间
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() { //没有锁住账户
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() { //没有有过期
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() { //是否激活
        if(this.active==0){
            return false;
        }else{
            return true;
        }

    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }
}