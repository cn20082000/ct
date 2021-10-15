/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ct.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author cn200
 */
public class Log implements Serializable {
    private int id;
    private Date time;
    private User modUser;
    private Info modInfo;

    public Log() {
        super();
    }

    public Log(int id, Date time, User modUser, Info modInfo) {
        super();
        this.id = id;
        this.time = time;
        this.modUser = modUser;
        this.modInfo = modInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public User getModUser() {
        return modUser;
    }

    public void setModUser(User modUser) {
        this.modUser = modUser;
    }

    public Info getModInfo() {
        return modInfo;
    }

    public void setModInfo(Info modInfo) {
        this.modInfo = modInfo;
    }
}
