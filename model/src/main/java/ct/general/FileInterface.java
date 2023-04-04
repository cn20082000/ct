package ct.general;

import ct.model.File;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FileInterface extends Remote {
    File createFile(File file) throws RemoteException;
    File getFile(Long id) throws RemoteException;
}
