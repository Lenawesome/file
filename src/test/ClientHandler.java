/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ServerDirectory;
import model.SplitFile;

/**
 *
 * @author MyPC
 */
public class ClientHandler extends Thread{
    private Socket clientSocket;
    int clientID = -1;
    boolean running = true;
    private ObjectOutputStream oos;
    private DataInputStream dis;
    private DataOutputStream dos;
    private OutputStream os;
    private File serverStorage = new File("C:\\Users\\MyPC\\Desktop\\server");
    File[] listFiles = serverStorage.listFiles();
    private ServerDirectory serverDirectory;
    
    
    public ClientHandler(Socket clientSocket, int i) {
        this.clientSocket = clientSocket;
        clientID = i;
        serverDirectory = new ServerDirectory(listFiles);
        try {
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            oos.writeObject(serverDirectory);
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void run() {
        System.out.println("Accepted Client : ID -" + clientID + ": Address ="
        + clientSocket.getInetAddress().getHostName());
        try {
            dis = new DataInputStream(clientSocket.getInputStream());
            os = clientSocket.getOutputStream();
            while(running){
                String clientRequest = dis.readUTF();
                System.out.println("Client request: " + clientRequest);
                if(clientRequest.equals("Download")){
                    String path = dis.readUTF();
                    
                    SplitFile splitFile = new SplitFile();
                    
                    splitFile.splitFile(path, 1024*300, clientSocket);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    
    
}
