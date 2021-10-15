/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ct.client.control;

import cn.ct.client.App;
import cn.ct.model.ObjectWrapper;
import java.io.ObjectInputStream;
import java.util.Iterator;

/**
 *
 * @author cn200
 */
public class ClientListening extends Thread {
         
    public ClientListening() {
        super();
    }

    public void run() {
        try {
            while(true) {
                ObjectInputStream ois = new ObjectInputStream(App.ctrl.getSocket().getInputStream());
                Object obj = ois.readObject();
                if(obj instanceof ObjectWrapper) {
                    ObjectWrapper data = (ObjectWrapper)obj;
                    int i = 0;
                    for (i = 0; i < App.ctrl.getMyFunction().size(); ++i) {
                        if(App.ctrl.getMyFunction().get(i).getPerformative() == data.getPerformative()) {
                            break;
                        }
                    }
                    ObjectWrapper func = App.ctrl.getMyFunction().get(i);
                    ((ReceiveData) func.getData()).process(data);
//                    for(ObjectWrapper func : App.ctrl.getMyFunction()) {
//                        if(func.getPerformative() == data.getPerformative()) {
//                            ((ReceiveData) func.getData()).process(data);
//                        }
//                    }
//                    Iterator<ObjectWrapper> iterator = App.ctrl.getMyFunction().iterator();
//                    while (iterator.hasNext()) {
//                        ObjectWrapper func = iterator.next();
//                        if(func.getPerformative() == data.getPerformative()) {
//                            ((ReceiveData) func.getData()).process(data);
//                        }
//                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}