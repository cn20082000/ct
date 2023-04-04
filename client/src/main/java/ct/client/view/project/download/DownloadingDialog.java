package ct.client.view.project.download;

import ct.client.App;
import ct.client.util.FileIO;
import ct.model.File;
import ct.model.Folder;
import ct.model.ObjectWrapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DownloadingDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JProgressBar pbDownload;
    private JTextArea tvLog;

    private String path;
    private List<File> files = new ArrayList<>();
    private List<String> data = new ArrayList<>();
    private boolean downloadLock = false;

    public DownloadingDialog(String path) {
        this.path = path;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Downloading...");
        setSize(400, 250);

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

        calculate();
        download();
        writeFile();
        buttonCancel.setText("Close");
        try {
            Desktop.getDesktop().open(new java.io.File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeFile() {
        for (int i = 0; i < files.size(); ++i) {
            printLog("Writing " + i + "/" + files.size());
            pbDownload.setValue(files.size() + i);

            try {
                FileIO.writeToFile(path, files.get(i), data.get(i));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Cannot write file.",
                        "Message", JOptionPane.ERROR_MESSAGE);
                dispose();
            }
        }

        printLog("Write files successful.");
        pbDownload.setValue(files.size() * 2);
    }

    private void download() {
        data.clear();
        while (data.size() < files.size()) {
            if (downloadLock) {
                printLog("Downloading " + data.size() + "/" + files.size());
                pbDownload.setValue(data.size());
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            downloadLock = true;
            try {
                App.client.sendData(new ObjectWrapper.Builder()
                                .setLabel(ObjectWrapper.LBL_GET)
                                .setPerformative(ObjectWrapper.DOWNLOAD_FILE)
                                .setData(files.get(data.size()))
                                .build())
                        .register(ObjectWrapper.DOWNLOAD_FILE, this::downloadFileResult);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Cannot send data to server.",
                        "Message", JOptionPane.ERROR_MESSAGE);
                dispose();
            }
        }
        printLog("Download successful.");
        pbDownload.setValue(data.size());
    }

    private void calculate() {
        printLog("Calculating...");
        files.clear();
        Folder rootFolder = App.project.getRootFolder();
        getChildren(rootFolder);
        pbDownload.setMaximum(files.size() * 2);
        pbDownload.setValue(0);
    }

    private void getChildren(Folder folder) {
        for (Folder f : folder.getFolders()) {
            getChildren(f);
        }
        files.addAll(folder.getFiles());
    }

    private void printLog(String msg) {
        tvLog.append("\n" + msg);
        tvLog.setCaretPosition(tvLog.getDocument().getLength());
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void downloadFileResult(ObjectWrapper data) {
        if (data.getLabel() == ObjectWrapper.LBL_SUCCESS) {
            this.data.add((String)data.getData());
            downloadLock = false;
        } else {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this, data.getData(), "Message", JOptionPane.ERROR_MESSAGE);
                dispose();
            });
        }
    }
}
