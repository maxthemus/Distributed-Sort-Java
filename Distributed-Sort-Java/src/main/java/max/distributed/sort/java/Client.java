/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package max.distributed.sort.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;


/**
 *
 * @author Maxth
 */
public class Client {
    //Fields
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
    
    

    
    public boolean networkSortFile(File fileToSort, File outputFile) {
        //Check if socket is open
        if(this.socket == null) {
            return false;
        }
        
        //Now we want to set up the threads
        ArrayList<Integer> localArray = new ArrayList<>();
        LinkedQueue<Integer> queue = new LinkedQueue<>();
        FileReader fileReader = new FileReader(fileToSort, queue);
        Distributor distributor = new Distributor(socket, queue, localArray);
        
        Thread readerThread = new Thread(fileReader);
        Thread senderThread = new Thread(distributor);
        
        readerThread.start();
        senderThread.start();
        
        try {
//            readerThread.join();
            senderThread.join();
        } catch(Exception e) {
            System.out.println(e);
            return false;
        }
        System.out.println("File Read and Sent");
        
        
        localArray = sortArray(localArray);
        
        File recvFile = new File("files/client/temp/recv");
        try {
            
            FileReceiver fileReciever = new FileReceiver(outputFile, this.socket, new BufferedReader(new InputStreamReader(this.socket.getInputStream())));
        } catch(Exception e) {
            System.out.println(e);
        }
        
        
        return true;
    }
    
    private ArrayList<Integer> sortArray(ArrayList<Integer> array) {
        for(int i = 0; i < array.size(); i++) {
            for(int j = 1; j < (array.size() - i); j++) {
                if(array.get(j-1) > array.get(j)) {
                    int temp = array.get(j-1);
                    array.set(j-1, array.get(j));
                    array.set(j, temp);
                }
            }
        }
        
        return array;
    }
    
    
    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 3010);
        client.networkSortFile(new File("files/client/input"), new File("files/client/output"));
    }
}
