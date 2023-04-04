package ct.client.view.collab;

import ct.client.App;
import ct.client.util.SomeFunc;
import ct.model.Collab;
import ct.model.ObjectWrapper;
import ct.model.Project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListCollabDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable tbl;

    List<Collab> collabs = new ArrayList<>();

    public ListCollabDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Collab invitation");

        try {
            App.client.sendData(new ObjectWrapper.Builder()
                            .setLabel(ObjectWrapper.LBL_GET)
                            .setPerformative(ObjectWrapper.LIST_COLLAB)
                            .build())
                    .register(ObjectWrapper.LIST_COLLAB, this::listCollabResult);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Cannot send data to server.",
                    "Message", JOptionPane.ERROR_MESSAGE);
        }

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        int row = tbl.getSelectedRow();

        if (row >= 0 && row < collabs.size()) {
            try {
                App.client.sendData(new ObjectWrapper.Builder()
                                .setLabel(ObjectWrapper.LBL_POST)
                                .setPerformative(ObjectWrapper.ACCEPT_COLLAB)
                                .setData(collabs.get(row))
                                .build())
                        .register(ObjectWrapper.ACCEPT_COLLAB, this::acceptCollabResult);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Cannot send data to server.",
                        "Message", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onCancel() {
        int row = tbl.getSelectedRow();

        if (row >= 0 && row < collabs.size()) {
            try {
                App.client.sendData(new ObjectWrapper.Builder()
                                .setLabel(ObjectWrapper.LBL_POST)
                                .setPerformative(ObjectWrapper.REJECT_COLLAB)
                                .setData(collabs.get(row))
                                .build())
                        .register(ObjectWrapper.REJECT_COLLAB, this::rejectCollabResult);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Cannot send data to server.",
                        "Message", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void listCollabResult(ObjectWrapper data) {
        if (data.getLabel() == ObjectWrapper.LBL_SUCCESS) {
            collabs = (List<Collab>) data.getData();
            String[] columnNames = {"Id", "Project name", "Host", "Create time"};
            String[][] value = new String[collabs.size()][columnNames.length];
            for (int i = 0; i < collabs.size(); ++i) {
                value[i][0] = String.valueOf(i + 1);
                value[i][1] = collabs.get(i).getFromProject().getInfo().getName();
                value[i][2] = collabs.get(i).getFromProject().getHost().getName();
                value[i][3] = SomeFunc.formatTime(collabs.get(i).getFromProject().getInfo().getCreateAt());
            }

            DefaultTableModel tableModel = new DefaultTableModel(value, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            tbl.setModel(tableModel);
            tbl.getColumnModel().getColumn(0).setPreferredWidth(10);
            tbl.getColumnModel().getColumn(1).setPreferredWidth(150);
            tbl.getColumnModel().getColumn(2).setPreferredWidth(120);
            tbl.getColumnModel().getColumn(3).setPreferredWidth(100);
            tbl.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        } else {
            JOptionPane.showMessageDialog(this, data.getData(), "Message", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void acceptCollabResult(ObjectWrapper data) {
        if (data.getLabel() == ObjectWrapper.LBL_SUCCESS) {
            try {
                App.client.sendData(new ObjectWrapper.Builder()
                                .setLabel(ObjectWrapper.LBL_GET)
                                .setPerformative(ObjectWrapper.LIST_COLLAB)
                                .build())
                        .register(ObjectWrapper.LIST_COLLAB, this::listCollabResult);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Cannot send data to server.",
                        "Message", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, data.getData(), "Message", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void rejectCollabResult(ObjectWrapper data) {
        if (data.getLabel() == ObjectWrapper.LBL_SUCCESS) {
            try {
                App.client.sendData(new ObjectWrapper.Builder()
                                .setLabel(ObjectWrapper.LBL_GET)
                                .setPerformative(ObjectWrapper.LIST_COLLAB)
                                .build())
                        .register(ObjectWrapper.LIST_COLLAB, this::listCollabResult);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Cannot send data to server.",
                        "Message", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, data.getData(), "Message", JOptionPane.ERROR_MESSAGE);
        }
    }
}
