/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ct.client.frm;

import cn.ct.client.App;
import cn.ct.client.view.MainPanel;
import cn.ct.model.ObjectWrapper;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author cn200
 */
public class MainFrm extends JFrame {
    
    private JMenuBar menuBar = new JMenuBar();
    
    private JMenu generalMenu = new JMenu();
    private JMenuItem connectionItem, userInfoItem, signInItem, signUpItem, signOutItem;
    
    private JMenu projectMenu = new JMenu();
    private JMenuItem createItem, listItem, collabItem, closeItem;
    
    private JMenu collabMenu = new JMenu();
    private JMenuItem inviteItem, collabInviteItem;
    
    private boolean isConnect = false;
    private boolean isSignin = false;
    private boolean isOpen = false;
    
    private MainPanel mainPanel = new MainPanel(this);
    
    public MainFrm() {
        super();
        init();
    }
    
    private void init() {
        this.setTitle("Code together");
        this.setSize(450, 300);
        this.setLocation(200, 10);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        
        setMenuBar();
        this.setJMenuBar(menuBar);
    }
    
    public void open() {
        this.add(mainPanel);
        this.setVisible(true);
    }
    
    public void close() {
        this.dispose();
    }
    
    private void updateView() {
        if (!isConnect) {
            userInfoItem.setEnabled(false);
            signInItem.setEnabled(false);
            signUpItem.setEnabled(false);
            signOutItem.setEnabled(false);
            
            projectMenu.setEnabled(false);
        } else {
            if (isSignin) {
                userInfoItem.setEnabled(true);
                signInItem.setEnabled(false);
                signUpItem.setEnabled(false);
                signOutItem.setEnabled(true);
            
                projectMenu.setEnabled(true);
                collabMenu.setEnabled(true);
                if (isOpen) {
                    closeItem.setEnabled(true);
//                    if (App.user.getId() == App.project.getHost().getId()) {
                        inviteItem.setEnabled(true);
//                    } else {
//                        inviteItem.setEnabled(false);
//                    }
                } else {
                    closeItem.setEnabled(false);
                    inviteItem.setEnabled(false);
                }
            } else {
                userInfoItem.setEnabled(false);
                signInItem.setEnabled(true);
                signUpItem.setEnabled(true);
                signOutItem.setEnabled(false);
            
                projectMenu.setEnabled(false);
            }
        }
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
            ConnectionDialog dialog = new ConnectionDialog(this);
            dialog.open();
        });
        userInfoItem.addActionListener(e -> {
            UserInfoDialog dialog = new UserInfoDialog(this);
            dialog.open();
        });
        signInItem.addActionListener(e -> {
            SigninDialog dialog = new SigninDialog(this);
            dialog.open();
        });
        signUpItem.addActionListener(e -> {
            SignupDialog dialog = new SignupDialog(this);
            dialog.open();
        });
        signOutItem.addActionListener(e -> {
            App.user = null;
            isSignin = false;
            updateView();
        });
        
        // project menu
        projectMenu.setText("Project");
        projectMenu.setEnabled(false);
        
        createItem = new JMenuItem("Create new project");
        listItem = new JMenuItem("Open project");
        collabItem = new JMenuItem("Open collab project");
        closeItem = new JMenuItem("Close project");
        
        createItem.setEnabled(true);
        listItem.setEnabled(true);
        collabItem.setEnabled(true);
        closeItem.setEnabled(false);
        
        projectMenu.add(createItem);
        projectMenu.add(listItem);
        projectMenu.add(collabItem);
        projectMenu.add(closeItem);
        
        createItem.addActionListener(e -> {
            CreateProjectDialog dialog = new CreateProjectDialog(this);
            dialog.open();
        });
        listItem.addActionListener(e -> {
            ListProjectDialog dialog = new ListProjectDialog(this, false);
            dialog.open();
        });
        collabItem.addActionListener(e -> {
            ListProjectDialog dialog = new ListProjectDialog(this, true);
            dialog.open();
        });
        closeItem.addActionListener(e -> {
            App.project = null;
            isOpen = false;
            App.ctrl.sendData(new ObjectWrapper(ObjectWrapper.GET_CLOSE_PROJECT, null));
            updateView();
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
            SendCollabDialog dialog = new SendCollabDialog(this);
            dialog.open();
        });
        collabInviteItem.addActionListener(e -> {
            ListCollabDialog dialog = new ListCollabDialog(this);
            dialog.open();
        });
        
        menuBar.add(generalMenu);
        menuBar.add(projectMenu);
        menuBar.add(collabMenu);
    }
    
    public void connect() {
        isConnect = true;
        mainPanel.regis();
        updateView();
    }
    
    public void disconnect() {
        isConnect = false;
        isSignin = false;
        App.user = null;
        updateView();
    }
    
    public void signInSuccess() {
        isSignin = true;
        updateView();
    }
    
    public void openProject() {
        isOpen = true;
        updateView();
    }
}
