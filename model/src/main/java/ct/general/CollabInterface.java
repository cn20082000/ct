package ct.general;

import ct.model.Collab;
import ct.model.Project;
import ct.model.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface CollabInterface extends Remote {
    Collab createCollab(Collab collab) throws RemoteException;
    List<Collab> getPendingCollabByUser(User user) throws RemoteException;
    Collab getCollab(User user, Project project) throws RemoteException;
    boolean acceptCollab(Long id) throws RemoteException;
    boolean rejectCollab(Long id) throws RemoteException;
}
