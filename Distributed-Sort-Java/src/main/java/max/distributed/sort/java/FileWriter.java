/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package max.distributed.sort.java;

import java.io.File;
import java.io.PrintWriter;

/**
 *
 * @author max
 */
public class FileWriter implements Runnable {
    //Fields
    private File outputFile;
    private LinkedQueue<Integer> queue;
    
    
    //Constructor
    public FileWriter(File outputFile, LinkedQueue<Integer> queue) {
        this.outputFile = outputFile;
        this.queue = queue;
    }
    
    
    //Methods

    @Override
    public void run() {
        try {
            //System.out.println(Thread.currentThread().getId() + " has started writing to " + this.outputFile.getName());
            
            PrintWriter writer = new PrintWriter(this.outputFile);
                        
            while(!this.queue.isDone()) {
                if(this.queue.size() >= 1) {
                    int value = this.queue.dequeue();
                    writer.println(value);
                } else {
                    Thread.yield();
                }
            }
            
            //Finished writing rest of queue
            while(this.queue.size() >= 1) {
                int value = this.queue.dequeue();
                writer.println(value);
            }
            
            writer.close();
            
            //System.out.println(Thread.currentThread().getId() + " has finished writing to " + this.outputFile.getName());
        } catch(Exception e) {
            System.out.println(e);
            System.exit(1);
            return;
        }
    }
    
    
    
}
