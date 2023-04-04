package ct.rmi.view;

import ct.rmi.control.ServerCtrl;

import javax.swing.*;

public class MainFrm extends JFrame {
    private JTextField edtHost;
    private JTextField edtPort;
    private JTextField edtKey;
    private JButton btnStart;
    private JButton btnStop;
    private JTextArea tvLog;
    private JPanel panel;

    private ServerCtrl server;

    public MainFrm() {
        this.setContentPane(panel);
        this.setSize(700, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Rmi Server");

        btnStart.addActionListener(e -> {
            try {
                if (!edtPort.getText().isBlank()
                        && !edtKey.getText().isBlank()) {
                    int port = Integer.parseInt(edtPort.getText().trim());
                    server = new ServerCtrl(port, edtKey.getText().trim());
                } else {
                    server = new ServerCtrl();
                }
                server.start();

                edtHost.setText(server.getMyAddress().getHost());
                edtPort.setText(String.valueOf(server.getMyAddress().getPort()));
                edtKey.setText(server.getServiceKey());

                edtHost.setEditable(false);
                edtPort.setEditable(false);
                edtKey.setEditable(false);
                btnStop.setEnabled(true);
                btnStart.setEnabled(false);

                printLog("The RIM has registered the service key: " + edtKey.getText() + ", at the port: " + edtPort.getText());
            } catch (Exception ex) {
                printLog("Error in starting the RIM server");
                ex.printStackTrace();
            }
        });

        btnStop.addActionListener(e -> {
            if (server != null) {
                try {
                    server.stop();
                    server = null;
                } catch (Exception ex) {
                    printLog("Error in closing the RIM server");
                    ex.printStackTrace();
                }
            }
            edtHost.setText("localhost");

            edtHost.setEditable(true);
            edtPort.setEditable(true);
            edtKey.setEditable(true);
            btnStop.setEnabled(false);
            btnStart.setEnabled(true);

            printLog("The RIM has unbinded the service key: " + edtKey.getText() + ", at the port: " + edtPort.getText());
        });
    }

    private void printLog(String msg) {
        tvLog.append("\n" + msg);
        tvLog.setCaretPosition(tvLog.getDocument().getLength());
    }
}
