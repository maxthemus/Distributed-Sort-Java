/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package max.distributed.sort.java;

import java.io.File;



/**
 *  Currently merges two files
 * @author Maxth
 */
public class FileMerger {
    //Fields
    public final String TEMP_FILE_PATH;
    private static int temp_file_count = 0;
    private String name;
    
    //Constructor
    public FileMerger(String name, String tempFilePath) {
        this.name = name;
        this.TEMP_FILE_PATH = tempFilePath;
    }
    
    
    //Methods
    public File mergeFiles(File fileOne, File fileTwo) {
        try {
            //System.out.println("MERGING " + fileOne.getName() + " : " + fileTwo.getName());
            //Setting up readers
            LinkedQueue<Integer>[] queues = (LinkedQueue<Integer>[])(new LinkedQueue[2]);
            FileReader[] readers = new FileReader[2];
            Thread[] readerThreads = new Thread[2];            
            for(int i = 0; i < 2; i++) {
                queues[i] = new LinkedQueue<>();
                readers[i] = new FileReader(((i == 0) ? fileOne : fileTwo), queues[i]);
                readerThreads[i] = new Thread(readers[i]);
                readerThreads[i].start();
            }

            //Now we set up an output queue to write to a file
            File outputFile = new File(TEMP_FILE_PATH + this.name + "_"+ Thread.currentThread().getId() + "_" + this.temp_file_count++);
            LinkedQueue<Integer> outputQueue = new LinkedQueue<>();
            FileWriter writer = new FileWriter(outputFile, outputQueue);
            Thread writerThread = new Thread(writer);
            writerThread.start();
            
            
            //Startin sort               
            while((!queues[0].isDone() || !queues[1].isDone()) || (queues[0].size() > 0 && queues[1].size() > 0)) {
                if(queues[0].size() >= 1 && queues[1].size() >= 1) {
                    if(queues[0].peek() <= queues[1].peek()) {
                        outputQueue.enqueue(queues[0].dequeue());
                    } else {
                        outputQueue.enqueue(queues[1].dequeue());
                    }
                }
            }
                        
            while(queues[0].size() > 0) {
                outputQueue.enqueue(queues[0].dequeue());
            }
            
            while(queues[1].size() > 0) {
                outputQueue.enqueue(queues[1].dequeue());
            }  
            

            outputQueue.setDone(true);
            writerThread.join();
            
            return outputFile;
        } catch(Exception e) {
            System.out.println(e);
            System.exit(3);
            return null;
        }
    }
    
    public File mergeFiles(File fileOne, File fileTwo, File output) {
        try {
            //System.out.println("MERGING " + fileOne.getName() + " : " + fileTwo.getName());
            //Setting up readers
            LinkedQueue<Integer>[] queues = (LinkedQueue<Integer>[])(new LinkedQueue[2]);
            FileReader[] readers = new FileReader[2];
            Thread[] readerThreads = new Thread[2];            
            for(int i = 0; i < 2; i++) {
                queues[i] = new LinkedQueue<>();
                readers[i] = new FileReader(((i == 0) ? fileOne : fileTwo), queues[i]);
                readerThreads[i] = new Thread(readers[i]);
                readerThreads[i].start();
            }

            //Now we set up an output queue to write to a file
            LinkedQueue<Integer> outputQueue = new LinkedQueue<>();
            FileWriter writer = new FileWriter(output, outputQueue);
            Thread writerThread = new Thread(writer);
            writerThread.start();
            
            
            //Startin sort               
            while((!queues[0].isDone() || !queues[1].isDone()) || (queues[0].size() > 0 && queues[1].size() > 0)) {
                if(queues[0].size() >= 1 && queues[1].size() >= 1) {
                    if(queues[0].peek() <= queues[1].peek()) {
                        outputQueue.enqueue(queues[0].dequeue());
                    } else {
                        outputQueue.enqueue(queues[1].dequeue());
                    }
                }
            }
                        
            while(queues[0].size() > 0) {
                outputQueue.enqueue(queues[0].dequeue());
            }
            
            while(queues[1].size() > 0) {
                outputQueue.enqueue(queues[1].dequeue());
            }  
            

            outputQueue.setDone(true);
            writerThread.join();
            
            return output;
        } catch(Exception e) {
            System.out.println(e);
            System.exit(3);
            return null;
        }
    }
    
    
    
    
}
