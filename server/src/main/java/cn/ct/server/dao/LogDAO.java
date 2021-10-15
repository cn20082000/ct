/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ct.server.dao;

import cn.ct.model.Log;
import cn.ct.server.util.SomeFunc;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

/**
 *
 * @author cn200
 */
public class LogDAO extends DAO {
    public LogDAO() {
        super();
    }
    
    public void newLog(Log log) {
        String sql = "INSERT INTO tblLog (time, modUser, modInfo) VALUES (?, ?, ?)";
        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDate(1, SomeFunc.convert(Calendar.getInstance().getTime()));
            ps.setInt(2, log.getModUser().getId());
            ps.setInt(3, log.getModInfo().getId());

            ps.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
}
