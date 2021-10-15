/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ct.server.dao;

import cn.ct.model.Info;
import static cn.ct.server.dao.DAO.con;
import cn.ct.server.util.SomeFunc;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 *
 * @author cn200
 */
public class InfoDAO extends DAO {
     
    public InfoDAO() {
        super();
    }
    
    public Info getInfo(int id) {
        Info result;
        String sql = "SELECT * FROM tblInfo WHERE id=?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
             
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                Date createTime = SomeFunc.convert(rs, "createTime");
                String version = rs.getString("version");
                result = new Info(id, name, createTime, version);
            } else {
                result = new Info(-1, null, null, null);
            }
        } catch(Exception e) {
            e.printStackTrace();
            result = new Info(-1, null, null, null);
        }
        return result;
    }
    
    public Info createInfo(Info info) {
        int id = -1;
        String sql = "INSERT INTO tblInfo (name, createTime, version) VALUES (?, ?, ?)";
        try{
            PreparedStatement ps = con.prepareStatement(sql, new String[] {"id"});
            ps.setString(1, info.getName());
            ps.setDate(2, SomeFunc.convert(Calendar.getInstance().getTime()));
            ps.setString(3, UUID.randomUUID().toString());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch(SQLException e){
            e.printStackTrace();
            return new Info(-1, null, null, null);
        }
        return getInfo(id);
    }
}
