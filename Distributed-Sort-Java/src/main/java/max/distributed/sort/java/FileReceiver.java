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

/**
 *
 * @author Maxth
 */
public class FileReceiver implements Runnable {
    //Fields
    private File outputFile;
    private Socket socket;
    
    
    
    //Constructor
    public FileReceiver(File outputFile, Socket socket) {
        this.outputFile = outputFile;
        this.socket = socket;
    }

    
    //Methods
    @Override
    public void run() {
        try {
            //Setting up reader
            BufferedReader socketReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            PrintWriter socketWriter = new PrintWriter(this.socket.getOutputStream(), true);
            
            //Setting up file writer
            LinkedQueue<Integer> queue = new LinkedQueue<>();
            FileWriter fileWriter = new FileWriter(outputFile, queue);
            Thread writerThread = new Thread(fileWriter);
            writerThread.start();
            
            //Now we want to listen to the socket until we reciever EOF
            String input = "";
            while(true) {
                input = socketReader.readLine();
                if(input.equals("EOF")) {
                    break;
                } else {
                    String[] values = input.split(" ");
                    for(int i = 0; i < values.length; i++) {
                        queue.enqueue(Integer.valueOf(values[i]));
                    }
                }
                
                //Sending reponse 
                socketWriter.println("OK");
            }
            
            queue.setDone(true);            
            //Closing reader and writer
//            socketReader.close();
//            socketWriter.close();
            
            writerThread.join();
        } catch(Exception e) {
            System.out.println("ERROR IN RECIEVER");
            System.out.println(e);
            StackTraceElement[] stackTrace = e.getStackTrace();
            if (stackTrace.length > 0) {
                int lineNumber = stackTrace[0].getLineNumber();
                System.out.println("Exception thrown at line: " + lineNumber);
            }
            System.exit(5);
            return;
        }
    }
}

