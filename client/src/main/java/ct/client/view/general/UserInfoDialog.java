package ct.client.view.general;

import ct.client.App;
import ct.model.ObjectWrapper;
import ct.model.User;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class UserInfoDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField edtName;
    private JTextField edtUsername;
    private JPasswordField edtPassword;
    private JPasswordField edtConfirmPassword;

    public UserInfoDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("User info");
        updateView();

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
        if (new String(edtPassword.getPassword()).length() < 6
                || new String(edtPassword.getPassword()).length() > 32) {
            JOptionPane.showMessageDialog(this, "Password must have 6-32 characters.",
                    "Message", JOptionPane.ERROR_MESSAGE);
        } else if (!new String(edtPassword.getPassword())
                .equals(new String(edtConfirmPassword.getPassword()))) {
            JOptionPane.showMessageDialog(this, "Password and confirm password do not match.",
                    "Message", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                App.client.sendData(new ObjectWrapper.Builder()
                                .setLabel(ObjectWrapper.LBL_POST)
                                .setPerformative(ObjectWrapper.CHANGE_PASSWORD)
                                .setData(new User(0L, edtName.getText(), edtUsername.getText(), new String(edtPassword.getPassword())))
                                .build())
                        .register(ObjectWrapper.CHANGE_PASSWORD, this::changePasswordResult);
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

    private void updateView() {
        edtName.setText(App.user.getName());
        edtUsername.setText(App.user.getUsername());
        edtPassword.setText("");
        edtConfirmPassword.setText("");
        edtPassword.requestFocus();
    }

    private void changePasswordResult(ObjectWrapper data) {
        if (data.getLabel() == ObjectWrapper.LBL_SUCCESS) {
            JOptionPane.showMessageDialog(this, "Change password successfully.");
        } else {
            JOptionPane.showMessageDialog(this, data.getData(), "Message", JOptionPane.ERROR_MESSAGE);
        }
        updateView();
    }
}
