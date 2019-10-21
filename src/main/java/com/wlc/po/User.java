package com.wlc.po;

/**
 * describe:用户登录实体类
 *
 * @author 王立朝
 * @date 2019/10/21
 */
public class User {
    private String password;
    private String name;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User() {
    }

    public User(String name, String password) {
        this.password = password;
        this.name = name;
    }
}
