package ct.client.control;

import ct.client.App;
import ct.model.ObjectWrapper;

import java.io.ObjectInputStream;

public class ClientListening extends Thread {
    public ClientListening() {
        super();
    }

    @Override
    public void run() {
        try {
            while (true) {
                ObjectInputStream ois = new ObjectInputStream(App.client.getSocket().getInputStream());
                Object o = ois.readObject();
                if (o instanceof ObjectWrapper) {
                    ObjectWrapper data = (ObjectWrapper) o;
                    int i = 0;
                    for (i = 0; i < App.client.getFunction().size(); ++i) {
                        if (App.client.getFunction().get(i).getPerformative() == data.getPerformative()) {
                            break;
                        }
                    }
                    if (i >= App.client.getFunction().size()) {
                        continue;
                    }
                    ObjectWrapper func = App.client.getFunction().get(i);
                    ((ReceiveData) func.getData()).process(data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
