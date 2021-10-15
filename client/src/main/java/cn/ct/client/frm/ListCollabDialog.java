/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ct.client.frm;

import cn.ct.client.view.ListCollabPanel;
import java.awt.BorderLayout;
import java.awt.Dialog;
import javax.swing.JDialog;

/**
 *
 * @author cn200
 */
public class ListCollabDialog extends JDialog {
    
    private MainFrm mainfrm;
    
    public ListCollabDialog(MainFrm mainfrm) {
        super();
        this.mainfrm = mainfrm;
        init();
    }
    
    private void init() {
        this.setTitle("Collab invitation");
        this.setSize(600, 500);
        this.setLocation(200, 10);
        this.setLocationRelativeTo(mainfrm);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        this.setLayout(new BorderLayout());
    }
    
    public void open() {
        this.add(new ListCollabPanel(this));
        this.setVisible(true);
    }
    
    public void close() {
        this.dispose();
    }
}