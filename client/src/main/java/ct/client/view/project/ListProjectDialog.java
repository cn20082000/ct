package ct.client.view.project;

import ct.client.App;
import ct.client.util.SomeFunc;
import ct.model.ObjectWrapper;
import ct.model.Project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListProjectDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable tbl;
    private JLabel tvOpen;

    private boolean isHost;
    private List<Project> projects = new ArrayList<>();

    public ListProjectDialog(boolean isHost) {
        this.isHost = isHost;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        if (isHost) {
            setTitle("Open project");
            tvOpen.setText("Your projects:");
            try {
                App.client.sendData(new ObjectWrapper.Builder()
                                .setLabel(ObjectWrapper.LBL_GET)
                                .setPerformative(ObjectWrapper.LIST_PROJECT)
                                .build())
                        .register(ObjectWrapper.LIST_PROJECT, this::listProjectResult);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Cannot send data to server.",
                        "Message", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            setTitle("Open collab project");
            tvOpen.setText("Your collab projects:");
            try {
                App.client.sendData(new ObjectWrapper.Builder()
                                .setLabel(ObjectWrapper.LBL_GET)
                                .setPerformative(ObjectWrapper.COLLAB_PROJECT)
                                .build())
                        .register(ObjectWrapper.COLLAB_PROJECT, this::collabProjectResult);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Cannot send data to server.",
                        "Message", JOptionPane.ERROR_MESSAGE);
            }
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
        // add your code here
        int row = tbl.getSelectedRow();

        if (row >= 0 && row < projects.size()) {
            try {
                App.client.sendData(new ObjectWrapper.Builder()
                                .setLabel(ObjectWrapper.LBL_GET)
                                .setPerformative(ObjectWrapper.OPEN_PROJECT)
                                .setData(projects.get(row))
                                .build())
                        .register(ObjectWrapper.OPEN_PROJECT, this::openProjectResult);
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

    private void listProjectResult(ObjectWrapper data) {
        if (data.getLabel() == ObjectWrapper.LBL_SUCCESS) {
            projects = (List<Project>) data.getData();
            String[] columnNames = {"Id", "Project name", "Create time"};
            String[][] value = new String[projects.size()][columnNames.length];
            for (int i = 0; i < projects.size(); ++i) {
                value[i][0] = String.valueOf(i + 1);
                value[i][1] = projects.get(i).getInfo().getName();
                value[i][2] = SomeFunc.formatTime(projects.get(i).getInfo().getCreateAt());
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

    private void collabProjectResult(ObjectWrapper data) {
        if (data.getLabel() == ObjectWrapper.LBL_SUCCESS) {
            List<Project> list = (List<Project>) data.getData();
            if (list != null) {
                projects = list;
            }
            String[] columnNames = {"Id", "Project name", "Host", "Create time"};
            String[][] value = new String[projects.size()][columnNames.length];
            for (int i = 0; i < projects.size(); ++i) {
                value[i][0] = String.valueOf(i + 1);
                value[i][1] = projects.get(i).getInfo().getName();
                value[i][2] = projects.get(i).getHost().getName();
                value[i][3] = SomeFunc.formatTime(projects.get(i).getInfo().getCreateAt());
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
            tbl.getColumnModel().getColumn(2).setPreferredWidth(150);
            tbl.getColumnModel().getColumn(3).setPreferredWidth(100);
            tbl.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        } else {
            JOptionPane.showMessageDialog(this, data.getData(), "Message", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openProjectResult(ObjectWrapper data) {
        if (data.getLabel() == ObjectWrapper.LBL_SUCCESS) {
            App.project = (Project) data.getData();
            App.isOpen = true;
            App.isHost = isHost;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, data.getData(), "Message", JOptionPane.ERROR_MESSAGE);
        }
    }
}
