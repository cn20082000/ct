/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ct.client;

import cn.ct.client.frm.MainFrm;
import javax.swing.UIManager;

/**
 *
 * @author cn200
 */
public class Main {
    public static void main(String args[]) {
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        MainFrm fr = new MainFrm();
        fr.open();
    }
}
