/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ct.client.view;

import cn.ct.client.App;
import cn.ct.client.frm.ReceiveCollabDialog;
import cn.ct.client.util.SomeFunc;
import cn.ct.model.Collab;
import cn.ct.model.ObjectWrapper;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author cn200
 */
public class ReceiveCollabPanel extends javax.swing.JPanel {
    
    private ReceiveCollabDialog parent;
    private Collab collab;

    /**
     * Creates new form ReceiveCollabPanel
     */
    public ReceiveCollabPanel(ReceiveCollabDialog parent, Collab collab) {
        this.parent = parent;
        this.collab = collab;
        initComponents();
        
        updateView();
    }
    
    private void updateView() {
        tvName.setText(collab.getFromProject().getInfo().getName());
        tvHost.setText(collab.getFromProject().getHost().getName());
        tvTime.setText(SomeFunc.formatDate(collab.getFromProject().getInfo().getCreateTime()));
    }
    
    private void acceptCollabResult(ObjectWrapper data) {
        if (data.getMessage() == ObjectWrapper.MES_SUCCESS) {
//            JFileChooser f = new JFileChooser();
//            f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//            while (true) {
//                f.showDialog(this, "Save " + collab.getFromProject().getInfo().getName());
//                if (!new java.io.File(f.getSelectedFile().getAbsoluteFile() 
//                    + "\\" + collab.getFromProject().getInfo().getName()).exists()) {
//                    JOptionPane.showMessageDialog(this, "Folder is exist..", 
//                            "Message", JOptionPane.ERROR_MESSAGE);
//                    continue;
//                }
//                break;
//            }
            parent.close();
        } else {
            JOptionPane.showMessageDialog(this, "Error while accept invitation.", 
                    "Message", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void rejectCollabResult(ObjectWrapper data) {
        if (data.getMessage() == ObjectWrapper.MES_SUCCESS) {
            parent.close();
        } else {
            JOptionPane.showMessageDialog(this, "Error while reject invitation.", 
                    "Message", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        tvName = new javax.swing.JLabel();
        tvHost = new javax.swing.JLabel();
        tvTime = new javax.swing.JLabel();
        btnAccept = new javax.swing.JButton();
        btnReject = new javax.swing.JButton();

        jLabel1.setText("You have received an invitation to join the project.");

        jLabel2.setText("Project name:");

        jLabel3.setText("Project host:");

        jLabel4.setText("Create time:");

        tvName.setText("jLabel5");

        tvHost.setText("jLabel6");

        tvTime.setText("jLabel7");

        btnAccept.setText("Accept");
        btnAccept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcceptActionPerformed(evt);
            }
        });

        btnReject.setText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tvName))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tvHost))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tvTime)))
                .addContainerGap(74, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnReject)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAccept)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tvName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(tvHost))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(tvTime))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAccept)
                    .addComponent(btnReject))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAcceptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcceptActionPerformed
        // TODO add your handling code here:
        collab.setStatus(true);
        App.ctrl.sendData(new ObjectWrapper(ObjectWrapper.GET_ACCEPT_COLLAB, collab))
                .register(ObjectWrapper.REPLY_ACCEPT_COLLAB, data -> {
            acceptCollabResult(data);
        });
    }//GEN-LAST:event_btnAcceptActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        App.ctrl.sendData(new ObjectWrapper(ObjectWrapper.GET_REJECT_COLLAB, collab))
                .register(ObjectWrapper.REPLY_REJECT_COLLAB, data -> {
            rejectCollabResult(data);
        });
    }//GEN-LAST:event_btnRejectActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAccept;
    private javax.swing.JButton btnReject;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel tvHost;
    private javax.swing.JLabel tvName;
    private javax.swing.JLabel tvTime;
    // End of variables declaration//GEN-END:variables
}
