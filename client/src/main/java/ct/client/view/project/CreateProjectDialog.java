package ct.client.view.project;

import ct.client.App;
import ct.model.ObjectWrapper;
import ct.model.Project;
import ct.model.User;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class CreateProjectDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField edtName;

    public CreateProjectDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Create project");

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
        if (edtName.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Name can not empty.",
                    "Message", JOptionPane.ERROR_MESSAGE);
        } else if (edtName.getText().contains(" ")) {
            JOptionPane.showMessageDialog(this, "Project's name can not have space.",
                    "Message", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                App.client.sendData(new ObjectWrapper.Builder()
                                .setLabel(ObjectWrapper.LBL_GET)
                                .setPerformative(ObjectWrapper.CREATE_PROJECT)
                                .setData(edtName.getText())
                                .build())
                        .register(ObjectWrapper.CREATE_PROJECT, this::createProjectResult);
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

    private void createProjectResult(ObjectWrapper data) {
        if (data.getLabel() == ObjectWrapper.LBL_SUCCESS) {
            Project project = (Project) data.getData();
            JOptionPane.showMessageDialog(this, "Create project " + project.getInfo().getName() + " successfully.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, data.getData(), "Message", JOptionPane.ERROR_MESSAGE);
        }
    }
}
