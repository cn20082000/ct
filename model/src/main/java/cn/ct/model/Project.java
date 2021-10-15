/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ct.model;

import java.io.Serializable;

/**
 *
 * @author cn200
 */
public class Project implements Serializable {
    private int id;
    private User host;
    private Folder rootFolder;
    private Info info;

    public Project() {
        super();
    }

    public Project(int id, User host, Folder rootFolder, Info info) {
        super();
        this.id = id;
        this.host = host;
        this.rootFolder = rootFolder;
        this.info = info;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
    }

    public Folder getRootFolder() {
        return rootFolder;
    }

    public void setRootFolder(Folder rootFolder) {
        this.rootFolder = rootFolder;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }
}
