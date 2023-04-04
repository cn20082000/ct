package ct.tcp.control;

import ct.model.IPAddress;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerCtrl {
    private IPAddress myAddress = new IPAddress("localhost", 8888);
    private ServerSocket myServer;
    private ServerListening myListening;
    public static List<ServerProcess> myProcess;

    public ServerCtrl() throws IOException {
        myProcess = new ArrayList<>();
        openServer();
    }

    public ServerCtrl(int serverPort) throws IOException {
        myProcess = new ArrayList<>();
        myAddress.setPort(serverPort);
        openServer();
    }

    public static List<ServerProcess> getMyProcess() {
        return myProcess;
    }

    private void openServer() throws IOException {
        myServer = new ServerSocket(myAddress.getPort());
        myListening = new ServerListening();
        myListening.start();
        myAddress.setHost(InetAddress.getLocalHost().getHostAddress());
    }

    public void stopServer() throws IOException {
        for (ServerProcess sp : myProcess)
            sp.stop();
        myListening.stop();
        myServer.close();
    }

    public IPAddress getMyAddress() {
        return myAddress;
    }

    class ServerListening extends Thread {
        public ServerListening() {
            super();
        }

        public void run() {
            try {
                while (true) {
                    Socket clientSocket = myServer.accept();
                    ServerProcess sp = new ServerProcess(clientSocket);
                    sp.start();
                    myProcess.add(sp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
