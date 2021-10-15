/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ct.server.frm;

import cn.ct.server.view.MainPanel;
import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 *
 * @author cn200
 */
public class MainFrm extends JFrame {
    public MainFrm() {
        init();
    }
    
    private void init() {
        this.setTitle("Server");
        this.setSize(450, 300);
        this.setLocation(200, 10);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
    }
    
    public void open() {
        this.add(new MainPanel(this));
        this.setVisible(true);
    }
    
    public void close() {
        this.dispose();
    }
}
