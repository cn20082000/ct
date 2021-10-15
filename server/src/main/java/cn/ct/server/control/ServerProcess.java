/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ct.server.control;

import cn.ct.model.Collab;
import cn.ct.model.Folder;
import cn.ct.model.Info;
import cn.ct.model.ObjectWrapper;
import cn.ct.model.Project;
import cn.ct.model.User;
import cn.ct.server.dao.CollabDAO;
import cn.ct.server.dao.ProjectDAO;
import cn.ct.server.dao.UserDAO;
import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cn200
 */
public class ServerProcess extends Thread implements ServerProcessNav {
    private Socket mySocket;
    private User user;
    private Project project;
    private ObjectOutputStream oos;

    public ServerProcess(Socket s) {
        super();
        mySocket = s;
    }

    public void sendData(Object obj) {
        try {
            oos = new ObjectOutputStream(mySocket.getOutputStream());
            oos.writeObject(obj);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public User getUser() {
        return user;
    }
    
    public Project getProject() {
        return project;
    }

    public void run() { 
        try {
            while(true) {
                ObjectInputStream ois = new ObjectInputStream(mySocket.getInputStream());
                Object o = ois.readObject();
                
                if(o instanceof ObjectWrapper){
                    ObjectWrapper data = (ObjectWrapper)o;

                    switch(data.getPerformative()) {
                        case ObjectWrapper.GET_SIGNIN:
                            sendData(resSignIn(data));
                            break;
                        case ObjectWrapper.GET_SIGNUP:
                            sendData(resSignUp(data));
                            break;
                        case ObjectWrapper.GET_UPDATE:
                            sendData(resUpdate(data));
                            break;
                        case ObjectWrapper.GET_CREATE_PROJECT:
                            sendData(resCreateProject(data));
                            break;
                        case ObjectWrapper.GET_LIST_PROJECT:
                            sendData(resListProject(data));
                            break;
                        case ObjectWrapper.GET_OPEN_PROJECT:
                            sendData(resOpenProject(data));
                            break;
                        case ObjectWrapper.GET_CLOSE_PROJECT:
                            sendData(resCloseProject(data));
                            break;
                        case ObjectWrapper.GET_COLLAB_PROJECT:
                            sendData(resCollabProject(data));
                            break;
                        case ObjectWrapper.GET_SEND_COLLAB:
                            sendData(resSendCollab(data));
                            break;
                        case ObjectWrapper.GET_LIST_INVITE:
                            sendData(resListCollab(data));
                            break;
                        case ObjectWrapper.GET_ACCEPT_COLLAB:
                            sendData(resAcceptCollab(data));
                            break;
                        case ObjectWrapper.GET_REJECT_COLLAB:
                            sendData(resRejectCollab(data));
                            break;
                        case 9999:
                            sendData(new ObjectWrapper(9999, null));
                            break;
                    }

                }
            }
        } catch (EOFException | SocketException e) { 
            ServerCtrl.myProcess.remove(this);
            sendLiOnline(null);
            try {
                mySocket.close();
            }catch(Exception ex) {
                ex.printStackTrace();
            }
            this.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ObjectWrapper resSignIn(ObjectWrapper data) {
        User u = (User)data.getData();
        UserDAO ud = new UserDAO();
        u = ud.signIn(u);
        if (u.getId() >= 0){
            user = u;
            return new ObjectWrapper(ObjectWrapper.REPLY_SIGNIN,
                    ObjectWrapper.MES_SUCCESS, 
                    new User(0, user.getName(), user.getUsername(), ""));
        } else {
            return new ObjectWrapper(ObjectWrapper.REPLY_SIGNIN, 
                    ObjectWrapper.MES_FAILED, null);
        }
    }

    @Override
    public ObjectWrapper resSignUp(ObjectWrapper data) {
        User u = (User)data.getData();
        UserDAO ud = new UserDAO();
        if(ud.signUp(u)){
            return new ObjectWrapper(ObjectWrapper.REPLY_SIGNUP, 
                    ObjectWrapper.MES_SUCCESS, null);
        } else {
            return new ObjectWrapper(ObjectWrapper.REPLY_SIGNUP, 
                    ObjectWrapper.MES_FAILED, null);
        }
    }

    @Override
    public ObjectWrapper resUpdate(ObjectWrapper data) {
        User u = (User)data.getData();
        UserDAO ud = new UserDAO();
        u.setId(user.getId());
        if(ud.update(u)){
            user = u;
            return new ObjectWrapper(ObjectWrapper.REPLY_UPDATE, 
                    ObjectWrapper.MES_SUCCESS, 
                    new User(0, user.getName(), user.getUsername(), ""));
        } else {
            return new ObjectWrapper(ObjectWrapper.REPLY_UPDATE, 
                    ObjectWrapper.MES_FAILED, null);
        }
    }

    @Override
    public ObjectWrapper resCreateProject(ObjectWrapper data) {
        Project p = (Project)data.getData();
        p.setHost(user);
        p.setRootFolder(new Folder(-1, null, null, new Info(-1, "src", null, null)));
        if (new ProjectDAO().createProject(p).getId() >= 0) {
            return new ObjectWrapper(ObjectWrapper.REPLY_CREATE_PROJECT, 
                    ObjectWrapper.MES_SUCCESS, null);
        } else {
            return new ObjectWrapper(ObjectWrapper.REPLY_CREATE_PROJECT, 
                    ObjectWrapper.MES_FAILED, null);
        }
    }

    @Override
    public ObjectWrapper resListProject(ObjectWrapper data) {
        return new ObjectWrapper(ObjectWrapper.REPLY_LIST_PROJECT, 
                ObjectWrapper.MES_SUCCESS, new ProjectDAO().getProject(user));
    }

    @Override
    public ObjectWrapper resOpenProject(ObjectWrapper data) {
        Project p = (Project) data.getData();
        Project proj = new ProjectDAO().getProject(p.getId());
        if (proj.getId() >= 0) {
            project = proj;
            sendLiCollab(new ObjectWrapper(0, project));
            sendLiOnline(null);
            return new ObjectWrapper(ObjectWrapper.REPLY_OPEN_PROJECT, 
                ObjectWrapper.MES_SUCCESS, proj);
        } else {
            project = null;
            return new ObjectWrapper(ObjectWrapper.REPLY_OPEN_PROJECT, 
                ObjectWrapper.MES_FAILED, null);
        }
    }

    @Override
    public ObjectWrapper resCloseProject(ObjectWrapper data) {
        sendLiOnline(null);
        project = null;
        return new ObjectWrapper(ObjectWrapper.REPLY_CLOSE_PROJECT, 
            ObjectWrapper.MES_SUCCESS, null);
    }

    @Override
    public ObjectWrapper resSendCollab(ObjectWrapper data) {
        Collab c = (Collab) data.getData();
        c = new CollabDAO().createCollab(c);
        if (c.getId() >= 0) {
            sendReCollab(new ObjectWrapper(ObjectWrapper.ONL_RECEIVE_COLLAB, 
                    ObjectWrapper.MES_SUCCESS, c));
            return new ObjectWrapper(ObjectWrapper.REPLY_SEND_COLLAB, 
                    ObjectWrapper.MES_SUCCESS, null);
        } else {
            return new ObjectWrapper(ObjectWrapper.REPLY_SEND_COLLAB, 
                    ObjectWrapper.MES_FAILED, null);
        }
    }

    @Override
    public void sendReCollab(ObjectWrapper data) {
        Collab c = (Collab) data.getData();
        for (ServerProcess proc : ServerCtrl.myProcess) {
            if (proc.getUser().getId() == c.getToUser().getId()) {
                proc.sendData(data);
            }
        }
    }

    @Override
    public ObjectWrapper resAcceptCollab(ObjectWrapper data) {
        Collab c = (Collab) data.getData();
        c.setStatus(true);
        c = new CollabDAO().updateCollab(c);
        if (c.getId() >= 0) {
            sendLiCollab(new ObjectWrapper(0, c.getFromProject()));
            return new ObjectWrapper(ObjectWrapper.REPLY_ACCEPT_COLLAB, 
                    ObjectWrapper.MES_SUCCESS, c);
        } else {
            return new ObjectWrapper(ObjectWrapper.REPLY_ACCEPT_COLLAB, 
                    ObjectWrapper.MES_FAILED, null);
        }
    }

    @Override
    public ObjectWrapper resRejectCollab(ObjectWrapper data) {
        Collab c = (Collab) data.getData();
        boolean result = new CollabDAO().deleteCollab(c);
        if (result) {
            return new ObjectWrapper(ObjectWrapper.REPLY_REJECT_COLLAB, 
                    ObjectWrapper.MES_SUCCESS, null);
        } else {
            return new ObjectWrapper(ObjectWrapper.REPLY_REJECT_COLLAB, 
                    ObjectWrapper.MES_FAILED, null);
        }
    }

    @Override
    public void sendLiCollab(ObjectWrapper data) {
        Project p = (Project) data.getData();
        List<Collab> cs = new CollabDAO().getCollab(p);
        cs.add(new Collab(0, p.getHost(), null, false));
        for (ServerProcess proc : ServerCtrl.myProcess) {
            if (proc.getProject() != null && proc.getProject().getId() == p.getId()) {
                proc.sendData(new ObjectWrapper(ObjectWrapper.ONL_LIST_COLLAB, 
                        ObjectWrapper.MES_SUCCESS, cs));
            }
        }
    }

    @Override
    public void sendLiOnline(ObjectWrapper data) {
        List<User> us = new ArrayList<>();
        for (ServerProcess proc : ServerCtrl.myProcess) {
            if (proc.getProject() != null && proc.getProject().getId() == project.getId()) {
                us.add(proc.getUser());
            }
        }
        for (ServerProcess proc : ServerCtrl.myProcess) {
            if (proc.getProject() != null && proc.getProject().getId() == project.getId()) {
                proc.sendData(new ObjectWrapper(ObjectWrapper.ONL_LIST_ONLINE, 
                        ObjectWrapper.MES_SUCCESS, us));
            }
        }
    }

    @Override
    public ObjectWrapper resCollabProject(ObjectWrapper data) {
        List<Collab> cs = new CollabDAO().getCollab(user, true);
        List<Project> ps = new ArrayList<>();
        for (Collab c : cs) {
            ps.add(c.getFromProject());
        }
        return new ObjectWrapper(ObjectWrapper.REPLY_COLLAB_PROJECT, 
                ObjectWrapper.MES_SUCCESS, ps);
    }

    @Override
    public ObjectWrapper resListCollab(ObjectWrapper data) {
        return new ObjectWrapper(ObjectWrapper.REPLY_LIST_INVITE, 
                ObjectWrapper.MES_SUCCESS, new CollabDAO().getCollab(user, false));
    }
}