/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package max.distributed.sort.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


/**
 *
 * @author Maxth
 */
public class FileSender implements Runnable {
    //Fields
    private File file;
    private Socket socket;
    private final int MAX_SEND_COUNT = 100;
    
    
    //Constructor
    public FileSender(File file, Socket socket) {
        this.file = file;
        this.socket = socket;
    }
    
    
    //Methods
    @Override
    public void run() {
        try {
//            System.out.println("SENDING FILE");
            LinkedQueue<Integer> queue = new LinkedQueue<>();
            FileReader reader = new FileReader(file, queue);
            Thread readerThread = new Thread(reader);
            readerThread.start();
            
            //Opening up print writer and reader
            BufferedReader socketReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream())); 
            PrintWriter socketWriter = new PrintWriter(this.socket.getOutputStream(), true);

            //Keep sending until file is comp
            int sendCount = 0;
            while(!queue.isDone()) {
                if(queue.size() >= 1) {
                    String string = "";
                    while(queue.size() > 0 && sendCount < MAX_SEND_COUNT) {
                        string += queue.dequeue() + " ";
                        sendCount++;
                    }
//                    System.out.println("SENDING STRING == " + string);
                    socketWriter.println(string);
                    sendCount = 0; //Resetting send Count
                    socketReader.readLine();
                } else {
                    Thread.yield();
                }
            }
            
            //Clearing rest of queue
            sendCount = 0;
            while(queue.size() >= 1) {
                String string = "";
                    while(queue.size() > 0 && sendCount < MAX_SEND_COUNT) {
                        string += queue.dequeue() + " ";
                        sendCount++;
                    }
//                    System.out.println("SENDING STRING == " + string);
                    socketWriter.println(string);
                    sendCount = 0; //Resetting send Count
                    socketReader.readLine();
            }
            
            //Sending EOF
            socketWriter.println("EOF");
        } catch(Exception e) {
            System.out.println(e);
            System.exit(1);
        }
    }
}
