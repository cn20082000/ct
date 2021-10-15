/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ct.client.frm;

import cn.ct.client.view.SigninPanel;
import java.awt.BorderLayout;
import java.awt.Dialog;
import javax.swing.JDialog;

/**
 *
 * @author cn200
 */
public class SigninDialog extends JDialog {
    
    private MainFrm mainfrm;
    
    public SigninDialog(MainFrm mainfrm) {
        super();
        this.mainfrm = mainfrm;
        init();
    }
    
    private void init() {
        this.setTitle("Sign in");
        this.setSize(300, 150);
        this.setLocation(200, 10);
        this.setLocationRelativeTo(mainfrm);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        this.setLayout(new BorderLayout());
    }
    
    public void open() {
        this.add(new SigninPanel(this));
        this.setVisible(true);
    }
    
    public void close() {
        this.dispose();
    }
    
    public void signInSuccess() {
        mainfrm.signInSuccess();
    }
}
