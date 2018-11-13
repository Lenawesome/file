/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import GiaoDien.Client;
import GiaoDien.Server;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author MyPC
 */
public class DownlaodThread extends Thread{
    private File[] listFile;
    File[] listFiles2;
    private DefaultTableModel model1;
    private DefaultTableModel model2;
    private Socket clientSocket;
    private JoinFile jFile  = new JoinFile();
    private String downloadDes = "client\\";
    
    private JProgressBar progressBar;
    private String hostName = "localhost";
    private int port = 2001;
    
    private JTable TableClient;
    private JTable TableServer;

    public DownlaodThread(File[] listFiles2, DefaultTableModel model1, DefaultTableModel model2, Socket clientSocket, JProgressBar progressBar, JTable TableClient, JTable TableServer) {
        this.listFiles2 = listFiles2;
        this.model1 = model1;
        this.model2 = model2;
        this.clientSocket = clientSocket;
        this.progressBar = progressBar;
        this.TableClient = TableClient;
        this.TableServer = TableServer;
    }

    
    
    
    
    
    @Override
    public void run(){
        sendDownloadRequest();
    }
    private void initConnection() {
        try {
            clientSocket = new Socket(hostName, port);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void receiveFromServer() {
        try {
            model2.setRowCount(0);
            ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
            ServerDirectory listDirectory = (ServerDirectory) ois.readObject();
            Object[] row = new Object[2];
            listFiles2 = listDirectory.getListFiles();
            for (int i = 0; i < listFiles2.length; i++) {
                row[0] = listFiles2[i].getName();
                
//            row[1] = rootFile[i].length();
                model2.addRow(row);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void sendDownloadRequest() {
        DataOutputStream dos = null;
        try {
            int rowSelected1 = TableClient.getSelectedRow();
            if(rowSelected1 != -1){
                String desName = model1.getValueAt(rowSelected1, 0).toString();
                for(File f : listFile){
                    if(f.getName().equals(desName)){
                        downloadDes = f.getAbsolutePath();
                        downloadDes = downloadDes+"\\";
                    }
                }
            }
            
            System.out.println(downloadDes);
            
//            DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
//            System.out.println(dis.readUTF());
            
            int rowSelected2 = TableServer.getSelectedRow();
            if(rowSelected2 != -1){
                dos = new DataOutputStream(clientSocket.getOutputStream());
                dos.writeUTF("Download");
                String path = model2.getValueAt(rowSelected2, 0).toString();
                for (File f : listFiles2) {
                    if (f.getName().equals(path)) {
                        dos.writeUTF(f.getAbsolutePath());
                        break;
    //                    dos.flush();
                    }
                }
                receiveFile();
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

       private void receiveFile() {
        ObjectInputStream ois;
            try {
                ois = new ObjectInputStream(clientSocket.getInputStream());
                FileInfo fileInfo = (FileInfo) ois.readObject();
                InputStream is = clientSocket.getInputStream();
//                progressBar.setValue(0);
                byte[] arr = new byte[1024*300];
                for (int i = 1; i <= fileInfo.getPiecesOfFile(); i++) {
                    int j = 0;
                    long a = 0;
    //                OutputStream os = new FileOutputStream(dest + sourceFile.getName() + "." + i);
                    OutputStream os = new FileOutputStream(downloadDes + fileInfo.getFileName()+ "." + i);
                    while ((j = is.read(arr)) != -1) {
                        os.write(arr, 0, j);
                        a += j;
                        if (a >= fileInfo.getPartLength()) {
                            break;
                        }
                    }
//                    progressBar.setValue(i*100/fileInfo.getPiecesOfFile());
//                    if(progressBar.getValue() == 100){
//                        JOptionPane.showMessageDialog(null, "Done");
//                        progressBar.setValue(0);
//                    }
                    System.out.println("file cắt được "+fileInfo.getFileName()+"."+i);
                    os.flush();
                    os.close();
                }
                System.out.println("toi day roi");
                jFile.joinFile(downloadDes+fileInfo.getFileName()+".1",progressBar,fileInfo.getPiecesOfFile(),2);
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
//                try {
//                    ois.close();
//                } catch (IOException ex) {
//                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
//                }
            }
    }
}
