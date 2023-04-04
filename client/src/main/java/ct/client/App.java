package ct.client;

import ct.client.control.ClientCtrl;
import ct.model.Project;
import ct.model.User;

public class App {
    public static ClientCtrl client;

    public static User user;
    public static Project project;

    public static boolean isConnect = false;
    public static boolean isSignIn = false;
    public static boolean isOpen = false;
    public static boolean isHost = false;
}
