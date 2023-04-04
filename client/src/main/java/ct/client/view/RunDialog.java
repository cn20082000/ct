package ct.client.view;

import ct.client.App;
import ct.model.ObjectWrapper;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class RunDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextArea taInput;
    private JTextArea taOutput;

    public RunDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Run project...");

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
        try {
            App.client.sendData(new ObjectWrapper.Builder()
                            .setLabel(ObjectWrapper.LBL_GET)
                            .setPerformative(ObjectWrapper.RUN_PROJECT)
                            .setData(taInput.getText())
                            .build())
                    .register(ObjectWrapper.RUN_PROJECT, this::runProjectResult);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Cannot send data to server.",
                    "Message", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void runProjectResult(ObjectWrapper data) {
        if (data.getLabel() == ObjectWrapper.LBL_SUCCESS) {
            String result = (String) data.getData();
            taOutput.setText(result);
        } else {
            JOptionPane.showMessageDialog(this, data.getData(), "Message", JOptionPane.ERROR_MESSAGE);
        }
    }
}
