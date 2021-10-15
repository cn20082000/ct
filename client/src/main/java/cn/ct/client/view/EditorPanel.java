/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ct.client.view;

import cn.ct.client.util.TextLineNumber;
import java.awt.Font;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

/**
 *
 * @author cn200
 */
public class EditorPanel extends javax.swing.JPanel {

    /**
     * Creates new form EditorPanel
     */
    public EditorPanel() {
        initComponents();
        
        // line number
        TextLineNumber tln = new TextLineNumber(tp);
        sp.setRowHeaderView(tln);
        
        // set font
        tp.setFont(new Font("Consolas", Font.PLAIN, 13));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sp = new javax.swing.JScrollPane();
        tp = new javax.swing.JTextArea();

        tp.setColumns(20);
        tp.setRows(5);
        sp.setViewportView(tp);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sp, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sp, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane sp;
    private javax.swing.JTextArea tp;
    // End of variables declaration//GEN-END:variables
}