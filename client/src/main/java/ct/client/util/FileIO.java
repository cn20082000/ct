package ct.client.util;

import ct.model.File;

import java.io.FileWriter;
import java.io.IOException;

public class FileIO {
    private static void createFile(java.io.File file) {
        java.io.File parent = file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IllegalStateException("Couldn't create dir: " + parent);
        }
    }
    public static void writeToFile(String path, File file, String data) throws IOException {
        String[] parts = file.getUrl().split("\\\\");
        if (parts.length < 3) {
            throw new IOException("Invalid file");
        }
        for (int i = 2; i < parts.length; ++i) {
            path += "\\" + parts[i];
        }
        java.io.File f = new java.io.File(path);
        createFile(f);
        FileWriter myWriter = new FileWriter(f);
        myWriter.write(data);
        myWriter.close();
    }
}
