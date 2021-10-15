/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ct.client.util;

import cn.ct.client.model.UserStatus;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author cn200
 */
public class UserStatusRenderer extends JLabel implements ListCellRenderer<UserStatus> {
 
    public UserStatusRenderer() {
        setOpaque(true);
    }
 
    @Override
    public Component getListCellRendererComponent(JList<? extends UserStatus> list, UserStatus user, 
            int index, boolean isSelected, boolean cellHasFocus) {
 
        boolean status = user.isOnline();
        ImageIcon imageIcon;
        if (status) {
            imageIcon = new ImageIcon(getClass().getResource("/image/ic_online.png"));
        } else {
            imageIcon = new ImageIcon(getClass().getResource("/image/ic_offline.png"));
        }
 
        setIcon(imageIcon);
        setText(user.getUser().getName());
 
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
 
        return this;
    }
} 
