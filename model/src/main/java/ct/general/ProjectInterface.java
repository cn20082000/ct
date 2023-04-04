package ct.general;

import ct.model.Project;
import ct.model.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ProjectInterface extends Remote {
    Project createProject(Project project) throws RemoteException;
    List<Project> getProjectByUser(User user) throws RemoteException;
    List<Project> getCollabProjectByUser(User user) throws RemoteException;
    Project getProject(Long id) throws RemoteException;
    Project updateProject(Project project) throws RemoteException;
    boolean deleteProject(Project project) throws RemoteException;
}
