package ct.client;

import ct.client.view.MainFrm;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        MainFrm frm = new MainFrm();
        frm.setVisible(true);


//        try {
//            System.out.println("Start");
//            ProcessBuilder builder = new ProcessBuilder(
//                    "cmd.exe", "/c", "CnR.bat");
//            builder = builder.directory(new java.io.File("E:\\Data\\proj\\java\\NhacNhoLamViec\\"));
//            builder.redirectErrorStream(true);
//            Process p = builder.start();
//            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
//            String line;
//            while ((line = r.readLine()) != null)
//                System.out.println(line);
//            System.out.println("End");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
