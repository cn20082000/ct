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
public class File implements Serializable {
    private int id;
    private String url;
    private Info info;

    public File() {
        super();
    }

    public File(int id, String url, Info info) {
        super();
        this.id = id;
        this.url = url;
        this.info = info;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }
}
