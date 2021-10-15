/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ct.server.dao;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author cn200
 */
public class DAO {
    public static Connection con;
     
    public DAO(){
        if(con == null){
            String dbUrl = "jdbc:mysql://localhost:3306/ct?autoReconnect=true&useSSL=false&allowMultiQueries=true";
            String dbClass = "com.mysql.cj.jdbc.Driver";
 
            try {
                Class.forName(dbClass);
                con = DriverManager.getConnection (dbUrl, "root", "132456");
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}