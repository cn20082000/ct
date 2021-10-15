/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ct.client.model;

import cn.ct.model.User;

/**
 *
 * @author cn200
 */
public class UserStatus {
    private User user;
    private boolean isOnline;

    public UserStatus() {}

    public UserStatus(User user, boolean isOnline) {
        this.user = user;
        this.isOnline = isOnline;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }
}
