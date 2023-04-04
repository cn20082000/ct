package ct.client.control;

import ct.model.IPAddress;
import ct.model.ObjectWrapper;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientCtrl {
    private Socket socket;
    private ClientListening listening;
    private List<ObjectWrapper> function;
    private IPAddress serverAddress = new IPAddress("localhost", 8888);

    public ClientCtrl() {
        function = new ArrayList<>();
    }

    public ClientCtrl(IPAddress serverAddress) {
        this.serverAddress = serverAddress;
        function = new ArrayList<>();
    }

    public boolean openConnection() {
        try {
            socket = new Socket(serverAddress.getHost(), serverAddress.getPort());
            listening = new ClientListening();
            listening.start();
            function.clear();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean closeConnection() {
        try {
            if (listening != null)
                listening.stop();
            if (socket != null) {
                socket.close();
            }
            function.clear();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public ClientCtrl register(int event, ReceiveData action) {
        for (ObjectWrapper func : function) {
            if (func.getPerformative() == event) {
                function.remove(func);
                break;
            }
        }
        function.add(new ObjectWrapper.Builder()
                .setPerformative(event)
                .setData(action)
                .build());
        return this;
    }

    public void unregister(int event) {
        for (ObjectWrapper func : function) {
            if (func.getPerformative() == event) {
                function.remove(func);
                break;
            }
        }
    }

    public ClientCtrl sendData(Object obj) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(obj);
        return this;
    }

    public Socket getSocket() {
        return socket;
    }

    public List<ObjectWrapper> getFunction() {
        return function;
    }

    public IPAddress getServerAddress() {
        return serverAddress;
    }
}
