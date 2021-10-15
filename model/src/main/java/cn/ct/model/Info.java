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
public class Info implements Serializable {
    private int id;
    private String name;
    private Date createTime;
    private String version;

    public Info() {
        super();
    }

    public Info(int id, String name, Date createTime, String version) {
        super();
        this.id = id;
        this.name = name;
        this.createTime = createTime;
        this.version = version;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
