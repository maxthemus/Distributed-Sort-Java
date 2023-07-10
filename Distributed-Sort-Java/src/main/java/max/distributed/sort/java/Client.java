/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
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
public class Client {
    //Fields
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    
    //Constructor
    public Client(String address, int port) {
        try {
            //Connecting socket
            this.socket = new Socket(address, port);
            
            //Setting up I/O
            this.writer = new PrintWriter(this.socket.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        } catch(Exception e) {
            System.out.println(e);
        }
    }
    
    
    //Methods
    public String recieveMessage() {
        try {
            String string = this.reader.readLine();
            return string;
        } catch(Exception e) {
            System.out.println(e);
            return null;
        }
    }
    
    public boolean networkSortFile(File fileToSort, File outputFile) {
        //Check if socket is open
        if(this.socket == null) {
            return false;
        }
        
        //Split file into temp files
        int networkConnecitonCount = 2;
        File[] sendFiles = new File[networkConnecitonCount];
        for(int i = 0; i < networkConnecitonCount; i++) {
            try {
                sendFiles[i] = new File("files/client/tmp/temp_" + i);
            } catch(Exception e) {
                System.out.println(e);
            }
        }
        
        //Split main file into sub files
        FileSpliter spliter = new FileSpliter(fileToSort, sendFiles);        
        if(!spliter.readAndWriteToFiles()) {
            return false;
        }

        //Send file to server
        this.sendFileToServer(sendFiles[1]);
        
        //Once the File is sent we want to sort our own file
        System.out.println("SORTING OWN FILES");
        
        File sortedFile = new File("files/client/tmp/sorted");
        FileSorter fileSorter = new FileSorter(sortedFile, sendFiles[0]);
        fileSorter.sortFile();
        System.out.println("FILE SORTED");
        
        try {
            //Clearing input buffer
            this.reader.readLine();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        
        //Recieve the files 
        
        File serverFile = new File("files/client/tmp/server_file");
        this.recieveFileFromServer(serverFile);
        
        
        //Now we want to merge the two files
        System.out.println("READY TO MERGE");
        
        FileMerger merger = new FileMerger(outputFile, serverFile, sortedFile);
        merger.mergeFiles();
        
        System.out.println("ALL DONE");
        
        return true;
    }
    
    private void sendFileToServer(File file) {
        FileSender sender = new FileSender(file, this.socket, this.writer, this.reader);
        sender.sendFile();
    }
    
    private void recieveFileFromServer(File file) {
        FileReceiver reciever = new FileReceiver(file, this.socket, this.reader);
        reciever.recieveFile();
    }
    
    
    
    
    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 3010);
        client.networkSortFile(new File("files/client/input"), new File("files/client/output"));
    }
}
