/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ct.client.frm;

import cn.ct.client.view.MainPanel;
import cn.ct.client.view.ReceiveCollabPanel;
import cn.ct.model.Collab;
import java.awt.BorderLayout;
import java.awt.Dialog;
import javax.swing.JDialog;

/**
 *
 * @author cn200
 */
public class ReceiveCollabDialog extends JDialog {
    
    private MainPanel mainpanel;
    private Collab collab;
    
    public ReceiveCollabDialog(MainPanel mainpanel, Collab collab) {
        super();
        this.mainpanel = mainpanel;
        this.collab = collab;
        init();
    }
    
    private void init() {
        this.setTitle("Receive invitation");
        this.setSize(350, 180);
        this.setLocation(200, 10);
        this.setLocationRelativeTo(mainpanel);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        this.setLayout(new BorderLayout());
    }
    
    public void open() {
        this.add(new ReceiveCollabPanel(this, collab));
        this.setVisible(true);
    }
    
    public void close() {
        this.dispose();
    }
}