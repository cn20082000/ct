package ct.tcp;

import ct.tcp.view.MainFrm;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        MainFrm frm = new MainFrm();
        frm.setVisible(true);
    }
}
