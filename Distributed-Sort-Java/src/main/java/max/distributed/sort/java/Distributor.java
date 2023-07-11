/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package max.distributed.sort.java;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author max
 */
public class Distributor implements Runnable {
    //Fields
    private final LinkedQueue<Integer> inputQueue;
    private final Socket socket;
    private final ArrayList<Integer> localArray;
    private boolean sending;
    
    //Constructor
    public Distributor(Socket socket, LinkedQueue<Integer> inputQueue, ArrayList<Integer> localArray) {
            this.inputQueue = inputQueue;
            this.socket = socket;
            this.sending = true;
            this.localArray = localArray;
    }
    
    
    
    //Methods
    @Override
    public void run() {
        //Creating I/O streams
        PrintWriter writer;
        BufferedReader reader;
        try {
            writer = new PrintWriter(this.socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        } catch(Exception e) {
            System.out.println(e);
            System.exit(0);
            return;
        }
        

        boolean keepingValue = true;
        while(sending) {
            if(inputQueue.size() <= 0) {
                if(inputQueue.isDone()) {
                    sending = false;
                    
                    //And we want to send EOF
                    writer.println("EOF");
                    System.out.println("EOF sent");
                }
            } else {
                //We want to deque a value and send is
                int value = inputQueue.dequeue();
                
                if(!keepingValue) {
                    writer.println(value);

                   try {
                        //Now we wait for response
                        while(reader.ready()) {
                            reader.readLine();
                        }
                   } catch(Exception e) {
                       System.out.println(e);
                       System.exit(0);
                       return;
                   }  
                } else {
                    //We want to write into local array
                    this.localArray.add(value);
                }
            }      
        }
        
        try{
            reader.close();
            writer.close();
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}
