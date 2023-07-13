/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package max.distributed.sort.java;

import java.io.File;
import java.net.Socket;
import java.util.ArrayList;


/**
 *
 * @author Maxth
 */
public class Client {
    //Fields
    private static final String WINDOWS_PATH_NAME = "files\\client\\";
    private static final String LINUX_PATH_NAME = "files/client/";
    private static final String PATH_NAME = "files\\client\\";
    private Socket socket;
//    private PrintWriter writer;
//    private BufferedReader reader;
    
    //Constructor
    public Client(String address, int port) {
        try {
            //Connecting socket
            this.socket = new Socket(address, port);
        } catch(Exception e) {
            System.out.println(e);
        }
    }
    
    

    
    public File networkSortFile(File fileToSort, File outputFile) {
        //Check if socket is open
        if(this.socket == null) {
            return null;
        }
        
        
        LinkedQueue<Integer> queue = new LinkedQueue<>();
        FileReader fileReader = new FileReader(fileToSort, queue);
        
        
        File localOut = new File(WINDOWS_PATH_NAME + "temp\\local");
        File networkOut = new File(WINDOWS_PATH_NAME + "temp\\network");
        File[] files = {localOut, networkOut};
        
        Distributor distributor = new Distributor(queue, files);
        
        Thread readerThread = new Thread(fileReader);
        Thread distributorThread = new Thread(distributor);
        
        readerThread.start();
        distributorThread.start();
       
        try {
            distributorThread.join();
        } catch(Exception e) {
            System.out.println(e);
            return null;
        }

//        System.out.println("SENDING FILES");
        //Creating sender threads
        Thread[] senderThreads = new Thread[files.length-1];
        
        for(int i = 1; i < files.length; i++) {
            FileSender sender = new FileSender(files[i], socket);
            senderThreads[i-1] = new Thread(sender);
            senderThreads[i-1].start();
        }
        
//        System.out.println("SORTING START");
        //Creating the sorter thread
        FileSorter localSorter = new FileSorter(files[0], WINDOWS_PATH_NAME + "temp\\");
        Thread sorterThread = new Thread(localSorter);
        sorterThread.start();
        
        
        //Before recieving we want to wait for the sender threads to finish
        for(int i = 0; i < senderThreads.length; i++) {
            try {
                senderThreads[i].join();
            } catch(Exception e) {
                System.out.println(e);
                System.exit(1);
            }
        }
        
//        System.out.println("SEND DONE");
        
//        System.out.println("STARTING Reciever");
        
        //Now we want to wait to recieve a file from the different sockets
        FileReceiver[] recievers = new FileReceiver[senderThreads.length];
        File[] outputFiles = new File[senderThreads.length+1];
        Thread[] recieverThreads = new Thread[senderThreads.length];
        for(int i = 0; i < senderThreads.length; i++) {
            outputFiles[i+1] = new File(PATH_NAME + "temp\\output_" + i);
            recievers[i] = new FileReceiver(outputFiles[i+1], this.socket);
            recieverThreads[i] = new Thread(recievers[i]);
            recieverThreads[i].start();
        }
        
        
        //Now we want to join the sorter thread and copy file into index 0 of outputFiles
        try {
            sorterThread.join();
//            System.out.println("SORTING DONE");
        } catch(Exception e ){
            System.out.println("ERROR SORTING");
            System.exit(1);
        }
        if(localSorter.getDone()) {
            outputFiles[0] = localSorter.getOutputFile();
        } else {
            System.out.println("ISN'T DONE ERROR");
            System.exit(1);
        }
        
        //Now we join all the threads
        for(int i = 0; i < recieverThreads.length; i++) {
            try {
                recieverThreads[i].join();
            } catch(Exception e) {
                System.out.println(e);
                System.exit(1);
            }
        }
        
//        System.out.println("FILE RECIEVED");
        
        //Now that we have all the files we need to merge them into one file and then 
        FileMerger merger = new FileMerger("final", WINDOWS_PATH_NAME + "temp\\");
        int placeIndex = 0;
        int segEnd = outputFiles.length;
        while(segEnd > 2) {
            placeIndex = 0;
            for(int i = 0; i < segEnd; i+=2) {
                File tempOne = outputFiles[i];
                File tempTwo = outputFiles[i+1];
                outputFiles[i] = null;
                outputFiles[i+1] = null;
                
                if(tempTwo == null) {
                    outputFiles[placeIndex++] = tempOne;
                } else {
                    File mergedFile = merger.mergeFiles(tempOne, tempTwo);
                    outputFiles[placeIndex++] = mergedFile;
                }
             
                //Cleaning up old files
                tempOne.delete();
                tempTwo.delete();
            }
            segEnd = placeIndex;
        }
        
        //Now we want to merge the last two files into the output file
        merger.mergeFiles(outputFiles[0], outputFiles[1], outputFile);
        outputFiles[0].delete();
        outputFiles[1].delete();
        
        for(int i = 0; i < files.length; i++) {
            files[i].delete();
        }

        return outputFile;
    }
    
    
    public static void main(String[] args) {
        File inputFile = new File("files\\client\\input");
        File outputFile = new File("files\\client\\output");
        
        Client client = new Client("127.0.0.1", 3010);
        
        
        
        double start = System.nanoTime();

        outputFile = client.networkSortFile(inputFile, outputFile);
        
        double end = System.nanoTime();
        
        double time = (end - start) / 1_000_000_000.0;
        if(outputFile == null) {
            System.out.println("FAILED");
        } else {
//            System.out.println(outputFile.getName());
        }
        System.out.println("TIME");
        System.out.println("TOOK : " + (time));
        System.exit(0);
    }
}
