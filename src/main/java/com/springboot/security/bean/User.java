package com.springboot.security.bean;

import org.hibernate.validator.constraints.Length;


import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;
import java.util.Date;

public class User {
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

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