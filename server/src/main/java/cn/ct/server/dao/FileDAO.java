/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ct.server.dao;

import cn.ct.model.File;
import cn.ct.model.Folder;
import static cn.ct.server.dao.DAO.con;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cn200
 */
public class FileDAO extends DAO {
    public FileDAO() {
        super();
    }
    
    public File getFile(int id) {
        File result;
        String sql = "SELECT * FROM tblFile WHERE id=?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
             
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int infoId = rs.getInt("info");
                String url = rs.getString("url");
                result = new File(id, url, new InfoDAO().getInfo(infoId));
            } else {
                result = new File(-1, null, null);
            }
        } catch(Exception e) {
            e.printStackTrace();
            result = new File(-1, null, null);
        }
        return result;
    }
    
    public List<File> getChildFile(Folder folder) {
        List<File> result = new ArrayList<>();
        String sql = "SELECT id FROM tblFile WHERE folder=?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, folder.getId());
             
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int fileId = rs.getInt("id");
                result.add(getFile(fileId));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
