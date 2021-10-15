/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ct.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author cn200
 */
public class Folder implements Serializable {
    private int id;
    private List<Folder> folders;
    private List<File> files;
    private Info info;

    public Folder() {
        super();
    }

    public Folder(int id, List<Folder> folders, List<File> files, Info info) {
        super();
        this.id = id;
        this.folders = folders;
        this.files = files;
        this.info = info;
    }
    
    public void sort() {
        Collections.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o1.getInfo().getName().compareTo(o2.getInfo().getName());
            }
        });
        Collections.sort(folders, new Comparator<Folder>() {
            @Override
            public int compare(Folder o1, Folder o2) {
                return o1.getInfo().getName().compareTo(o2.getInfo().getName());
            }
        });
        for (Folder folder : folders) {
            folder.sort();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Folder> getFolders() {
        return folders;
    }

    public void setFolders(List<Folder> folders) {
        this.folders = folders;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }
}
