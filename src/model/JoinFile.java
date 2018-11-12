/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author MyPC
 */
public class JoinFile {
    public boolean joinFile(String source) throws FileNotFoundException, IOException {
//    name file
        String sourceFile = source.substring(0, source.lastIndexOf('.'));
        System.out.println(sourceFile);
        File file = new File(sourceFile);
        OutputStream os = new FileOutputStream(file);
        InputStream is;
        int count = 1;
        while (true) {
            String path = file + "." + count;
            File eachFile = new File(path);
            if (eachFile.exists()) {
                is = new FileInputStream(eachFile);
                int i = 0;
                byte[] arr = new byte[1024];
                while ((i = is.read(arr)) != -1) {
                    os.write(arr, 0, i);
                };
                os.flush();
                is.close();
                eachFile.delete();
                count++;
            } else {
                System.out.println("Done");
                break;
            }
        }
        os.close();
        return false;
    }
}
