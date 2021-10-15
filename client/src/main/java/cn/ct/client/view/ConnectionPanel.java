/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ct.client.view;

import cn.ct.client.App;
import cn.ct.client.control.ClientCtrl;
import cn.ct.client.frm.ConnectionDialog;
import cn.ct.model.IPAddress;
import javax.swing.JOptionPane;

/**
 *
 * @author cn200
 */
public class ConnectionPanel extends javax.swing.JPanel {

    private ConnectionDialog parent;
    /**
     * Creates new form ConnectionPanel
     */
    public ConnectionPanel(ConnectionDialog parent) {
        this.parent = parent;
        initComponents();
        
        updateView();
    }
    
    private void updateView() {
        if(App.ctrl != null) {
            edtHost.setText(App.ctrl.getServerAddress().getHost());
            edtPort.setText(String.valueOf(App.ctrl.getServerAddress().getPort()));
            edtHost.setEditable(false);
            edtPort.setEditable(false);
            btnDisconnect.setEnabled(true);
            btnConnect.setEnabled(false);
        } else {
            edtHost.setEditable(true);
            edtPort.setEditable(true);
            btnDisconnect.setEnabled(false);
            btnConnect.setEnabled(true);
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
        edtHost = new javax.swing.JTextField();
        edtPort = new javax.swing.JTextField();
        btnConnect = new javax.swing.JButton();
        btnDisconnect = new javax.swing.JButton();

        jLabel1.setText("Server host:");

        jLabel2.setText("Server port:");

        btnConnect.setText("Connect");
        btnConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectActionPerformed(evt);
            }
        });

        btnDisconnect.setText("Disconnect");
        btnDisconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisconnectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(edtHost)
                            .addComponent(edtPort)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 133, Short.MAX_VALUE)
                        .addComponent(btnDisconnect)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnConnect)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(edtHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(edtPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnConnect)
                    .addComponent(btnDisconnect))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnectActionPerformed
        // TODO add your handling code here:
        if (!edtHost.getText().isEmpty() && (edtHost.getText().trim().length() > 0) &&
                !edtPort.getText().isEmpty() && (edtPort.getText().trim().length() > 0)) {
            int port = Integer.parseInt(edtPort.getText().trim());
            App.ctrl = new ClientCtrl(new IPAddress(edtHost.getText().trim(), port));
        } else {
            App.ctrl = new ClientCtrl();
        }
        if (App.ctrl.openConnection()) {
            edtHost.setText(App.ctrl.getServerAddress().getHost());
            edtPort.setText(String.valueOf(App.ctrl.getServerAddress().getPort()));
            edtHost.setEditable(false);
            edtPort.setEditable(false);
            btnDisconnect.setEnabled(true);
            btnConnect.setEnabled(false);
            parent.connect();
            JOptionPane.showMessageDialog(this, "Connect successfully.");
        } else {
            if(App.ctrl != null) {
                App.ctrl.closeConnection();
                App.ctrl = null;
            }
            edtHost.setEditable(true);
            edtPort.setEditable(true);
            btnDisconnect.setEnabled(false);
            btnConnect.setEnabled(true);
            parent.disconnect();
            JOptionPane.showMessageDialog(this, "Connect failed.", "Message", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnConnectActionPerformed

    private void btnDisconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisconnectActionPerformed
        // TODO add your handling code here:
        if(App.ctrl != null) {
            App.ctrl.closeConnection();
            App.ctrl = null;
        }
        edtHost.setEditable(true);
        edtPort.setEditable(true);
        btnDisconnect.setEnabled(false);
        btnConnect.setEnabled(true);
        parent.disconnect();
    }//GEN-LAST:event_btnDisconnectActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConnect;
    private javax.swing.JButton btnDisconnect;
    private javax.swing.JTextField edtHost;
    private javax.swing.JTextField edtPort;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}