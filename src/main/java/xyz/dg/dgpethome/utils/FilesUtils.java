package xyz.dg.dgpethome.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Dugong
 * @date 2021-11-07 14:27
 * @description
 **/
public class FilesUtils {
    public byte[] getFile(String path) throws IOException {
        File file = new File(path);
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        inputStream.close();
        return bytes;
    }

    public boolean judgeFileExists(String path) {
        File file = new File(path);
        return file.exists();
    }
}