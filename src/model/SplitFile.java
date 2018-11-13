/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

/**
 *
 * @author MyPC
 */
public class SplitFile {
     public boolean splitFile(String source, long partSize,Socket clientSocket,JProgressBar jProgressBar,int c) throws FileNotFoundException, IOException, InterruptedException {
       
        File sourceFile = new File(source);
        if (sourceFile.exists()) {
            long sizeFile = sourceFile.length();
            int numbPart = (int)(sizeFile / partSize);
            long remain = sizeFile%partSize;
            
            if(remain > 0)
                numbPart++;
            
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(sourceFile.getName());
            fileInfo.setFileSize(sizeFile);
            fileInfo.setPiecesOfFile(numbPart);
            fileInfo.setPartLength(partSize);
            ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
            oos.writeObject(fileInfo);
            System.out.println(fileInfo.getFileSize());
            
            InputStream is = new FileInputStream(sourceFile);
            byte[] arr = new byte[1024*300];
            OutputStream os = null;
            jProgressBar.setValue(0);
            for (int i = 1; i <= numbPart; i++) {
                int j = 0;
                long a = 0;
//                OutputStream os = new FileOutputStream(dest + sourceFile.getName() + "." + i);
                os = clientSocket.getOutputStream();
                while ((j = is.read(arr)) != -1) {
                    os.write(arr, 0, j);
                    a += j;
                    if (a >= partSize) {
                        break;
                    }
                }
                System.out.println("file cắt được "+sourceFile.getName()+"."+i);
                os.flush();
                    jProgressBar.setValue(i*100/fileInfo.getPiecesOfFile());
                    if(jProgressBar.getValue() == 100 && c == 2){
                        JOptionPane.showMessageDialog(null, "Done");
                        jProgressBar.setValue(0);
                    }
//                os.close();
            }
//            clientSocket.close();
            is.close();
            os.close();
            return true;
        } else {
            System.out.println("file không tồn tại");
            return false;
        }
    }
}
