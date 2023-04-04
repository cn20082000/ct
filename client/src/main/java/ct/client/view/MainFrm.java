package ct.client.view;

import ct.client.App;
import ct.client.util.FileTreeMouseAdapter;
import ct.client.util.SomeFunc;
import ct.client.view.collab.ListCollabDialog;
import ct.client.view.collab.SendCollabDialog;
import ct.client.view.general.ConnectionDialog;
import ct.client.view.general.SignInDialog;
import ct.client.view.general.SignUpDialog;
import ct.client.view.general.UserInfoDialog;
import ct.client.view.project.CreateProjectDialog;
import ct.client.view.project.DownloadProjectDialog;
import ct.client.view.project.ListProjectDialog;
import ct.model.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainFrm extends JFrame {
    private JButton btnRun;
    private JPanel panel;
    private JTree tree;
    private JLabel tvOnline;
    private JList listOnline;
    private JLabel tvNoti;
    private JTextArea ta;
    private JScrollPane sp;

    private JMenuBar menuBar = new JMenuBar();

    private JMenu generalMenu = new JMenu();
    private JMenuItem connectionItem, userInfoItem, signInItem, signUpItem, signOutItem;

    private JMenu projectMenu = new JMenu();
    private JMenuItem createItem, listItem, collabItem, downloadItem, closeItem;

    private JMenu collabMenu = new JMenu();
    private JMenuItem inviteItem, collabInviteItem;

    private DefaultMutableTreeNode root = new DefaultMutableTreeNode("No project is open...");
    private DefaultTreeModel model;
//    private boolean isChangeOfMyself = false;
    private boolean isChangeOfOther = false;
    private String guid = UUID.randomUUID().toString();

    public MainFrm() {
        this.setContentPane(panel);
        this.setSize(1200, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Code together");
        setMenuBar();
        this.setJMenuBar(menuBar);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addMouseListener(new FileTreeMouseAdapter(this));
        model = (DefaultTreeModel) tree.getModel();
        model.setRoot(root);
        model.reload();
        setTa();
        setButton();
    }

    private void setMenuBar() {
        // general menu
        generalMenu.setText("General");

        connectionItem = new JMenuItem("Connection");
        userInfoItem = new JMenuItem("User info");
        signInItem = new JMenuItem("Sign in");
        signUpItem = new JMenuItem("Sign up");
        signOutItem = new JMenuItem("Sign out");

        userInfoItem.setEnabled(false);
        signInItem.setEnabled(false);
        signUpItem.setEnabled(false);
        signOutItem.setEnabled(false);

        generalMenu.add(connectionItem);
        generalMenu.add(userInfoItem);
        generalMenu.add(signInItem);
        generalMenu.add(signUpItem);
        generalMenu.add(signOutItem);

        connectionItem.addActionListener(e -> {
            ConnectionDialog dialog = new ConnectionDialog();
            dialog.pack();
            dialog.setVisible(true);
            updateView();

            if (App.isConnect) {
                registerEvent();
            }
        });
        userInfoItem.addActionListener(e -> {
            UserInfoDialog dialog = new UserInfoDialog();
            dialog.pack();
            dialog.setVisible(true);
        });
        signInItem.addActionListener(e -> {
            SignInDialog dialog = new SignInDialog();
            dialog.pack();
            dialog.setVisible(true);

            updateView();
        });
        signUpItem.addActionListener(e -> {
            SignUpDialog dialog = new SignUpDialog();
            dialog.pack();
            dialog.setVisible(true);
        });
        signOutItem.addActionListener(e -> {
            try {
                App.client.sendData(new ObjectWrapper.Builder()
                        .setLabel(ObjectWrapper.LBL_POST)
                        .setPerformative(ObjectWrapper.SIGN_OUT)
                        .build());

                App.user = null;
                App.isSignIn = false;
                App.project = null;
                App.isOpen = false;
                App.isHost = false;
                ta.setText("");
                ta.setEditable(false);
                updateView();
                updateTree();

                DefaultListModel<String> listModel = new DefaultListModel<>();
                tvOnline.setText("Online(0)");
                listOnline.setModel(listModel);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Cannot send data to server.",
                        "Message", JOptionPane.ERROR_MESSAGE);
            }
        });

        // project menu
        projectMenu.setText("Project");
        projectMenu.setEnabled(false);

        createItem = new JMenuItem("Create new project");
        listItem = new JMenuItem("Open project");
        collabItem = new JMenuItem("Open collab project");
        downloadItem = new JMenuItem("Download project");
        closeItem = new JMenuItem("Close project");

        createItem.setEnabled(true);
        listItem.setEnabled(true);
        collabItem.setEnabled(true);
        downloadItem.setEnabled(false);
        closeItem.setEnabled(false);

        projectMenu.add(createItem);
        projectMenu.add(listItem);
        projectMenu.add(collabItem);
        projectMenu.add(downloadItem);
        projectMenu.add(closeItem);

        createItem.addActionListener(e -> {
            CreateProjectDialog dialog = new CreateProjectDialog();
            dialog.pack();
            dialog.setVisible(true);
        });
        listItem.addActionListener(e -> {
            ListProjectDialog dialog = new ListProjectDialog( true);
            dialog.pack();
            dialog.setVisible(true);

            updateView();
            updateTree();
        });
        collabItem.addActionListener(e -> {
            ListProjectDialog dialog = new ListProjectDialog( false);
            dialog.pack();
            dialog.setVisible(true);

            updateView();
            updateTree();
        });
        downloadItem.addActionListener(e -> {
            DownloadProjectDialog dialog = new DownloadProjectDialog();
            dialog.pack();
            dialog.setVisible(true);
        });
        closeItem.addActionListener(e -> {
            try {
                App.client.sendData(new ObjectWrapper.Builder()
                        .setLabel(ObjectWrapper.LBL_POST)
                        .setPerformative(ObjectWrapper.CLOSE_PROJECT)
                        .build());

                App.project = null;
                App.isOpen = false;
                App.isHost = false;
                ta.setText("");
                ta.setEditable(false);
                updateView();
                updateTree();

                DefaultListModel<String> listModel = new DefaultListModel<>();
                tvOnline.setText("Online(0)");
                listOnline.setModel(listModel);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Cannot send data to server.",
                        "Message", JOptionPane.ERROR_MESSAGE);
            }
        });

        // collab menu
        collabMenu.setText("Collab");
        collabMenu.setEnabled(false);

        inviteItem = new JMenuItem("Send invitation");
        collabInviteItem = new JMenuItem("Collab invitation");

        inviteItem.setEnabled(false);
        collabInviteItem.setEnabled(true);

        collabMenu.add(inviteItem);
        collabMenu.add(collabInviteItem);

        inviteItem.addActionListener(e -> {
            SendCollabDialog dialog = new SendCollabDialog();
            dialog.pack();
            dialog.setVisible(true);
        });
        collabInviteItem.addActionListener(e -> {
            tvNoti.setText(" ");

            ListCollabDialog dialog = new ListCollabDialog();
            dialog.pack();
            dialog.setVisible(true);
        });

        menuBar.add(generalMenu);
        menuBar.add(projectMenu);
        menuBar.add(collabMenu);
    }

    private void updateView() {
        generalMenu.setEnabled(true);
        connectionItem.setEnabled(true);
        userInfoItem.setEnabled(App.isConnect && App.isSignIn);
        signInItem.setEnabled(App.isConnect && !App.isSignIn);
        signUpItem.setEnabled(App.isConnect && !App.isSignIn);
        signOutItem.setEnabled(App.isConnect && App.isSignIn);

        projectMenu.setEnabled(App.isConnect);
        createItem.setEnabled(App.isConnect && App.isSignIn);
        listItem.setEnabled(App.isConnect && App.isSignIn && !App.isOpen);
        collabItem.setEnabled(App.isConnect && App.isSignIn && !App.isOpen);
        downloadItem.setEnabled(App.isConnect && App.isSignIn && App.isOpen);
        closeItem.setEnabled(App.isConnect && App.isSignIn && App.isOpen);

        collabMenu.setEnabled(App.isConnect);
        inviteItem.setEnabled(App.isConnect && App.isSignIn && App.isOpen && App.isHost);
        collabInviteItem.setEnabled(App.isConnect && App.isSignIn);

        btnRun.setEnabled(App.isConnect && App.isSignIn && App.isOpen);
    }

    private void setTa() {
        // set font
        ta.setFont(new Font("Consolas", Font.PLAIN, 14));
        ta.setEditable(false);

//        ta.addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyPressed(KeyEvent e) {
//                isChangeOfMyself = true;
//            }
//        });

        ta.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
//                if (isChangeOfMyself) {
//                    try {
//                        int line = SomeFunc.getLineOfOffset(ta, e.getOffset());
//                        ChangedText ct = new ChangedText(line,
//                                e.getOffset() - SomeFunc.getLineStartOffset(ta, line),
//                                e.getLength(), ChangedText.INSERT,
//                                ta.getText(e.getOffset(), e.getLength()));
//                        App.client.sendData(new ObjectWrapper.Builder()
//                                .setLabel(ObjectWrapper.LBL_POST)
//                                .setPerformative(ObjectWrapper.CHANGE_TEXT)
//                                .setData(ct)
//                                .build());
////                        System.out.println(ct.toString());
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//                }
                if (!isChangeOfOther) {
                    try {
                        int line = SomeFunc.getLineOfOffset(ta, e.getOffset());
                        ChangedText ct = new ChangedText(guid, line,
                                e.getOffset() - SomeFunc.getLineStartOffset(ta, line),
                                e.getLength(), ChangedText.INSERT,
                                ta.getText(e.getOffset(), e.getLength()));
                        App.client.sendData(new ObjectWrapper.Builder()
                                .setLabel(ObjectWrapper.LBL_POST)
                                .setPerformative(ObjectWrapper.CHANGE_TEXT)
                                .setData(ct)
                                .build());
//                        System.out.println(ct.toString());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
//                if (isChangeOfMyself) {
//                    try {
//                        int line = SomeFunc.getLineOfOffset(ta, e.getOffset());
//                        ChangedText ct = new ChangedText(line,
//                                e.getOffset() - SomeFunc.getLineStartOffset(ta, line),
//                                e.getLength(), ChangedText.REMOVE, "");
//                        App.client.sendData(new ObjectWrapper.Builder()
//                                .setLabel(ObjectWrapper.LBL_POST)
//                                .setPerformative(ObjectWrapper.CHANGE_TEXT)
//                                .setData(ct)
//                                .build());
////                        System.out.println(ct.toString());
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//                }
                if (!isChangeOfOther) {
                    try {
                        int line = SomeFunc.getLineOfOffset(ta, e.getOffset());
                        ChangedText ct = new ChangedText(guid, line,
                                e.getOffset() - SomeFunc.getLineStartOffset(ta, line),
                                e.getLength(), ChangedText.REMOVE, "");
                        App.client.sendData(new ObjectWrapper.Builder()
                                .setLabel(ObjectWrapper.LBL_POST)
                                .setPerformative(ObjectWrapper.CHANGE_TEXT)
                                .setData(ct)
                                .build());
//                        System.out.println(ct.toString());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }

    private void setButton() {
        btnRun.addActionListener(e -> {
            RunDialog dialog = new RunDialog();
            dialog.pack();
            dialog.setVisible(true);
        });
    }

    private void registerEvent() {
        App.client.register(ObjectWrapper.JOIN_ROOM, this::joinRoomResult);
        App.client.register(ObjectWrapper.RECEIVE_COLLAB, this::receiveCollabResult);
        App.client.register(ObjectWrapper.OPEN_FILE, this::openFile);
        App.client.register(ObjectWrapper.CHANGE_TEXT, this::changeText);
        App.client.register(ObjectWrapper.CHANGED_TEXT, this::changedText);
        App.client.register(ObjectWrapper.UPDATE_PROJECT, this::updateProjectResult);
    }

    private void joinRoomResult(ObjectWrapper data) {
        if (data.getLabel() == ObjectWrapper.LBL_SYNC) {
            List<User> list = (List<User>) data.getData();
            if (list == null) {
                list = new ArrayList<>();
            }
            DefaultListModel<String> listModel = new DefaultListModel<>();
            for (User u : list) {
                listModel.addElement(u.getName());
            }
            List<User> finalList = list;
            SwingUtilities.invokeLater(() -> {
                tvOnline.setText("Online(" + finalList.size() + ")");
                listOnline.setModel(listModel);
            });
        }
    }

    private void receiveCollabResult(ObjectWrapper data) {
        if (data.getLabel() == ObjectWrapper.LBL_SYNC) {
            Collab collab = (Collab) data.getData();
            SwingUtilities.invokeLater(() -> {
                tvNoti.setText("You have just received an invitation to join the "
                        + collab.getFromProject().getInfo().getName()
                        + " project by "
                        + collab.getFromProject().getHost().getName() + ".");
            });
        }
    }

    private void openFile(ObjectWrapper data) {
        if (data.getLabel() == ObjectWrapper.LBL_SUCCESS) {
            String s = (String) data.getData();
            isChangeOfOther = true;
            ta.setText(s);
            isChangeOfOther = false;
            ta.setEditable(true);
        } else {
            JOptionPane.showMessageDialog(this, data.getData(), "Message", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateProjectResult(ObjectWrapper data) {
        if (data.getLabel() == ObjectWrapper.LBL_SYNC) {
            Project project = (Project) data.getData();
            App.project = project;
            SwingUtilities.invokeLater(this::updateTree);
        }
    }

    private void changeText(ObjectWrapper data) {
        if (data.getLabel() == ObjectWrapper.LBL_SUCCESS) {
//            isChangeOfMyself = false;
        } else {
            JOptionPane.showMessageDialog(this, data.getData(), "Message", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void changedText(ObjectWrapper data) {
//        if (!isChangeOfMyself) {
//            if (data.getLabel() == ObjectWrapper.LBL_SYNC) {
//                ChangedText ct = (ChangedText) data.getData();
//                    if (ct.getType() == ChangedText.INSERT) {
//                        try {
//                            isChangeOfOther = true;
//                            ta.insert(ct.getStr(), ta.getLineStartOffset(ct.getLine()) + ct.getOffset());
//                        } catch (BadLocationException ex) {
//                            ex.printStackTrace();
//                        }
//                    }
//                    if (ct.getType() == ChangedText.REMOVE) {
//                        try {
//                            ta.replaceRange("", ta.getLineStartOffset(ct.getLine()) + ct.getOffset(),
//                                    ta.getLineStartOffset(ct.getLine()) + ct.getOffset() + ct.getLength());
//                        } catch (BadLocationException ex) {
//                            ex.printStackTrace();
//                        }
//                    }
//            }
//        }
        if (data.getLabel() == ObjectWrapper.LBL_SYNC) {
            ChangedText ct = (ChangedText) data.getData();
            if (!ct.getGuid().equals(guid)) {
                if (ct.getType() == ChangedText.INSERT) {
                    try {
                        isChangeOfOther = true;
                        ta.insert(ct.getStr(), ta.getLineStartOffset(ct.getLine()) + ct.getOffset());
                        isChangeOfOther = false;
                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                    }
                }
                if (ct.getType() == ChangedText.REMOVE) {
                    try {
                        isChangeOfOther = true;
                        ta.replaceRange("", ta.getLineStartOffset(ct.getLine()) + ct.getOffset(),
                                ta.getLineStartOffset(ct.getLine()) + ct.getOffset() + ct.getLength());
                        isChangeOfOther = false;
                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    public void updateTree() {
        if (App.isOpen) {
            root = new DefaultMutableTreeNode(App.project.getInfo().getName());
            root = setChildren(root, App.project.getRootFolder());
        } else {
            root = new DefaultMutableTreeNode("No project is open...");
        }
        model.setRoot(root);
        model.reload();
    }

    private DefaultMutableTreeNode setChildren(DefaultMutableTreeNode node, Folder folder) {
        for (Folder f : folder.getFolders()) {
            DefaultMutableTreeNode folderNode = new DefaultMutableTreeNode(f);
            folderNode = setChildren(folderNode, f);
            node.add(folderNode);
        }
        for (File f : folder.getFiles()) {
            node.add(new DefaultMutableTreeNode(f));
        }
        return node;
    }
}
