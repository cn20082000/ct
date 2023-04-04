package ct.client.view.collab;

import ct.client.App;
import ct.client.util.SomeFunc;
import ct.model.Collab;
import ct.model.ObjectWrapper;
import ct.model.Project;
import ct.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SendCollabDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField edtKey;
    private JButton btnSearch;
    private JTable tbl;

    private List<User> users = new ArrayList<>();

    public SendCollabDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Send invitation");

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

        btnSearch.addActionListener(e -> {
            try {
                App.client.sendData(new ObjectWrapper.Builder()
                                .setLabel(ObjectWrapper.LBL_GET)
                                .setPerformative(ObjectWrapper.SEARCH_USER)
                                .setData(edtKey.getText())
                                .build())
                        .register(ObjectWrapper.SEARCH_USER, this::searchUserResult);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Cannot send data to server.",
                        "Message", JOptionPane.ERROR_MESSAGE);
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        int row = tbl.getSelectedRow();

        if (row >= 0 && row < users.size()) {
            try {
                App.client.sendData(new ObjectWrapper.Builder()
                                .setLabel(ObjectWrapper.LBL_POST)
                                .setPerformative(ObjectWrapper.SEND_COLLAB)
                                .setData(new Collab(null, users.get(row), App.project, false))
                                .build())
                        .register(ObjectWrapper.SEND_COLLAB, this::sendCollabResult);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Cannot send data to server.",
                        "Message", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void sendCollabResult(ObjectWrapper data) {
        if (data.getLabel() == ObjectWrapper.LBL_SUCCESS) {
            SwingUtilities.invokeLater(() -> {
                try {
                    App.client.sendData(new ObjectWrapper.Builder()
                                    .setLabel(ObjectWrapper.LBL_GET)
                                    .setPerformative(ObjectWrapper.SEARCH_USER)
                                    .setData(edtKey.getText())
                                    .build())
                            .register(ObjectWrapper.SEARCH_USER, this::searchUserResult);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Cannot send data to server.",
                            "Message", JOptionPane.ERROR_MESSAGE);
                }
            });
        } else {
            JOptionPane.showMessageDialog(this, data.getData(), "Message", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchUserResult(ObjectWrapper data) {
        if (data.getLabel() == ObjectWrapper.LBL_SUCCESS) {
            users = (List<User>) data.getData();
            String[] columnNames = {"Id", "Name", "Username"};
            String[][] value = new String[users.size()][columnNames.length];
            for (int i = 0; i < users.size(); ++i) {
                value[i][0] = String.valueOf(i + 1);
                value[i][1] = users.get(i).getName();
                value[i][2] = users.get(i).getUsername();
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
            tbl.getColumnModel().getColumn(2).setPreferredWidth(100);
            tbl.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        } else {
            JOptionPane.showMessageDialog(this, data.getData(), "Message", JOptionPane.ERROR_MESSAGE);
        }
    }
}
