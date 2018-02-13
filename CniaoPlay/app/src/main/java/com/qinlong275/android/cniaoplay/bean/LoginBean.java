package com.qinlong275.android.cniaoplay.bean;

/**
 * Created by 秦龙 on 2018/2/12.
 */

public class LoginBean extends BaseEntity {
    private String token;

    private  User user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
