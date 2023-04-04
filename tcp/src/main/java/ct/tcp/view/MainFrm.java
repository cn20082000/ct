package ct.tcp.view;

import ct.tcp.App;
import ct.tcp.control.ClientCtrl;
import ct.tcp.control.RoomCtrl;
import ct.tcp.control.ServerCtrl;

import javax.swing.*;

public class MainFrm extends JFrame {
    private JTextField edtHost;
    private JTextField edtPort;
    private JTextField edtRmiHost;
    private JTextField edtRmiPort;
    private JTextField edtKey;
    private JButton btnLookUp;
    private JButton btnStart;
    private JButton btnStop;
    private JTextArea tvLog;
    private JPanel panel;

    public MainFrm() {
        this.setContentPane(panel);
        this.setSize(700, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Tcp Server");

        btnLookUp.addActionListener(e -> {
            if (!edtRmiHost.getText().isBlank()
                    && !edtRmiPort.getText().isBlank()
                    && !edtKey.getText().isBlank()) {
                int serverPort = Integer.parseInt(edtRmiPort.getText().trim());
                App.client = new ClientCtrl(edtRmiHost.getText().trim(), serverPort, edtKey.getText().trim());
            } else {
                App.client = new ClientCtrl();
            }
            if (App.client.init()) {
                edtRmiHost.setText(App.client.getServerAddress().getHost());
                edtRmiPort.setText(String.valueOf(App.client.getServerAddress().getPort()));
                edtKey.setText(App.client.getServiceKey());

                printLog("Connect to rmi server successfully.");
            } else {
                App.client = null;
                printLog("Connect to rmi server failed.");
            }
        });

        btnStart.addActionListener(e -> {
            try {
                if (!edtPort.getText().isBlank()) {
                    int port = Integer.parseInt(edtPort.getText().trim());
                    App.server = new ServerCtrl(port);
                } else {
                    App.server = new ServerCtrl();
                }
                App.room = new RoomCtrl();
                edtHost.setText(App.server.getMyAddress().getHost());
                edtPort.setText(String.valueOf(App.server.getMyAddress().getPort()));

                edtHost.setEditable(false);
                edtPort.setEditable(false);
                btnStart.setEnabled(false);
                btnStop.setEnabled(true);

                printLog("TCP server is running at the port " + App.server.getMyAddress().getPort() +"...");
            } catch (Exception ex) {
                App.room = null;
                printLog("Start tcp server failed.");
            }
        });

        btnStop.addActionListener(e -> {
            try {
                if (App.server != null) {
                    App.server.stopServer();
                    App.server = null;
                }
                App.room = null;
                edtHost.setText("localhost");

                edtHost.setEditable(true);
                edtPort.setEditable(true);
                btnStop.setEnabled(false);
                btnStart.setEnabled(true);

                printLog("TCP server is stopped!");
            } catch (Exception ex) {
                printLog("Stop tcp server failed.");
            }
        });
    }

    private void printLog(String msg) {
        tvLog.append("\n" + msg);
        tvLog.setCaretPosition(tvLog.getDocument().getLength());
    }
}
