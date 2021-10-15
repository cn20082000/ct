/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ct.client.view;

import cn.ct.client.App;
import cn.ct.client.frm.MainFrm;
import cn.ct.client.frm.ReceiveCollabDialog;
import cn.ct.client.model.UserStatus;
import cn.ct.client.util.UserStatusRenderer;
import cn.ct.model.Collab;
import cn.ct.model.File;
import cn.ct.model.Folder;
import cn.ct.model.ObjectWrapper;
import cn.ct.model.Project;
import cn.ct.model.User;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author cn200
 */
public class MainPanel extends javax.swing.JPanel {
    
    private MainFrm parent;
    private List<UserStatus> us = new ArrayList<>();

    /**
     * Creates new form MainPanel
     */
    public MainPanel(MainFrm parent) {
        this.parent = parent;
        initComponents();
        
        listCollab.setCellRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList list, 
                    Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                boolean status = us.get(index).isOnline();
                ImageIcon imageIcon;
                if (status) {
                    imageIcon = new ImageIcon("image/ic_online.png");
                } else {
                    imageIcon = new ImageIcon("image/ic_offline.png");
                }

                label.setIcon(imageIcon);
                label.setText(us.get(index).getUser().getName());

                if (isSelected) {
                    label.setBackground(list.getSelectionBackground());
                    label.setForeground(list.getSelectionForeground());
                } else {
                    label.setBackground(list.getBackground());
                    label.setForeground(list.getForeground());
                }
                return label;
            }
        });
        
        tp.add("Tab 1", new EditorPanel());
        SwingUtilities.invokeLater(() -> {
            updateView();
        });
    }
    
    public void regis() {
        App.ctrl.register(ObjectWrapper.REPLY_OPEN_PROJECT, data -> {
            openProjectResult(data);
        });
        App.ctrl.register(ObjectWrapper.ONL_LIST_COLLAB, data -> {
            liCollabResult(data);
        });
        App.ctrl.register(ObjectWrapper.ONL_LIST_ONLINE, data -> {
            liOnlineResult(data);
        });
        App.ctrl.register(ObjectWrapper.ONL_RECEIVE_COLLAB, data -> {
            reCollabResult(data);
        });
    }
    
    private void updateView() {
        if (App.project != null) {
            App.project.getRootFolder().sort();

            DefaultMutableTreeNode root = 
                    new DefaultMutableTreeNode(App.project.getInfo().getName());
            root.add(setTreeNode(App.project.getRootFolder()));

            DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
            model.setRoot(root);
            model.reload();
        }
        if (us != null) {
            DefaultListModel<String> listModel = new DefaultListModel<>();
            int online = 0;
            for (UserStatus u : us) {
                listModel.addElement(u.getUser().getName());
                online += u.isOnline() ? 1 : 0;
            }
            tvOnline.setText("Online(" + online + ")");
            listCollab.setModel(listModel);
        }
    }
    
    private DefaultMutableTreeNode setTreeNode(Folder folder) {
        DefaultMutableTreeNode fd = 
                new DefaultMutableTreeNode(folder.getInfo().getName());
        for (Folder f : folder.getFolders()) {
            fd.add(setTreeNode(f));
        }
        for (File f : folder.getFiles()) {
            fd.add(new DefaultMutableTreeNode(f.getInfo().getName()));
        }
        return fd;
    }
    
    private void openProjectResult(ObjectWrapper data) {
        if (data.getMessage() == ObjectWrapper.MES_SUCCESS) {
            App.project = (Project) data.getData();
            parent.openProject();
            SwingUtilities.invokeLater(() -> {
                updateView();
            });
        } else {
            JOptionPane.showMessageDialog(this, "Failed to open project.", 
                    "Message", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void reCollabResult(ObjectWrapper data) {
        if (data.getMessage() == ObjectWrapper.MES_SUCCESS) {
            ReceiveCollabDialog dialog = new ReceiveCollabDialog(this, (Collab) data.getData());
            dialog.open();
        }
    }
    
    private void liCollabResult(ObjectWrapper data) {
        if (data.getMessage() == ObjectWrapper.MES_SUCCESS) {
            us = new ArrayList<>();
            List<Collab> cs = (List<Collab>) data.getData();
            for (Collab c : cs) {
                us.add(new UserStatus(c.getToUser(), false));
            }
            SwingUtilities.invokeLater(() -> {
                updateView();
            });
        }
    }
    
    private void liOnlineResult(ObjectWrapper data) {
        if (data.getMessage() == ObjectWrapper.MES_SUCCESS) {
            List<User> u = (List<User>) data.getData();
            for (int i = 0; i < u.size(); ++i) {
                for (int j = 0; j < us.size(); ++j) {
                    if (u.get(i).getId() == us.get(j).getUser().getId()) {
                        us.get(j).setOnline(true);
                    }
                }
            }
            SwingUtilities.invokeLater(() -> {
                updateView();
            });
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

        jSplitPane3 = new javax.swing.JSplitPane();
        tp = new javax.swing.JTabbedPane();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        tvOnline = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listCollab = new javax.swing.JList<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tree = new javax.swing.JTree();

        jSplitPane3.setDividerLocation(200);
        jSplitPane3.setDividerSize(3);
        jSplitPane3.setRightComponent(tp);

        jSplitPane1.setDividerLocation(300);
        jSplitPane1.setDividerSize(3);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        tvOnline.setText("Online(0)");

        jScrollPane2.setViewportView(listCollab);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tvOnline)
                .addContainerGap(143, Short.MAX_VALUE))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tvOnline)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE))
        );

        jSplitPane1.setRightComponent(jPanel2);

        jScrollPane1.setViewportView(tree);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jSplitPane3.setLeftComponent(jSplitPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane3)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JList<String> listCollab;
    private javax.swing.JTabbedPane tp;
    private javax.swing.JTree tree;
    private javax.swing.JLabel tvOnline;
    // End of variables declaration//GEN-END:variables
}
