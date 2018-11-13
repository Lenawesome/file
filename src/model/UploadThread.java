/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import GiaoDien.Client;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author MyPC
 */
public class UploadThread extends Thread{
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

    public UploadThread(File[] listFile, File[] listFiles2, DefaultTableModel model1, DefaultTableModel model2, Socket clientSocket, JProgressBar progressBar, JTable TableClient, JTable TableServer) {
        this.listFile = listFile;
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
         DataOutputStream dos;
        try {
            int rowSelected = TableClient.getSelectedRow();
            if(rowSelected != -1){
                String path = model1.getValueAt(rowSelected, 0).toString();
                dos = new DataOutputStream(clientSocket.getOutputStream());
                dos.writeUTF("Upload");
                upload(path);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//            try {
////                dos.close();
//            } catch (IOException ex) {
//                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }
    }
    private void initConnection() {
        try {
            clientSocket = new Socket(hostName, port);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void upload(String path) {
        for (File f : listFile) {
            if (f.getName().equals(path) && f.isFile()) {
                sendToServer(f);
            }
        }
    }
    private void sendToServer(File f) {
        
        SplitFile splitFile = new SplitFile();
        try {
            System.out.println(splitFile.splitFile(f.getAbsolutePath(), 1024 * 300, clientSocket,progressBar,2));
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
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
}
