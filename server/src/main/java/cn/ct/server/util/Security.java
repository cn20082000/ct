/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ct.server.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author cn200
 */
public class Security {
    private static final String salt = "cQpED7TNpS6lQHgGbVRf";
    
    public static String hashPassword(String password) {
        StringBuffer buffer = new StringBuffer(password);
        buffer.insert(password.length() % 8, salt);
        return DigestUtils.sha256Hex(buffer.toString());
    }
}
