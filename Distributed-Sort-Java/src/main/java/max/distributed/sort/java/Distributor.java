/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package max.distributed.sort.java;

import java.io.File;


/**
 *
 * @author max
 */
public class Distributor implements Runnable {
    //Fields
    private final LinkedQueue<Integer> inputQueue;
    private File[] tempFiles;
    
    //Constructor
    public Distributor(LinkedQueue<Integer> inputQueue, File[] outputFiles) {
            this.inputQueue = inputQueue;
            this.tempFiles = outputFiles;
    }
    
    
    
    //Methods
    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getId() + " has started distributor");
            //Creating queues for output
            LinkedQueue<Integer>[] queues = (LinkedQueue<Integer>[])new LinkedQueue[this.tempFiles.length];
            //Creating file writers for the files
            FileWriter[] writers = new FileWriter[this.tempFiles.length];
            Thread[] threads = new Thread[this.tempFiles.length]; 
                        
            //Creating objects
            for(int i = 0; i < this.tempFiles.length; i++) {
                queues[i] = new LinkedQueue<>();
                writers[i] = new FileWriter(this.tempFiles[i], queues[i]);
            }
            
            //Creating and starting threads
            for(int i = 0; i < threads.length; i++) {
                threads[i] = new Thread(writers[i]);
                threads[i].start();
            }            
            
            int indexPtr = 0;
            while(!this.inputQueue.isDone()) {
                if(this.inputQueue.size() >= 1) {
                    queues[indexPtr++].enqueue(this.inputQueue.dequeue());
                }
                
                if(indexPtr >= queues.length) {
                    indexPtr = 0;
                }
            }
            
            
            //Setting all output queues as done
            for(int i = 0; i < queues.length; i++) {
                queues[i].setDone(true);
            }
            
            
            //Waiting for threads to join
            for(int i = 0; i < threads.length; i++) {
                threads[i].join();
            }
            
            
            System.out.println(Thread.currentThread().getId() + " has finished distributor");
        } catch(Exception e) {
            System.out.println(e);
            System.exit(1);
            return;
        }
    }
}
