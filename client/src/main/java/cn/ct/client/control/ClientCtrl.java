/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ct.client.control;

import cn.ct.model.IPAddress;
import cn.ct.model.ObjectWrapper;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author cn200
 */
public class ClientCtrl {
    private Socket mySocket;
    private ClientListening myListening;
    private List<ObjectWrapper> myFunction;  
    private IPAddress serverAddress = new IPAddress("localhost", 8888);  // default server host and port
     
    public ClientCtrl(){
        super();
        myFunction = new ArrayList<>();  
    }
     
    public ClientCtrl(IPAddress serverAddr) {
        super();
        this.serverAddress = serverAddr;
        myFunction = new ArrayList<>();  
    }
 
    public boolean openConnection(){
        try {
            mySocket = new Socket(serverAddress.getHost(), serverAddress.getPort()); 
            myListening = new ClientListening();
            myListening.start();
            myFunction.clear();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public ClientCtrl sendData(Object obj){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(mySocket.getOutputStream());
            oos.writeObject(obj);
        } catch (Exception e) {
            return this;
        }
        return this;
    }
    
    public ClientCtrl register(int event, ReceiveData action) {
//        Iterator<ObjectWrapper> iterator = myFunction.iterator();
//        while (iterator.hasNext()) {
//            ObjectWrapper func = iterator.next();
//            if (func.getPerformative() == event) {
//                iterator.remove();
//            }
//        }
//        int index = 0;
//        for (index = 0; index < myFunction.size(); ++index) {
//            if (myFunction.get(index).getPerformative() == event) {
//                break;
//            }
//        }
//        if (index >= myFunction.size()) {
//            myFunction.add(new ObjectWrapper(event, action));
//        } else {
//            myFunction.set(index, new ObjectWrapper(event, action));
//        }
        for (ObjectWrapper func : myFunction) {
            if (func.getPerformative() == event) {
                myFunction.remove(func);
                break;
            }
        }
        myFunction.add(new ObjectWrapper(event, action));
        return this;
    }
    
    public void unregister(int event) {
        for (ObjectWrapper func : myFunction) {
            if (func.getPerformative() == event) {
                myFunction.remove(func);
                break;
            }
        }
    }
     
    public boolean closeConnection(){
        try {
            if(myListening != null)
                myListening.stop();
            if(mySocket != null) {
                mySocket.close();
            }     
            myFunction.clear();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public Socket getSocket() {
        return this.mySocket;
    }

    public List<ObjectWrapper> getMyFunction() {
        return myFunction;
    }

    public IPAddress getServerAddress() {
        return serverAddress;
    }
}
