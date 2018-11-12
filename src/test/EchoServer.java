/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author MyPC
 */
public class EchoServer {
    public static void main(String[] args) throws IOException {
        ServerSocket m_ServerSocket = new ServerSocket(2001);
        int id = 0;
        while (true) {
            Socket clientSocket = m_ServerSocket.accept();
            ClientHandler clientThread = new ClientHandler(clientSocket, id++);
            clientThread.start();
        }
    }
}
