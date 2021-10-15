/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ct.server.dao;

import cn.ct.model.Collab;
import cn.ct.model.Project;
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
public class CollabDAO extends DAO {
    public CollabDAO() {
        super();
    }
    
    public Collab getCollab(int id) {
        Collab result;
        String sql = "SELECT * FROM tblCollab WHERE id=?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int toUser = rs.getInt("toUser");
                int fromProject = rs.getInt("fromProject");
                boolean status = rs.getBoolean("status");
                result = new Collab(id, new UserDAO().getUser(toUser), 
                        new ProjectDAO().getProject(fromProject), status);
            } else {
                result =  new Collab(-1, null, null, false);
            }
        } catch(SQLException e){
            e.printStackTrace();
            result =  new Collab(-1, null, null, false);
        }
        return result;
    }
    
    public List<Collab> getCollab(Project project) {
        List<Collab> result = new ArrayList<>();
        String sql = "SELECT id FROM tblCollab WHERE fromProject=? AND status=true";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, project.getId());
             
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int collabId = rs.getInt("id");
                result.add(getCollab(collabId));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public List<Collab> getCollab(User user, boolean status) {
        List<Collab> result = new ArrayList<>();
        String sql = "SELECT id FROM tblCollab WHERE toUser=? AND status=?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, user.getId());
            ps.setBoolean(2, status);
             
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int collabId = rs.getInt("id");
                result.add(getCollab(collabId));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public Collab createCollab(Collab collab) {
        collab.setToUser(new UserDAO().getUser(collab.getToUser().getUsername()));
        if (collab.getToUser().getId() < 0) {
            return new Collab(-1, null, null, false);
        }
        int id = -1;
        String sql = "INSERT INTO tblCollab (toUser, fromProject, status) VALUES (?,?,?)";
        try{
            PreparedStatement ps = con.prepareStatement(sql, new String[] {"id"});
            ps.setInt(1, collab.getToUser().getId());
            ps.setInt(2, collab.getFromProject().getId());
            ps.setBoolean(3, false);

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch(SQLException e){
            e.printStackTrace();
            return new Collab(-1, null, null, false);
        }
        
        return getCollab(id);
    }
    
    public Collab updateCollab(Collab collab) {
        String sql = "UPDATE tblCollab SET status=? WHERE id=?";
        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setBoolean(1, collab.isStatus());
            ps.setInt(2, collab.getId());

            ps.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
            return new Collab(-1, null, null, false);
        }
        
        return getCollab(collab.getId());
    }
    
    public boolean deleteCollab(Collab collab) {
        String sql = "DELETE FROM tblCollab WHERE id=?";
        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, collab.getId());

            ps.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
}
