/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package max.distributed.sort.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Maxth
 */
public class Server {
    //Fields
    private final int PORT;
    private boolean serverRunning;
    private ServerSocket serverSocket;
    private ArrayList<ClientSocket> clients;
    
    
    //Constructor
    public Server(int port) {
        this.PORT = port;
        this.serverRunning = false;
    }
    
    
    //Methods
    public void startServer() {
        try {
            this.serverSocket = new ServerSocket(this.PORT);
            this.serverRunning = true;
            
            this.listenConnections();
        } catch(Exception e) {
            System.out.println(e);
        }
    }
    
    public void stopServer() {
        
        
    }
    
    
    private void listenConnections() {
        try {
            while(this.serverRunning) {
                Socket newConneciton = this.serverSocket.accept();
                //Creating client object
                ClientSocket newClient = new ClientSocket(newConneciton);
                
                //Creating file to store the incomming values in
                File tempFile = new File("files/server/temp/networkFile");
                
                //Once Client is connected we want to listen for incomming files
                FileReceiver reciever = new FileReceiver(tempFile, newClient.getSocket(), newClient.getReader());
                reciever.recieveFile();
                
                //Now we sort the array
                File sortedFile = new File("files/server/temp/sortedFile");
                FileSorter sorter = new FileSorter(sortedFile, tempFile);
                sorter.sortFile();
                
                
                //Now we want to send the file back
//                FileSender fileSender = new FileSender(sortedFile, newClient.getSocket(), newClient.getWriter(), newClient.getReader());
//                fileSender.sendFile();
                
                System.out.println("ALL DONE");
            }
        } catch(Exception e) {
            System.out.println(e);
        }
    }
    
    
   
    //Main Method
    public static void main(String[] args) {
        Server server = new Server(3010);
        server.startServer();
    }
    
    
    //Private Inner Class
    private class ClientSocket {
        //Fields
        private Socket socket;
        private PrintWriter writer;
        private BufferedReader reader;

        
        //Constructor
        public ClientSocket(Socket socket) {
            this.socket = socket;
            
            try {
                //Setting up I/O
            this.writer = new PrintWriter(this.socket.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            } catch(Exception e) {
                System.out.println(e);
            }
        }
        
        //Methods
        public void sendMessage(String message) {
            this.writer.println(message);
        }

        public Socket getSocket() {
            return socket;
        }

        public PrintWriter getWriter() {
            return writer;
        }

        public BufferedReader getReader() {
            return reader;
        }
        
        
        
        public String recieveMessage() {
            try {
                String recvMessage = this.reader.readLine();
                return recvMessage;
            } catch(Exception e) {
                System.out.println(e);
                return null;
            }
        }
    }
}
