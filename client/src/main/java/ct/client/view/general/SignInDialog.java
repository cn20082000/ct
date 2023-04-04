package ct.client.view.general;

import ct.client.App;
import ct.model.ObjectWrapper;
import ct.model.User;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class SignInDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField edtUsername;
    private JPasswordField edtPassword;

    public SignInDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Sign in");

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
        if (edtUsername.getText().isBlank() || new String(edtPassword.getPassword()).isBlank()) {
            JOptionPane.showMessageDialog(this, "Username or password can not empty.",
                    "Message", JOptionPane.ERROR_MESSAGE);
        } else {
            User user = new User(null, null, edtUsername.getText(), new String(edtPassword.getPassword()));
            try {
                App.client.sendData(new ObjectWrapper.Builder()
                                .setLabel(ObjectWrapper.LBL_GET)
                                .setPerformative(ObjectWrapper.SIGN_IN)
                                .setData(user)
                                .build())
                        .register(ObjectWrapper.SIGN_IN, this::signInResult);
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

    private void signInResult(ObjectWrapper data) {
        if (data.getLabel() == ObjectWrapper.LBL_SUCCESS) {
            JOptionPane.showMessageDialog(this, "Sign in successfully.");
            App.user = (User) data.getData();
            App.isSignIn = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, data.getData(), "Message", JOptionPane.ERROR_MESSAGE);
            edtUsername.requestFocus();
            edtPassword.setText("");
        }
    }
}
