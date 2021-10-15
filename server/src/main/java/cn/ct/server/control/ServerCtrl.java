/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ct.server.control;

import cn.ct.model.IPAddress;
import cn.ct.server.view.MainPanel;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cn200
 */
public class ServerCtrl {
    private MainPanel view;
    private ServerSocket myServer;
    private ServerListening myListening;
    public static List<ServerProcess> myProcess;
    private IPAddress myAddress = new IPAddress("localhost", 8888);  //default server host and port
     
    public ServerCtrl(MainPanel view){
        myProcess = new ArrayList<ServerProcess>();
        this.view = view;
        openServer();       
    }
     
    public ServerCtrl(MainPanel view, int serverPort){
        myProcess = new ArrayList<ServerProcess>();
        this.view = view;
        myAddress.setPort(serverPort);
        openServer();       
    }
     
    private void openServer(){
        try {
            myServer = new ServerSocket(myAddress.getPort());
            myListening = new ServerListening();
            myListening.start();
            myAddress.setHost(InetAddress.getLocalHost().getHostAddress());
            view.showServerInfor(myAddress);
            view.showMessage("TCP server is running at the port " + myAddress.getPort() +"...");
        } catch(Exception e) {
            e.printStackTrace();;
        }
    }
     
    public void stopServer() {
        try {
            for(ServerProcess sp : myProcess)
                sp.stop();
            myListening.stop();
            myServer.close();
            view.showMessage("TCP server is stopped!");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    class ServerListening extends Thread{
        public ServerListening() {
            super();
        }
         
        public void run() {
            view.showMessage("server is listening... ");
            try {
                while(true) {
                    Socket clientSocket = myServer.accept();
                    ServerProcess sp = new ServerProcess(clientSocket);
                    sp.start();
                    myProcess.add(sp);
                    view.showMessage("Number of client connecting to the server: " + myProcess.size());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
