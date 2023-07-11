/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package max.distributed.sort.java;

import java.io.File;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author max
 */
public class FileReader implements Runnable {
    //Fields
    private File fileToRead;
    private LinkedQueue<Integer> queue;
    
    
    //Constructor
    public FileReader(File fileToRead, LinkedQueue<Integer> queue) {
        this.fileToRead = fileToRead;
        this.queue = queue;
    }
    
    
    
    //Methods
    @Override
    public void run() {
        try {
            System.out.println("Starting to read File");
            
            Scanner reader = new Scanner(this.fileToRead);
            
            while(reader.hasNext()) {
                queue.enqueue(reader.nextInt());
            }
            
            queue.setDone(true);
            
            reader.close();
            
            System.out.println("Finished reading file");
        } catch (Exception ex) {
            System.out.println(ex);
            System.exit(0);
        }
    }
    
    
    
    //Main Method
    public static void main(String[] args) {
        LinkedQueue<Integer> queue = new LinkedQueue<>();
        File file = new File("files/client/input");
        FileReader reader = new FileReader(file, queue);
        
        Thread thread = new Thread(reader);
        thread.start();
        
        try {
            thread.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(FileReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(queue.size());
        
    }
    
    
}
