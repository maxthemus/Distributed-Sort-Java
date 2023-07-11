/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package max.distributed.sort.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Maxth
 */
public class FileReceiver {
    //Fields
    private File outputFile;
    private Socket socket;
    private BufferedReader reader;
    
    
    //Constructor
    public FileReceiver(File outputFile, Socket socket, BufferedReader reader) {
        this.outputFile = outputFile;
        this.socket = socket;
        this.reader = reader;
    }

    
    //Methods
    public boolean recieveFile() {
        PrintWriter socketWriter;
        PrintWriter writer;
        try {
            writer = new PrintWriter(this.outputFile);
            socketWriter = new PrintWriter(this.socket.getOutputStream(), true);
        } catch(Exception e) {
            System.out.println(e);
            return false;
        }
   
        String input;
        do {
            try {
                //Recieve message
                input = this.reader.readLine();
                
                //Send Response
                socketWriter.println("RES");     
            } catch (Exception ex) {
                System.out.println(ex);
                return false;
            }
            
            if(input.compareTo("EOF") == 0) {
                break;
            } 

            writer.println(Integer.valueOf(input));
        } while(true);

        System.out.println("EOF FOUND");
        //Closing file writer
        writer.close();
        
        return true;
    }
}

