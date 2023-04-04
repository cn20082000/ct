package ct.client.view.general;

import ct.client.App;
import ct.client.control.ClientCtrl;
import ct.model.IPAddress;

import javax.swing.*;
import java.awt.event.*;

public class ConnectionDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField edtHost;
    private JTextField edtPort;

    public ConnectionDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Connection");
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
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        if (!edtHost.getText().isBlank() &&
                !edtPort.getText().isBlank()) {
            int port = Integer.parseInt(edtPort.getText().trim());
            App.client = new ClientCtrl(new IPAddress(edtHost.getText().trim(), port));
        } else {
            App.client = new ClientCtrl();
        }
        if (App.client.openConnection()) {
            edtHost.setText(App.client.getServerAddress().getHost());
            edtPort.setText(String.valueOf(App.client.getServerAddress().getPort()));
            edtHost.setEditable(false);
            edtPort.setEditable(false);

            buttonCancel.setEnabled(true);
            buttonOK.setEnabled(false);
            JOptionPane.showMessageDialog(this, "Connect successfully.");

            App.isConnect = true;
        } else {
            if (App.client != null) {
                App.client.closeConnection();
                App.client = null;
            }
            edtHost.setEditable(true);
            edtPort.setEditable(true);

            buttonCancel.setEnabled(false);
            buttonOK.setEnabled(true);
            JOptionPane.showMessageDialog(this, "Connect failed.", "Message", JOptionPane.ERROR_MESSAGE);

            App.user = null;
            App.project = null;
            App.isConnect = false;
            App.isSignIn = false;
            App.isOpen = false;
            App.isHost = false;
        }
    }

    private void onCancel() {
        if(App.client != null) {
            App.client.closeConnection();
            App.client = null;
        }
        edtHost.setEditable(true);
        edtPort.setEditable(true);
        buttonCancel.setEnabled(false);
        buttonOK.setEnabled(true);

        App.user = null;
        App.project = null;
        App.isConnect = false;
        App.isSignIn = false;
        App.isOpen = false;
        App.isHost = false;
    }

    private void updateView() {
        if(App.client != null) {
            edtHost.setText(App.client.getServerAddress().getHost());
            edtPort.setText(String.valueOf(App.client.getServerAddress().getPort()));
            edtHost.setEditable(false);
            edtPort.setEditable(false);

            buttonCancel.setEnabled(true);
            buttonOK.setEnabled(false);
        } else {
            edtHost.setEditable(true);
            edtPort.setEditable(true);
            buttonCancel.setEnabled(false);
            buttonOK.setEnabled(true);
        }
    }
}
