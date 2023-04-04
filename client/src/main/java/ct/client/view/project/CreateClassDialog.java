package ct.client.view.project;

import ct.client.App;
import ct.model.File;
import ct.model.Info;
import ct.model.ObjectWrapper;

import javax.swing.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class CreateClassDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField edtName;

    public CreateClassDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Create new class");

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
            JOptionPane.showMessageDialog(this, "Class name can not empty.",
                    "Message", JOptionPane.ERROR_MESSAGE);
        } else if (edtName.getText().contains(" ")) {
            JOptionPane.showMessageDialog(this, "Class name can not contain space.",
                    "Message", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                String rootFolder = App.project.getRootFolder().getFiles().get(0).getUrl().replace("Main.java", "");
                File f = new File(
                        null,
                        new Info(
                                null,
                                edtName.getText() + ".java",
                                LocalDateTime.now(),
                                UUID.randomUUID().toString()),
                        App.project.getRootFolder(),
                        rootFolder + edtName.getText() + ".java"
                );
                App.client.sendData(new ObjectWrapper.Builder()
                                .setLabel(ObjectWrapper.LBL_POST)
                                .setPerformative(ObjectWrapper.CREATE_FILE)
                                .setData(f)
                                .build())
                        .register(ObjectWrapper.CREATE_FILE, this::createFileResult);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Cannot send data to server.",
                        "Message", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void createFileResult(ObjectWrapper data) {
        if (data.getLabel() == ObjectWrapper.LBL_SUCCESS) {
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, data.getData(), "Message", JOptionPane.ERROR_MESSAGE);
        }
    }
}
