package ct.tcp.control;

import ct.general.CollabInterface;
import ct.general.FileInterface;
import ct.general.ProjectInterface;
import ct.general.UserInterface;
import ct.model.*;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class ClientCtrl {
    private UserInterface userRO;
    private ProjectInterface projectRO;
    private CollabInterface collabRO;
    private FileInterface fileRO;
    private IPAddress serverAddress = new IPAddress("localhost", 9999);
    private String serviceKey = "rmiServer";

    public ClientCtrl() {
    }

    public ClientCtrl(String serverHost, int serverPort, String service) {
        serverAddress.setHost(serverHost);
        serverAddress.setPort(serverPort);
        serviceKey = service;
    }

    public boolean init() {
        try {
            Registry registry = LocateRegistry.getRegistry(serverAddress.getHost(), serverAddress.getPort());
            userRO = (UserInterface) (registry.lookup(serviceKey));
            projectRO = (ProjectInterface) (registry.lookup(serviceKey));
            collabRO = (CollabInterface) (registry.lookup(serviceKey));
            fileRO = (FileInterface) (registry.lookup(serviceKey));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public IPAddress getServerAddress() {
        return serverAddress;
    }

    public String getServiceKey() {
        return serviceKey;
    }

    public User remoteSignIn(User user) throws RemoteException {
        return userRO.signIn(user);
    }

    public User remoteSignUp(User user) throws RemoteException {
        return userRO.signUp(user);
    }

    public boolean remoteUpdateUser(User user) throws RemoteException {
        return userRO.updateUser(user);
    }

    public List<User> remoteGetUserByKey(String key) throws RemoteException {
        return userRO.getUserByKey(key);
    }

    public Project remoteCreateProject(Project project) throws RemoteException {
        return projectRO.createProject(project);
    }

    public List<Project> remoteGetProjectByUser(User user) throws RemoteException {
        return projectRO.getProjectByUser(user);
    }

    public List<Project> remoteGetCollabProjectByUser(User user) throws RemoteException {
        return projectRO.getCollabProjectByUser(user);
    }

    public Project remoteGetProject(Long id) throws RemoteException {
        return projectRO.getProject(id);
    }

    public Project remoteUpdateProject(Project project) throws RemoteException {
        return projectRO.updateProject(project);
    }

    public boolean remoteDeleteProject(Project project) throws RemoteException {
        return projectRO.deleteProject(project);
    }

    public File remoteCreateFile(File file) throws RemoteException {
        return fileRO.createFile(file);
    }

    public File remoteGetFile(Long id) throws RemoteException {
        return fileRO.getFile(id);
    }

    public Collab remoteCreateCollab(Collab collab) throws RemoteException {
        return collabRO.createCollab(collab);
    }

    public List<Collab> remoteGetPendingCollabByUser(User user) throws RemoteException {
        return collabRO.getPendingCollabByUser(user);
    }

    public Collab remoteGetCollab(User user, Project project) throws RemoteException {
        return collabRO.getCollab(user, project);
    }

    public boolean remoteAcceptCollab(Long id) throws RemoteException {
        return collabRO.acceptCollab(id);
    }

    public boolean remoteRejectCollab(Long id) throws RemoteException {
        return collabRO.rejectCollab(id);
    }
}
