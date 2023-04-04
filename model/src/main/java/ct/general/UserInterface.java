package ct.general;

import ct.model.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface UserInterface extends Remote {
    User signIn(User user) throws RemoteException;
    User signUp(User user) throws RemoteException;
    boolean updateUser(User user) throws RemoteException;
    List<User> getUserByKey(String key) throws RemoteException;
}
