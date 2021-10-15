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
public class User implements Serializable {
    private int id;
    private String name;
    private String username;
    private String password;

    public User() {
        super();
    }

    public User(int id, String name, String username, String password) {
        super();
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public User(String name, String username, String password) {
        super();
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public User(int id, String name, String username) {
        super();
        this.id = id;
        this.name = name;
        this.username = username;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
