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
    private ArrayList<Socket> connections;
//    private PrintWriter writer;
//    private BufferedReader reader;
    
    //Constructor
    public Client() {
        this.connections = new ArrayList<>();
    }
    
    public boolean connectClient(String address, int port) {
        try {
            Socket tempSocket = new Socket(address, port);
            connections.add(tempSocket);
        } catch(Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    
    public File networkSortFile(File fileToSort, File outputFile) {
        System.out.println(this.connections.size());
        //Check if socket is open
        if(this.connections.size() <= 0) {
            return null;
        }
        
        
        LinkedQueue<Integer> queue = new LinkedQueue<>();
        FileReader fileReader = new FileReader(fileToSort, queue);
        
        File[] files = new File[this.connections.size()+1];
        for(int i = 0; i < files.length; i++) {
            files[i] = new File(WINDOWS_PATH_NAME + "temp\\original_"+i);
        }
        
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

        System.out.println("SENDING FILES");
        //Creating sender threads
        Thread[] senderThreads = new Thread[files.length-1];
        
        for(int i = 1; i < files.length; i++) {
            FileSender sender = new FileSender(files[i], this.connections.get(i-1));
            senderThreads[i-1] = new Thread(sender);
            senderThreads[i-1].start();
        }
        
        System.out.println("SORTING START");
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
        
        System.out.println("SEND DONE");
        
        System.out.println("STARTING Reciever");
        
        //Now we want to wait to recieve a file from the different sockets
        FileReceiver[] recievers = new FileReceiver[senderThreads.length];
        File[] outputFiles = new File[senderThreads.length+1];
        Thread[] recieverThreads = new Thread[senderThreads.length];
        for(int i = 0; i < senderThreads.length; i++) {
            outputFiles[i+1] = new File(PATH_NAME + "temp\\output_" + i);
            recievers[i] = new FileReceiver(outputFiles[i+1], this.connections.get(i));
            recieverThreads[i] = new Thread(recievers[i]);
            recieverThreads[i].start();
        }
        
        
        //Now we want to join the sorter thread and copy file into index 0 of outputFiles
        try {
            sorterThread.join();
            System.out.println("SORTING DONE");
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
        
        System.out.println("FILE RECIEVED");
        
        //Now that we have all the files we need to merge them into one file and then 
        FileMerger merger = new FileMerger("final", WINDOWS_PATH_NAME + "temp\\");
        int placeIndex = 0;
        int segEnd = outputFiles.length;
        while(segEnd > 2) {
            placeIndex = 0;
            for(int i = 0; i < segEnd; i+=2) {
                File tempOne = outputFiles[i];
                if (i + 1 >= outputFiles.length) {
                    outputFiles[placeIndex++] = tempOne;
                } else {
                    if (outputFiles[i + 1] == null) {
                        outputFiles[placeIndex++] = tempOne;
                    } else {
                        File tempTwo = outputFiles[i + 1];
                        File mergedFile = merger.mergeFiles(tempOne, tempTwo);
                        outputFiles[placeIndex++] = mergedFile;
                        //tempTwo.delete(); 
                    }
                }
                //Cleaning up old files
                //tempOne.delete();
            }
            segEnd = placeIndex;
        }
        
        //Now we want to merge the last two files into the output file
        merger.mergeFiles(outputFiles[0], outputFiles[1], outputFile);
  
        
        System.out.println("FILES MERGED");
        
        for(int i = 0; i < files.length; i++) {
            files[i].delete();
        }

        return outputFile;
    }
    
    
    public static void main(String[] args) {
        File inputFile = new File("files\\client\\input");
        File outputFile = new File("files\\client\\output");
        
//        String networkAdr = "192.168.1.239";
        String networkAdr = "127.0.0.1";
        Client client = new Client();
        client.connectClient(networkAdr, 3010);
//        client.connectClient("192.168.1.174", 3010);
        
        double start = System.nanoTime();

        outputFile = client.networkSortFile(inputFile, outputFile);
        
        double end = System.nanoTime();
        
        double time = (end - start) / 1_000_000_000.0;
        if(outputFile == null) {
            System.out.println("FAILED");
        } else {
//            System.out.println(outputFile.getName());
        }
        System.out.println("TIME : " + (time));
        System.exit(0);
    }
}
