/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ct.server.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author cn200
 */
public class SomeFunc {
    public static Date convert(ResultSet rs, String column) throws SQLException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(rs.getTime(column));
        return new Date(cal.getTime().getTime() + rs.getDate(column).getTime() + 25200000);
    }
    
    public static java.sql.Date convert(Date date) {
        return new java.sql.Date(date.getTime());
    }
}
