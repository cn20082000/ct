/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ct.client.frm;

import cn.ct.client.view.ListProjectPanel;
import java.awt.BorderLayout;
import java.awt.Dialog;
import javax.swing.JDialog;

/**
 *
 * @author cn200
 */
public class ListProjectDialog extends JDialog {
    
    private MainFrm mainfrm;
    private boolean isCollab;
    
    public ListProjectDialog(MainFrm mainfrm, boolean isCollab) {
        super();
        this.mainfrm = mainfrm;
        this.isCollab = isCollab;
        init();
    }
    
    private void init() {
        if (isCollab) {
            this.setTitle("Open collab project");
        } else {
            this.setTitle("Open project");
        }
        this.setSize(600, 500);
        this.setLocation(200, 10);
        this.setLocationRelativeTo(mainfrm);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        this.setLayout(new BorderLayout());
    }
    
    public void open() {
        this.add(new ListProjectPanel(this, isCollab));
        this.setVisible(true);
    }
    
    public void close() {
        this.dispose();
    }
}