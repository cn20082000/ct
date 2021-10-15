/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ct.server.dao;

import cn.ct.model.Folder;
import cn.ct.model.Log;
import cn.ct.model.User;
import static cn.ct.server.dao.DAO.con;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cn200
 */
public class FolderDAO extends DAO {
    public FolderDAO() {
        super();
    }
    
    public Folder getFolder(int id) {
        Folder result;
        String sql = "SELECT * FROM tblFolder WHERE id=?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
             
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int infoId = rs.getInt("info");
                result = new Folder(id, null, null, new InfoDAO().getInfo(infoId));
                result.setFolders(getChildFolder(result));
                result.setFiles(new FileDAO().getChildFile(result));
            } else {
                result = new Folder(-1, null, null, null);
            }
        } catch(Exception e) {
            e.printStackTrace();
            result = new Folder(-1, null, null, null);
        }
        return result;
    }
    
    public List<Folder> getChildFolder(Folder folder) {
        List<Folder> result = new ArrayList<>();
        String sql = "SELECT id FROM tblFolder WHERE dadFolder=?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, folder.getId());
             
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int folderId = rs.getInt("id");
                result.add(getFolder(folderId));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public Folder createFolder(Folder folder, User user) {
        folder.setInfo(new InfoDAO().createInfo(folder.getInfo()));
        int id = -1;
        String sql = "INSERT INTO tblFolder (info) VALUES (?)";
        try{
            PreparedStatement ps = con.prepareStatement(sql, new String[] {"id"});
            ps.setInt(1, folder.getInfo().getId());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch(SQLException e){
            e.printStackTrace();
            return new Folder(-1, null, null, null);
        }
        new LogDAO().newLog(new Log(0, null, user, folder.getInfo()));
        return getFolder(id);
    }
}
