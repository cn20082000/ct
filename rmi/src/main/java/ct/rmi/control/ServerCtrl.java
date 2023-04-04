package ct.rmi.control;

import ct.general.CollabInterface;
import ct.general.FileInterface;
import ct.general.ProjectInterface;
import ct.general.UserInterface;
import ct.model.*;
import ct.rmi.dao.CollabDAO;
import ct.rmi.dao.FileDAO;
import ct.rmi.dao.ProjectDAO;
import ct.rmi.dao.UserDAO;
import ct.rmi.util.exception.DatabaseFailedException;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ServerCtrl extends UnicastRemoteObject implements UserInterface, ProjectInterface, FileInterface, CollabInterface {
    private IPAddress myAddress = new IPAddress("localhost", 9999);
    private Registry registry;
    private String serviceKey = "rmiServer";

    public ServerCtrl() throws RemoteException {}

    public ServerCtrl(int port, String serviceKey) throws RemoteException {
        myAddress.setPort(port);
        this.serviceKey = serviceKey;
    }

    public void start() throws RemoteException {
        try {
            try {
                registry = LocateRegistry.createRegistry(myAddress.getPort());
            } catch(ExportException e) {
                registry = LocateRegistry.getRegistry(myAddress.getPort());
            }
            registry.rebind(serviceKey, this);
            myAddress.setHost(InetAddress.getLocalHost().getHostAddress());
        } catch (RemoteException e){
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() throws RemoteException {
        try {
            if (registry != null) {
                registry.unbind(serviceKey);
                UnicastRemoteObject.unexportObject(this,true);
            }
        } catch (RemoteException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public IPAddress getMyAddress() {
        return myAddress;
    }

    public String getServiceKey() {
        return serviceKey;
    }

    @Override
    public User signIn(User user) throws RemoteException {
        try {
            return new UserDAO().signIn(user);
        } catch (DatabaseFailedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User signUp(User user) throws RemoteException {
        try {
            return new UserDAO().create(user);
        } catch (DatabaseFailedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean updateUser(User user) throws RemoteException {
        try {
            new UserDAO().update(user);
            return true;
        } catch (DatabaseFailedException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<User> getUserByKey(String key) throws RemoteException {
        try {
            return new UserDAO().getByKey(key);
        } catch (DatabaseFailedException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Project createProject(Project project) throws RemoteException {
        try {
            return new ProjectDAO().create(project);
        } catch (DatabaseFailedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Project> getProjectByUser(User user) throws RemoteException {
        try {
            return new ProjectDAO().getByUser(user);
        } catch (DatabaseFailedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Project> getCollabProjectByUser(User user) throws RemoteException {
        try {
            return new ProjectDAO().getCollabByUser(user);
        } catch (DatabaseFailedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Project getProject(Long id) throws RemoteException {
        return new ProjectDAO().get(id);
    }

    @Override
    public Project updateProject(Project project) throws RemoteException {
        try {
            new ProjectDAO().update(project);
            return new ProjectDAO().get(project.getId());
        } catch (DatabaseFailedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean deleteProject(Project project) throws RemoteException {
        try {
            new ProjectDAO().delete(project.getId());
            return true;
        } catch (DatabaseFailedException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public File createFile(File file) throws RemoteException {
        try {
            return new FileDAO().create(file);
        } catch (DatabaseFailedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public File getFile(Long id) throws RemoteException {
        return new FileDAO().get(id);
    }

    @Override
    public Collab createCollab(Collab collab) throws RemoteException {
        try {
            return new CollabDAO().create(collab);
        } catch (DatabaseFailedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Collab> getPendingCollabByUser(User user) throws RemoteException {
        try {
            return new CollabDAO().getPendingByUser(user);
        } catch (DatabaseFailedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Collab getCollab(User user, Project project) throws RemoteException {
        try {
            return new CollabDAO().get(user, project);
        } catch (DatabaseFailedException e) {
            return null;
        }
    }

    @Override
    public boolean acceptCollab(Long id) throws RemoteException {
        try {
            new CollabDAO().acceptCollab(id);
            return true;
        } catch (DatabaseFailedException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean rejectCollab(Long id) throws RemoteException {
        try {
            new CollabDAO().delete(id);
            return true;
        } catch (DatabaseFailedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
