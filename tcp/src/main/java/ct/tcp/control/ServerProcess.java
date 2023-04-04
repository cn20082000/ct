package ct.tcp.control;

import ct.model.*;
import ct.tcp.App;
import ct.tcp.util.FileIO;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ServerProcess extends Thread implements ServerProcessNav {
    private Socket socket;
    private ObjectOutputStream oos;

    private User user;
    private Long fileId;

    public ServerProcess(Socket socket) {
        super();
        this.socket = socket;
    }

    public void sendData(Object obj) {
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User getUser() {
        return user;
    }

    public Long getFileId() {
        return fileId;
    }

    @Override
    public void run() {
        try {
            while (true) {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Object o = ois.readObject();

                if (o instanceof ObjectWrapper) {
                    ObjectWrapper data = (ObjectWrapper) o;

                    switch (data.getLabel()) {
                        case ObjectWrapper.LBL_TESTER -> {
                            switch (data.getPerformative()) {
                                case ObjectWrapper.TESTER -> sendData(replyTester(data));
                            }
                        }
                        case ObjectWrapper.LBL_POST -> {
                            switch (data.getPerformative()) {
                                case ObjectWrapper.SIGN_UP -> sendData(replySignUp(data));
                                case ObjectWrapper.CHANGE_PASSWORD -> sendData(replyChangePassword(data));
                                case ObjectWrapper.SEND_COLLAB -> sendData(replySendCollab(data));
                                case ObjectWrapper.ACCEPT_COLLAB -> sendData(replyAcceptCollab(data));
                                case ObjectWrapper.REJECT_COLLAB -> sendData(replyRejectCollab(data));
                                case ObjectWrapper.CHANGE_TEXT -> sendData(replyChangeText(data));
                                case ObjectWrapper.CLOSE_PROJECT -> sendData(replyCloseProject(data));
                                case ObjectWrapper.SIGN_OUT -> sendData(replySignOut(data));
                                case ObjectWrapper.CREATE_FILE -> sendData(replyCreateFile(data));
                            }
                        }
                        case ObjectWrapper.LBL_GET -> {
                            switch (data.getPerformative()) {
                                case ObjectWrapper.SIGN_IN -> sendData(replySignIn(data));
                                case ObjectWrapper.CREATE_PROJECT -> sendData(replyCreateProject(data));
                                case ObjectWrapper.LIST_PROJECT -> sendData(replyListProject(data));
                                case ObjectWrapper.COLLAB_PROJECT -> sendData(replyCollabProject(data));
                                case ObjectWrapper.OPEN_PROJECT -> sendData(replyOpenProject(data));
                                case ObjectWrapper.LIST_COLLAB -> sendData(replyListCollab(data));
                                case ObjectWrapper.SEARCH_USER -> sendData(replySearchUser(data));
                                case ObjectWrapper.OPEN_FILE -> sendData(replyOpenFile(data));
                                case ObjectWrapper.RUN_PROJECT -> sendData(replyRunProject(data));
                                case ObjectWrapper.DOWNLOAD_FILE -> sendData(replyDownloadFile(data));
                            }
                        }
                    }

                }
            }
        } catch (EOFException | SocketException e) {
            ServerCtrl.myProcess.remove(this);
            App.room.leaveRoom(this);
            try {
                socket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            this.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ObjectWrapper replyTester(ObjectWrapper data) {
        return new ObjectWrapper.Builder()
                .setLabel(ObjectWrapper.LBL_TESTER)
                .setPerformative(ObjectWrapper.TESTER)
                .build();
    }

    @Override
    public ObjectWrapper replySignIn(ObjectWrapper data) {
        User in = (User)data.getData();
        if (in.getUsername() == null || in.getUsername().isBlank()
                || in.getPassword() == null || in.getPassword().isBlank()) {
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_FAILED)
                    .setPerformative(ObjectWrapper.SIGN_IN)
                    .setData("Incorrect username or password.")
                    .build();
        }
        try {
            User out = App.client.remoteSignIn(in);
            if (out == null) {
                return new ObjectWrapper.Builder()
                        .setLabel(ObjectWrapper.LBL_FAILED)
                        .setPerformative(ObjectWrapper.SIGN_IN)
                        .setData("Incorrect username or password.")
                        .build();
            }
            this.user = out.clone();
            out.setId(0L);
            out.setPassword(null);
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_SUCCESS)
                    .setPerformative(ObjectWrapper.SIGN_IN)
                    .setData(out.clone())
                    .build();
        } catch (Exception e) {
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_FAILED)
                    .setPerformative(ObjectWrapper.SIGN_IN)
                    .setData("Server error, try again another time.")
                    .build();
        }
    }

    @Override
    public ObjectWrapper replySignUp(ObjectWrapper data) {
        User in = (User)data.getData();
        if (in.getName() == null || in.getName().isBlank()
                || in.getUsername() == null || in.getUsername().isBlank()
                || in.getPassword() == null || in.getPassword().isBlank()) {
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_FAILED)
                    .setPerformative(ObjectWrapper.SIGN_UP)
                    .setData("Name, username or password must not be empty.")
                    .build();
        }
        try {
            User out = App.client.remoteSignUp(in);
            if (out == null) {
                return new ObjectWrapper.Builder()
                        .setLabel(ObjectWrapper.LBL_FAILED)
                        .setPerformative(ObjectWrapper.SIGN_UP)
                        .setData("Username already used.")
                        .build();
            }
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_SUCCESS)
                    .setPerformative(ObjectWrapper.SIGN_UP)
                    .build();
        } catch (Exception e) {
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_FAILED)
                    .setPerformative(ObjectWrapper.SIGN_UP)
                    .setData("Server error, try again another time.")
                    .build();
        }
    }

    @Override
    public ObjectWrapper replyChangePassword(ObjectWrapper data) {
        User in = user.clone();
        in.setPassword(((User) data.getData()).getPassword());
        if (in.getPassword() == null || in.getPassword().isBlank()) {
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_FAILED)
                    .setPerformative(ObjectWrapper.CHANGE_PASSWORD)
                    .setData("Password must not be empty.")
                    .build();
        }
        try {
            boolean out = App.client.remoteUpdateUser(in);
            if (!out) {
                return new ObjectWrapper.Builder()
                        .setLabel(ObjectWrapper.LBL_FAILED)
                        .setPerformative(ObjectWrapper.CHANGE_PASSWORD)
                        .setData("Password change failed.")
                        .build();
            }
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_SUCCESS)
                    .setPerformative(ObjectWrapper.CHANGE_PASSWORD)
                    .build();
        } catch (Exception e) {
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_FAILED)
                    .setPerformative(ObjectWrapper.CHANGE_PASSWORD)
                    .setData("Server error, try again another time.")
                    .build();
        }
    }

    @Override
    public ObjectWrapper replySignOut(ObjectWrapper data) {
        App.room.leaveRoom(this);
        user = null;
        fileId = null;
        return new ObjectWrapper.Builder()
                .setLabel(ObjectWrapper.LBL_SUCCESS)
                .setPerformative(ObjectWrapper.SIGN_OUT)
                .build();
    }

    @Override
    public ObjectWrapper replyCreateProject(ObjectWrapper data) {
        String name = (String) data.getData();
        if (name == null || name.isBlank()) {
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_FAILED)
                    .setPerformative(ObjectWrapper.CREATE_PROJECT)
                    .setData("Name of project must not be empty.")
                    .build();
        }
        Folder fo = new Folder(
                null,
                new Info(
                        null,
                        "src",
                        LocalDateTime.now(),
                        UUID.randomUUID().toString()),
                null,
                null,
                null);
        fo.setFiles(List.of(
                new File(
                        null,
                        new Info(
                                null,
                                "Main.java",
                                LocalDateTime.now(),
                                UUID.randomUUID().toString()),
                        fo,
                        name + UUID.randomUUID().toString() + "\\src\\Main.java"
                )
        ));
        Project in = new Project(
                null,
                new Info(
                        null,
                        name,
                        LocalDateTime.now(),
                        UUID.randomUUID().toString()),
                user.clone(),
                fo,
                null);
        try {
            Project out = App.client.remoteCreateProject(in);
            if (out == null) {
                return new ObjectWrapper.Builder()
                        .setLabel(ObjectWrapper.LBL_FAILED)
                        .setPerformative(ObjectWrapper.CREATE_PROJECT)
                        .setData("Create project failed, please try again.")
                        .build();
            }
            FileIO.initProject(out);
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_SUCCESS)
                    .setPerformative(ObjectWrapper.CREATE_PROJECT)
                    .setData(out)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_FAILED)
                    .setPerformative(ObjectWrapper.CREATE_PROJECT)
                    .setData("Server error, try again another time.")
                    .build();
        }
    }

    @Override
    public ObjectWrapper replyListProject(ObjectWrapper data) {
        User in = user.clone();
        try {
            List<Project> out = App.client.remoteGetProjectByUser(in);
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_SUCCESS)
                    .setPerformative(ObjectWrapper.LIST_PROJECT)
                    .setData(out)
                    .build();
        } catch (Exception e) {
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_FAILED)
                    .setPerformative(ObjectWrapper.LIST_PROJECT)
                    .setData("Server error, try again another time.")
                    .build();
        }
    }

    @Override
    public ObjectWrapper replyCollabProject(ObjectWrapper data) {
        User in = user.clone();
        try {
            List<Project> out = App.client.remoteGetCollabProjectByUser(in);
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_SUCCESS)
                    .setPerformative(ObjectWrapper.COLLAB_PROJECT)
                    .setData(out)
                    .build();
        } catch (Exception e) {
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_FAILED)
                    .setPerformative(ObjectWrapper.COLLAB_PROJECT)
                    .setData("Server error, try again another time.")
                    .build();
        }
    }

    @Override
    public ObjectWrapper replyOpenProject(ObjectWrapper data) {
        Project project = (Project) data.getData();
        if (project.getId() == null || project.getId() <= 0) {
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_FAILED)
                    .setPerformative(ObjectWrapper.OPEN_PROJECT)
                    .setData("Project dose not exist.")
                    .build();
        }
        try {
            project = App.client.remoteGetProject(project.getId());
            if (project == null) {
                return new ObjectWrapper.Builder()
                        .setLabel(ObjectWrapper.LBL_FAILED)
                        .setPerformative(ObjectWrapper.OPEN_PROJECT)
                        .setData("Project dose not exist.")
                        .build();
            }
            App.room.joinRoom(this, project);
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_SUCCESS)
                    .setPerformative(ObjectWrapper.OPEN_PROJECT)
                    .setData(project)
                    .build();
        } catch (Exception e) {
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_FAILED)
                    .setPerformative(ObjectWrapper.OPEN_PROJECT)
                    .setData("Server error, try again another time.")
                    .build();
        }
    }

    @Override
    public void syncJoinRoom(Object data) {
        sendData(new ObjectWrapper.Builder()
                .setLabel(ObjectWrapper.LBL_SYNC)
                .setPerformative(ObjectWrapper.JOIN_ROOM)
                .setData(data)
                .build());
    }

    @Override
    public ObjectWrapper replyCloseProject(ObjectWrapper data) {
        App.room.leaveRoom(this);
        fileId = null;
        return new ObjectWrapper.Builder()
                .setLabel(ObjectWrapper.LBL_SUCCESS)
                .setPerformative(ObjectWrapper.CLOSE_PROJECT)
                .build();
    }

    @Override
    public void syncUpdateProject(Object data) {
        sendData(new ObjectWrapper.Builder()
                .setLabel(ObjectWrapper.LBL_SYNC)
                .setPerformative(ObjectWrapper.UPDATE_PROJECT)
                .setData(data)
                .build());
    }

    @Override
    public ObjectWrapper replySendCollab(ObjectWrapper data) {
        Collab in = (Collab) data.getData();
        in.setAccept(false);
        if (in.getFromProject().getId() == null || in.getFromProject().getId() <= 0
                || in.getToUser().getId() == null || in.getToUser().getId() <= 0) {
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_FAILED)
                    .setPerformative(ObjectWrapper.SEND_COLLAB)
                    .setData("Project or user dose not exist.")
                    .build();
        } else if (Objects.equals(in.getToUser().getId(), user.getId())) {
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_FAILED)
                    .setPerformative(ObjectWrapper.SEND_COLLAB)
                    .setData("You cannot send invitation to myself.")
                    .build();
        }
        try {
            Collab check = App.client.remoteGetCollab(in.getToUser(), in.getFromProject());
            if (check != null) {
                return new ObjectWrapper.Builder()
                        .setLabel(ObjectWrapper.LBL_FAILED)
                        .setPerformative(ObjectWrapper.SEND_COLLAB)
                        .setData("You have sent an invitation to this user before.")
                        .build();
            }
            Collab out = App.client.remoteCreateCollab(in);
            if (out == null) {
                return new ObjectWrapper.Builder()
                        .setLabel(ObjectWrapper.LBL_FAILED)
                        .setPerformative(ObjectWrapper.SEND_COLLAB)
                        .setData("Project or user dose not exist.")
                        .build();
            }
            for (ServerProcess p : ServerCtrl.getMyProcess()) {
                if (Objects.equals(p.getUser().getId(), out.getToUser().getId())) {
                    p.syncReceiveCollab(out);
                }
            }
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_SUCCESS)
                    .setPerformative(ObjectWrapper.SEND_COLLAB)
                    .build();

        } catch (Exception e) {
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_FAILED)
                    .setPerformative(ObjectWrapper.SEND_COLLAB)
                    .setData("Server error, try again another time.")
                    .build();
        }
    }

    @Override
    public ObjectWrapper replyListCollab(ObjectWrapper data) {
        try {
            List<Collab> out = App.client.remoteGetPendingCollabByUser(user);
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_SUCCESS)
                    .setPerformative(ObjectWrapper.LIST_COLLAB)
                    .setData(out)
                    .build();
        } catch (Exception e) {
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_FAILED)
                    .setPerformative(ObjectWrapper.LIST_COLLAB)
                    .setData("Server error, try again another time.")
                    .build();
        }
    }

    @Override
    public ObjectWrapper replyAcceptCollab(ObjectWrapper data) {
        Collab in  = (Collab) data.getData();
        if (in.getId() == null || in.getId() <= 0) {
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_FAILED)
                    .setPerformative(ObjectWrapper.ACCEPT_COLLAB)
                    .setData("Invitation does not exist.")
                    .build();
        }
        try {
            boolean out = App.client.remoteAcceptCollab(in.getId());
            if (!out) {
                return new ObjectWrapper.Builder()
                        .setLabel(ObjectWrapper.LBL_FAILED)
                        .setPerformative(ObjectWrapper.ACCEPT_COLLAB)
                        .setData("Invitation does not exist.")
                        .build();
            }
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_SUCCESS)
                    .setPerformative(ObjectWrapper.ACCEPT_COLLAB)
                    .build();
        } catch (Exception e) {
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_FAILED)
                    .setPerformative(ObjectWrapper.ACCEPT_COLLAB)
                    .setData("Server error, try again another time.")
                    .build();
        }
    }

    @Override
    public ObjectWrapper replyRejectCollab(ObjectWrapper data) {
        Collab in  = (Collab) data.getData();
        if (in.getId() == null || in.getId() <= 0) {
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_FAILED)
                    .setPerformative(ObjectWrapper.REJECT_COLLAB)
                    .setData("Invitation does not exist.")
                    .build();
        }
        try {
            boolean out = App.client.remoteRejectCollab(in.getId());
            if (!out) {
                return new ObjectWrapper.Builder()
                        .setLabel(ObjectWrapper.LBL_FAILED)
                        .setPerformative(ObjectWrapper.REJECT_COLLAB)
                        .setData("Invitation does not exist.")
                        .build();
            }
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_SUCCESS)
                    .setPerformative(ObjectWrapper.REJECT_COLLAB)
                    .build();
        } catch (Exception e) {
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_FAILED)
                    .setPerformative(ObjectWrapper.REJECT_COLLAB)
                    .setData("Server error, try again another time.")
                    .build();
        }
    }

    @Override
    public void syncReceiveCollab(Object data) {
        sendData(new ObjectWrapper.Builder()
                .setLabel(ObjectWrapper.LBL_SYNC)
                .setPerformative(ObjectWrapper.RECEIVE_COLLAB)
                .setData(data)
                .build());
    }

    @Override
    public ObjectWrapper replySearchUser(ObjectWrapper data) {
        String key = (String) data.getData();
        if (key == null || key.isBlank()) {
            key = "";
        }
        try {
            List<User> out = App.client.remoteGetUserByKey(key);
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_SUCCESS)
                    .setPerformative(ObjectWrapper.SEARCH_USER)
                    .setData(out)
                    .build();
        } catch (Exception e) {
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_FAILED)
                    .setPerformative(ObjectWrapper.SEARCH_USER)
                    .setData("Server error, try again another time.")
                    .build();
        }
    }

    @Override
    public ObjectWrapper replyOpenFile(ObjectWrapper data) {
        File in = (File) data.getData();
        if (in == null) {
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_FAILED)
                    .setPerformative(ObjectWrapper.OPEN_FILE)
                    .setData("File does not exist.")
                    .build();
        }
        fileId = in.getId();
        try {
            String out = App.room.openFile(this, in);
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_SUCCESS)
                    .setPerformative(ObjectWrapper.OPEN_FILE)
                    .setData(out)
                    .build();
        } catch (Exception e) {
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_FAILED)
                    .setPerformative(ObjectWrapper.OPEN_FILE)
                    .setData("Server error, try again another time.")
                    .build();
        }
    }

    @Override
    public void syncChangedText(Object data) {
        sendData(new ObjectWrapper.Builder()
                .setLabel(ObjectWrapper.LBL_SYNC)
                .setPerformative(ObjectWrapper.CHANGED_TEXT)
                .setData(data)
                .build());
    }

    @Override
    public ObjectWrapper replyChangeText(ObjectWrapper data) {
        ChangedText in = (ChangedText) data.getData();
        try {
            App.room.changeText(this, in);
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_SUCCESS)
                    .setPerformative(ObjectWrapper.CHANGE_TEXT)
                    .build();
        } catch (Exception e) {
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_FAILED)
                    .setPerformative(ObjectWrapper.CHANGE_TEXT)
                    .setData("Server error, try again another time.")
                    .build();
        }
    }

    @Override
    public ObjectWrapper replyRunProject(ObjectWrapper data) {
        String in = (String) data.getData();
        if (in == null) {
            in = "";
        }
        try {
            String out = App.room.runProject(this, in);
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_SUCCESS)
                    .setPerformative(ObjectWrapper.RUN_PROJECT)
                    .setData(out)
                    .build();
        } catch (Exception e) {
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_FAILED)
                    .setPerformative(ObjectWrapper.RUN_PROJECT)
                    .setData("Server error, try again another time.")
                    .build();
        }
    }

    @Override
    public ObjectWrapper replyCreateFile(ObjectWrapper data) {
        File in = (File) data.getData();
        try {
            File out = App.client.remoteCreateFile(in);
            if (out == null) {
                return new ObjectWrapper.Builder()
                        .setLabel(ObjectWrapper.LBL_FAILED)
                        .setPerformative(ObjectWrapper.CREATE_FILE)
                        .setData("Cannot create file, please try again.")
                        .build();
            }
            App.room.createNewFile(this, out);
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_SUCCESS)
                    .setPerformative(ObjectWrapper.CREATE_FILE)
                    .build();
        } catch (Exception e) {
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_FAILED)
                    .setPerformative(ObjectWrapper.CREATE_FILE)
                    .setData("Server error, try again another time.")
                    .build();
        }
    }

    @Override
    public ObjectWrapper replyDownloadFile(ObjectWrapper data) {
        File in = (File) data.getData();
        if (in == null) {
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_FAILED)
                    .setPerformative(ObjectWrapper.DOWNLOAD_FILE)
                    .setData("File does not exist.")
                    .build();
        }
        try {
            in = App.client.remoteGetFile(in.getId());
            String out = FileIO.getDataFrom(in);
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_SUCCESS)
                    .setPerformative(ObjectWrapper.DOWNLOAD_FILE)
                    .setData(out)
                    .build();
        } catch (Exception e) {
            return new ObjectWrapper.Builder()
                    .setLabel(ObjectWrapper.LBL_FAILED)
                    .setPerformative(ObjectWrapper.DOWNLOAD_FILE)
                    .setData("Server error, try again another time.")
                    .build();
        }
    }
}
