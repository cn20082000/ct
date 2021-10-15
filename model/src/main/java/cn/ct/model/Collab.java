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
public class Collab implements Serializable {
    private int id;
    private User toUser;
    private Project fromProject;
    private boolean status;

    public Collab() {
        super();
    }

    public Collab(int id, User toUser, Project fromProject, boolean status) {
        super();
        this.id = id;
        this.toUser = toUser;
        this.fromProject = fromProject;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public Project getFromProject() {
        return fromProject;
    }

    public void setFromProject(Project fromProject) {
        this.fromProject = fromProject;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
