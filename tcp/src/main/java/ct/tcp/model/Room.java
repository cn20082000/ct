package ct.tcp.model;

import ct.model.*;
import ct.tcp.App;
import ct.tcp.control.ServerProcess;
import ct.tcp.util.FileIO;
import ct.tcp.util.SomeFunc;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Room {
    private Project project;
    private List<ServerProcess> members = new ArrayList<>();
    private List<OpeningFile> openingFiles = new ArrayList<>();

    public Room(Project project, ServerProcess first) {
        this.project = project;
        members.add(first);
        first.syncJoinRoom(List.of(first.getUser()));
    }

    public void joinRoom(ServerProcess process) {
        members.add(process);
        List<User> users = new ArrayList<>();
        for (ServerProcess p : members) {
            users.add(p.getUser());
        }
        for (ServerProcess p : members) {
            p.syncJoinRoom(users);
        }
    }

    public Long roomId() {
        return project.getId();
    }

    public String openFile(ServerProcess process, File file) throws IOException {
        if (members.contains(process)) {
            for (OpeningFile op : openingFiles) {
                if (Objects.equals(op.getFile().getId(), file.getId())) {
                    return op.getText().getText();
                }
            }

            String out = FileIO.getDataFrom(file);

            openingFiles.add(new OpeningFile(file, out));

            return out;
        }
        return null;
    }

    public void changeText(ServerProcess process, ChangedText ct) {
        if (members.contains(process)) {
            for (OpeningFile op : openingFiles) {
                if (Objects.equals(op.getFile().getId(), process.getFileId())) {
                    if (ct.getType() == ChangedText.INSERT) {
                        try {
                            op.guid = ct.getGuid();
                            op.text.insert(ct.getStr(), op.text.getLineStartOffset(ct.getLine()) + ct.getOffset());
                        } catch (BadLocationException ex) {
                            ex.printStackTrace();
                        }
                    }
                    if (ct.getType() == ChangedText.REMOVE) {
                        try {
                            op.guid = ct.getGuid();
                            op.text.replaceRange("", op.text.getLineStartOffset(ct.getLine()) + ct.getOffset(),
                                    op.text.getLineStartOffset(ct.getLine()) + ct.getOffset() + ct.getLength());
                        } catch (BadLocationException ex) {
                            ex.printStackTrace();
                        }
                    }
                    return;
                }
            }
        }
    }

    public String runProject(ServerProcess process, String input) throws IOException {
        if (members.contains(process)) {
            FileIO.writeToInput(project, input);
            for (OpeningFile op : openingFiles) {
                FileIO.writeToFile(op.file, op.text.getText());
            }
            return FileIO.runProject(project);
        }
        return null;
    }

    public boolean leaveRoom(ServerProcess process) throws IOException {
        if (members.contains(process)) {
            members.remove(process);
            List<User> users = new ArrayList<>();
            for (ServerProcess p : members) {
                users.add(p.getUser());
            }
            for (ServerProcess p : members) {
                p.syncJoinRoom(users);
            }
            if (members.size() == 0) {
                for (OpeningFile op : openingFiles) {
                    FileIO.writeToFile(op.file, op.text.getText());
                }
                return false;
            }
        }
        return true;
    }

    public void createFile(ServerProcess process, File file) throws IOException {
        if (members.contains(process)) {
            project = App.client.remoteGetProject(project.getId());
            FileIO.createNewClass(project, file);
            for (ServerProcess p : members) {
                p.syncUpdateProject(project);
            }
        }
    }

    class OpeningFile {
        private File file;
        private JTextArea text;
        private String guid;

        public OpeningFile(File f, String str) {
            this.file = f;
            this.guid = "";
            this.text = new JTextArea();
            this.text.setText(str);
            this.text.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    try {
                        int line = SomeFunc.getLineOfOffset(text, e.getOffset());
                        ChangedText ct = new ChangedText(guid, line,
                                e.getOffset() - SomeFunc.getLineStartOffset(text, line),
                                e.getLength(), ChangedText.INSERT,
                                text.getText(e.getOffset(), e.getLength()));
                        for (ServerProcess process : members) {
                            if (Objects.equals(process.getFileId(), file.getId())) {
                                process.syncChangedText(ct);
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    try {
                        int line = SomeFunc.getLineOfOffset(text, e.getOffset());
                        ChangedText ct = new ChangedText(guid, line,
                                e.getOffset() - SomeFunc.getLineStartOffset(text, line),
                                e.getLength(), ChangedText.REMOVE, "");
                        for (ServerProcess process : members) {
                            if (Objects.equals(process.getFileId(), file.getId())) {
                                process.syncChangedText(ct);
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void changedUpdate(DocumentEvent e) {

                }
            });
        }

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public JTextArea getText() {
            return text;
        }

        public void setText(JTextArea text) {
            this.text = text;
        }
    }
}
