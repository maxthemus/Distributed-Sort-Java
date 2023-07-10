/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package max.distributed.sort.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Maxth
 */
public class FileSender {
    //Fields
    private File file;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    
    
    //Constructor
    public FileSender(File file, Socket socket, PrintWriter writer, BufferedReader reader) {
        this.file = file;
        this.socket = socket;
        this.writer = writer;
        this.reader = reader;
    }
    
    
    //Methods
    public boolean sendFile() {
        Scanner sc;
        try {
            sc = new Scanner(this.file); //Opening file reader
        } catch(Exception e) {
            System.out.println(e);
            return false;
        }

        int tempNumber;
            
        while(sc.hasNext()) {
            tempNumber = sc.nextInt();
            this.writer.println(tempNumber);
            
            //Wait for response
            String recv;
            try {
                recv = this.reader.readLine();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
        
        //Sending EOF
        this.writer.println("EOF");
        
        sc.close();
        
        return true;
    }
    
    
    public static void main(String[] args) {
        FileSender sender = new FileSender(new File("files/client/input"), null, null, null);
        sender.sendFile();
    }
    
    
}
