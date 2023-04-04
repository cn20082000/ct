package ct.client.view.project;

import ct.client.App;
import ct.client.view.project.download.DownloadingDialog;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Files;

public class DownloadProjectDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField edtLocation;
    private JButton btnBrowser;
    private JTextField edtName;
    private JLabel tvError;

    public DownloadProjectDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Download project");
        edtName.setText(App.project.getInfo().getName());

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

        btnBrowser.addActionListener(e -> {
            JFileChooser dialog = new JFileChooser();
            dialog.setDialogTitle("Location");
            dialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            dialog.setAcceptAllFileFilterUsed(false);

            int result = dialog.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                String s = dialog.getSelectedFile().getAbsolutePath();
                edtLocation.setText(s);
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
        if (edtLocation.getText().trim().equals("")) {
            tvError.setText("You need to choose the location.");
            edtLocation.requestFocus();
            return;
        }
        if (edtName.getText().trim().equals("")) {
            tvError.setText("You need to choose the name.");
            edtName.requestFocus();
            return;
        }
        String path = edtLocation.getText() + "\\" + edtName.getText();
        java.io.File folder = new java.io.File(path);
        if (folder.exists()) {
            tvError.setText(edtName.getText() + " directory already exists.");
            edtName.requestFocus();
            return;
        }
        tvError.setText(" ");
        DownloadingDialog dialog = new DownloadingDialog(path);
        dialog.pack();
        dialog.setVisible(true);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
