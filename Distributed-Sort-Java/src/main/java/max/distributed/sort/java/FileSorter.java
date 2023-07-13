/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package max.distributed.sort.java;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author Maxth
 */
public class FileSorter implements Runnable {
    //Fields
    private final static int MIN_FILE_SIZE = 5;
    private final String TEMP_FILE_PATH;
    private File outputFile;
    private File fileToSort;
    private int temp_file_count;
    private boolean done;
    
    
    //Constructor
    public FileSorter(File fileToSort, String tempFileFolder) {
        this.outputFile = null;
        this.fileToSort = fileToSort;
        this.temp_file_count = 1;
        this.done = false;
        
        this.TEMP_FILE_PATH = tempFileFolder;
    }
    
    
    //Methods
    @Override
    public void run() {
        try {
            int count = 0;
            //Setting up queue
            LinkedQueue<Integer> queue = new LinkedQueue<>();
            FileReader fileReader = new FileReader(fileToSort, queue);
            Thread readerThread = new Thread(fileReader);
            readerThread.start();
            
            
            
            ArrayList<File> fileArray = new ArrayList<>();
            File tempFile = new File(TEMP_FILE_PATH + Thread.currentThread().getId() + "_" + this.temp_file_count++);
            
            //Creating original files
            int[] buffer = new int[MIN_FILE_SIZE];
            int currentValueCount = 0;
            boolean fileAdded = false;
            while(!queue.isDone()) {
                while(queue.size() >= 1) {
                    buffer[currentValueCount++] = queue.dequeue();
                    fileAdded = false;
                    
                    if(currentValueCount >= MIN_FILE_SIZE) {
                        //We want to sort the buffer
                        buffer = this.sortArray(buffer, currentValueCount);
                        
                        //Then read it into the tempFile
                        this.writeFile(buffer, currentValueCount, tempFile);
                        
                        currentValueCount = 0;
                        buffer = new int[MIN_FILE_SIZE];
                        
                        fileArray.add(tempFile);
                        tempFile = new File(TEMP_FILE_PATH + Thread.currentThread().getId() + "_" + this.temp_file_count++);
                        fileAdded = true;
                    }
                }
            }
            
            
            //When the queue is done we want to double check that the queue is empty
            while(queue.size() >= 1) {
                buffer[currentValueCount++] = queue.dequeue();
                fileAdded = false;
                
                if(currentValueCount >= MIN_FILE_SIZE) {
                    buffer = this.sortArray(buffer, currentValueCount);
                    
                    //Then read it into the tempFile
                    this.writeFile(buffer, currentValueCount, tempFile);
                    
                    currentValueCount = 0;
                    buffer = new int[MIN_FILE_SIZE];

                    fileArray.add(tempFile);
                    tempFile = new File(TEMP_FILE_PATH + Thread.currentThread().getId() + "_" + this.temp_file_count++);
                    fileAdded = true;
                }
            }
            
            //Checking to see if the buffer is half empty with file needing to be empty
            if(!fileAdded) {
                //We must add final file
                buffer = this.sortArray(buffer, currentValueCount);
                    
                //Then read it into the tempFile
                this.writeFile(buffer, currentValueCount, tempFile);

                currentValueCount = 0;
                buffer = new int[MIN_FILE_SIZE];

                fileArray.add(tempFile);
                tempFile = new File(TEMP_FILE_PATH + Thread.currentThread().getId() + "_" + this.temp_file_count++);
            }
            
            
            
            //Once all files have been written into the fileArray we want to start the merging process
            FileMerger merger = new FileMerger("merger", this.TEMP_FILE_PATH);
            while(fileArray.size() > 1) {
                File tempOne = fileArray.remove(0);
                File tempTwo = fileArray.remove(0);
                    
                File mergedFile = mergeFiles(tempOne, tempTwo);
                fileArray.add(mergedFile);
                
                //Clean up old files
                tempOne.delete();
                tempTwo.delete();
            }
            
            this.outputFile = fileArray.get(0);
            this.done = true;
        } catch(Exception e) {
            System.out.println(e);
            System.out.println("HERE");
            System.exit(2);
        }
    }
    
    
    public File mergeFiles(File fileOne, File fileTwo) {
        try {
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
            File outputFile = new File(TEMP_FILE_PATH + Thread.currentThread().getId() + "_" + this.temp_file_count++);
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
    
    
    //COULD MULTI THREAD THIS USING FILE WRITER BUT IM NOT SURE IF IT WILL
    //INCREASE PERFROMANCE
    private void writeFile(int[] array, int valueCount, File file) {
        try {
            PrintWriter writer = new PrintWriter(file);
            
            for(int i = 0; i < valueCount; i++) {
                writer.println(array[i]);
                
            }
            
            writer.close();
        } catch(Exception e) {
            System.out.println(e);
            System.exit(1);
            return;
        }
    }
    
    
    //Must check null is used
    public File getOutputFile() {
        return this.outputFile;
    }
    
    //Insertion sort because extremely small data set
    private int[] sortArray(int[] arr, int n) {
        for (int i = 1; i < n; ++i) {
            int key = arr[i];
            int j = i - 1;
 
            /* Move elements of arr[0..i-1], that are
               greater than key, to one position ahead
               of their current position */
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }
        return arr;
    }
    
    public boolean getDone() {
        return this.done;
    }
    
    
    //Main Method
//    public static void main(String[] args) {
//        File fileToSort = new File("files/client/input");
//        FileSorter sorter = new FileSorter(fileToSort);
//        Thread thread = new Thread(sorter);
//        
//        thread.start();
//
//        
//        try {
//            thread.join();
//        } catch (InterruptedException ex) {
//            Logger.getLogger(FileSorter.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        System.out.println("DONE");
//        System.out.println(sorter.getOutputFile().getName());
//    }
    
}