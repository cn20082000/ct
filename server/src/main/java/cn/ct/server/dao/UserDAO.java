/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ct.server.dao;

import cn.ct.model.User;
import cn.ct.server.util.Security;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author cn200
 */
public class UserDAO extends DAO {
     
    public UserDAO() {
        super();
    }
     
    public User signIn(User user) {
        String sql = "SELECT id, name FROM tblUser WHERE username = ? AND password = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, Security.hashPassword(user.getPassword()));
             
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
            } else {
                user.setId(-1);
            }
        } catch(Exception e) {
            e.printStackTrace();
            user.setId(-1);
        }
        return user;
    }
    
    public User getUser(int id) {
        User result;
        String sql = "SELECT id, name, username FROM tblUser WHERE id=?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
             
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                String name = rs.getString("name");
                String username = rs.getString("username");
                result = new User(id, name, username, null);
            } else {
                result = new User(-1, null, null, null);
            }
        } catch(Exception e) {
            e.printStackTrace();
            result = new User(-1, null, null, null);
        }
        return result;
    }
    
    public User getUser(String username) {
        User result;
        String sql = "SELECT id, name FROM tblUser WHERE username=?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
             
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                result = new User(id, name, username, null);
            } else {
                result = new User(-1, null, null, null);
            }
        } catch(Exception e) {
            e.printStackTrace();
            result = new User(-1, null, null, null);
        }
        return result;
    }
    
    public boolean signUp(User user) {
        String sql = "INSERT INTO tblUser (name, username, password) VALUES (?, ?, ?)";
        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user.getName());
            ps.setString(2, user.getUsername());
            ps.setString(3, Security.hashPassword(user.getPassword()));
 
            ps.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public boolean update(User user) {
        String sql = "UPDATE tblUser SET name=?, username=?, password=? WHERE id=?";
        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user.getName());
            ps.setString(2, user.getUsername());
            ps.setString(3, Security.hashPassword(user.getPassword()));
            ps.setInt(4, user.getId());
 
            ps.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}