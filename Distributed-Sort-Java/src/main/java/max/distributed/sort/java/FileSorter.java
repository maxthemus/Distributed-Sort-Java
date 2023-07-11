/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package max.distributed.sort.java;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Maxth
 */
public class FileSorter implements Runnable {
    //Fields
    private File outputFile;
    private File fileToSort;
    private final static int MIN_FILE_SIZE = 5;
    private int temp_file_count;
    
    
    //Constructor
    public FileSorter(File outputFile, File fileToSort) {
        this.outputFile = outputFile;
        this.fileToSort = fileToSort;
        this.temp_file_count = 1;
    }
    
    
    //Methods
    @Override
    public void run() {
        try {
            //Setting up queue
            LinkedQueue<Integer> queue = new LinkedQueue<>();
            FileReader fileReader = new FileReader(fileToSort, queue);
            Thread readerThread = new Thread();
            readerThread.start();
            
            
            ArrayList<File> fileArray = new ArrayList<>();
            File tempFile = new File("files/client/temp/"+Thread.currentThread().getId() + "_" + this.temp_file_count++);
            int currentValueCount = 0;
            while(!queue.isDone()) {
                while(queue.size() >= 1) {
                    int value = queue.dequeue();
                    
                    
                }
            }
            
            
            
        } catch(Exception e) {
            System.out.println(e);
            System.exit(1);
        }
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
    
    
    private File mergeFiles(File fileOne, File fileTwo) {
        
        return null;
    }
    
}
