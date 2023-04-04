package ct.tcp.util;

import ct.model.File;
import ct.model.Folder;
import ct.model.Project;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileIO {
    public static final String PARENT = "D:\\Files\\";

    public static void initProject(Project project) throws IOException {
        File file = project.getRootFolder().getFiles().get(0);
        java.io.File mainCode = new java.io.File(PARENT + file.getUrl());
        String rootPath = mainCode.getParentFile().getParent() + "\\";

        createFile(mainCode);
        FileWriter myWriter = new FileWriter(mainCode);
        myWriter.write("""
                public class Main {
                \tpublic static void main(String args[]) {
                \t\t// Your code here
                \t\tSystem.out.println("Hello World!");
                \t}
                }
                """);
        myWriter.close();

        java.io.File execFile = new java.io.File(rootPath + "exec.bat");
        createFile(execFile);
        myWriter = new FileWriter(execFile);
        myWriter.write("""
                @echo off
                dir /s/b *.java > sources.txt
                javac -encoding "UTF-8" -d build/compile @sources.txt
                cd build/compile
                java Main < in.txt
                cd ../..
                """);
        myWriter.close();

        java.io.File inFile = new java.io.File(rootPath + "build\\compile\\in.txt");
        createFile(inFile);
        myWriter = new FileWriter(inFile);
        myWriter.write("\n");
        myWriter.close();
    }

    private static List<File> getFileFrom(Folder folder) {
        List<File> result = folder.getFiles();
        for (Folder fo : folder.getFolders()) {
            result.addAll(getFileFrom(fo));
        }
        return result;
    }

    private static void createFile(java.io.File file) {
        java.io.File parent = file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IllegalStateException("Couldn't create dir: " + parent);
        }
    }

    public static String getDataFrom(File file) throws IOException {
        Path p = Path.of(PARENT, file.getUrl());
        return Files.readString(p);
    }

    public static void writeToInput(Project project, String input) throws IOException {
        File file = project.getRootFolder().getFiles().get(0);
        java.io.File mainCode = new java.io.File(PARENT + file.getUrl());
        String rootPath = mainCode.getParentFile().getParent() + "\\";

        java.io.File inFile = new java.io.File(rootPath + "build\\compile\\in.txt");
        FileWriter myWriter = new FileWriter(inFile);
        myWriter.write(input);
        myWriter.close();
    }

    public static void writeToFile(File file, String data) throws IOException {
        java.io.File f = new java.io.File(PARENT + file.getUrl());
        FileWriter myWriter = new FileWriter(f);
        myWriter.write(data);
        myWriter.close();
    }

    public static String runProject(Project project) throws IOException {
        File file = project.getRootFolder().getFiles().get(0);
        java.io.File mainCode = new java.io.File(PARENT + file.getUrl());
        String rootPath = mainCode.getParentFile().getParent() + "\\";
        String result = "";

        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "exec.bat");
        builder = builder.directory(new java.io.File(rootPath));
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = r.readLine()) != null) {
            result += line + "\n";
        }
        return result;
    }

    public static void createNewClass(Project project, File file) throws IOException {
        java.io.File mainCode = new java.io.File(PARENT + file.getUrl());
        createFile(mainCode);
        FileWriter myWriter = new FileWriter(mainCode);
        myWriter.write("public class " + file.getInfo().getName().substring(0, file.getInfo().getName().length() - 5)
                       + " {\n\n}\n");
        myWriter.close();
    }
}
