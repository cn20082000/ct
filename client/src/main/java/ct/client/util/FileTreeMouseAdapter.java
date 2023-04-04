package ct.client.util;

import ct.client.App;
import ct.client.view.MainFrm;
import ct.client.view.project.CreateClassDialog;
import ct.model.File;
import ct.model.Folder;
import ct.model.ObjectWrapper;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class FileTreeMouseAdapter extends MouseAdapter {
    private MainFrm view;

    public FileTreeMouseAdapter(MainFrm view) {
        this.view = view;
    }

    private void myPopupEvent(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        JTree tree = (JTree) e.getSource();
        TreePath path = tree.getPathForLocation(x, y);
        if (path == null) {
            return;
        }

        DefaultMutableTreeNode rightClickedNode = (DefaultMutableTreeNode) path.getLastPathComponent();

        TreePath[] selectionPaths = tree.getSelectionPaths();

        boolean isSelected = false;
        if (selectionPaths != null) {
            for (TreePath selectionPath : selectionPaths) {
                if (selectionPath.equals(path)) {
                    isSelected = true;
                }
            }
        }
        if (!isSelected) {
            tree.setSelectionPath(path);
        }
        if (rightClickedNode.isLeaf()) {
            JPopupMenu popup = new JPopupMenu();
            final JMenuItem openItem = new JMenuItem("Open file");
//            final JMenuItem deleteItem = new JMenuItem("Delete file");
            openItem.addActionListener(ev -> {
                System.out.println("Open file " + path.toString());
                try {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                    App.client.sendData(new ObjectWrapper.Builder()
                            .setLabel(ObjectWrapper.LBL_GET)
                            .setPerformative(ObjectWrapper.OPEN_FILE)
                            .setData(node.getUserObject())
                            .build());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(view, "Cannot send data to server.",
                            "Message", JOptionPane.ERROR_MESSAGE);
                }
            });
//            deleteItem.addActionListener(ev -> {
//                System.out.println("Delete file " + path.toString());
//                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
//                if (Objects.equals(((File) node.getUserObject()).getInfo().getName(), "Main.java")) {
//                    JOptionPane.showMessageDialog(view, "You can not delete Main class",
//                            "Message", JOptionPane.ERROR_MESSAGE);
//                }
//            });
            popup.add(openItem);
//            if (App.isHost) {
//                popup.add(deleteItem);
//            }
            popup.show(tree, x, y);
        } else if (App.isHost) {
            JPopupMenu popup = new JPopupMenu();
            final JMenuItem newItem = new JMenuItem("New class");
//            final JMenuItem deleteItem = new JMenuItem("Delete folder");
            newItem.addActionListener(ev -> {
                System.out.println("New class at " + path.toString());
                CreateClassDialog dialog = new CreateClassDialog();
                dialog.pack();
                dialog.setVisible(true);
            });
//            deleteItem.addActionListener(ev -> {
//                System.out.println("Delete folder " + path.toString());
//            });
            popup.add(newItem);
//            popup.add(deleteItem);
            popup.show(tree, x, y);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger())
            myPopupEvent(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger())
            myPopupEvent(e);
    }
}
