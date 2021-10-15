/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ct.server.dao;

import cn.ct.model.Log;
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
public class ProjectDAO extends DAO {
    public ProjectDAO() {
        super();
    }
    
    public Project getProject(int id) {
        Project result;
        String sql = "SELECT * FROM tblProject WHERE id=?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
             
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int hostId = rs.getInt("host");
                int rootFolderId = rs.getInt("rootFolder");
                int infoId = rs.getInt("info");
                result = new Project(id, new UserDAO().getUser(hostId), 
                        new FolderDAO().getFolder(rootFolderId), new InfoDAO().getInfo(infoId));
            } else {
                result = new Project(-1, null, null, null);
            }
        } catch(Exception e) {
            e.printStackTrace();
            result = new Project(-1, null, null, null);
        }
        return result;
    }
    
    public List<Project> getProject(User user) {
        List<Project> result = new ArrayList<>();
        String sql = "SELECT id FROM tblProject WHERE host=?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, user.getId());
             
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int projectId = rs.getInt("id");
                result.add(getProject(projectId));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public Project createProject(Project project) {
        project.setInfo(new InfoDAO().createInfo(project.getInfo()));
        project.setRootFolder(new FolderDAO().createFolder(project.getRootFolder(), project.getHost()));
        int id = -1;
        String sql = "INSERT INTO tblProject (host, rootFolder, info) VALUES (?,?,?)";
        try{
            PreparedStatement ps = con.prepareStatement(sql, new String[] {"id"});
            ps.setInt(1, project.getHost().getId());
            ps.setInt(2, project.getRootFolder().getId());
            ps.setInt(3, project.getInfo().getId());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch(SQLException e){
            e.printStackTrace();
            return new Project(-1, null, null, null);
        }
        new LogDAO().newLog(new Log(0, null, project.getHost(), project.getInfo()));
        return getProject(id);
    }
}
