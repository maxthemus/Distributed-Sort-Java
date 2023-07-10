/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package max.distributed.sort.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Currently merges two files
 * @author Maxth
 */
public class FileMerger {
    //Fields
    private File inputOne;
    private File inputTwo;
    private File output;
    
    
    //Constructor
    public FileMerger(File output, File inputOne, File inputTwo) {
        this.inputOne = inputOne;
        this.inputTwo = inputTwo;
        this.output = output;
    }
    
    
    //Methods
    public boolean mergeFiles() {
        ArrayList<Integer> bufferOne = new ArrayList<>();
        ArrayList<Integer> bufferTwo = new ArrayList<>();
        
        PrintWriter writer;
        Scanner scOne, scTwo;
        try {
            scOne = new Scanner(this.inputOne);
            scTwo = new Scanner(this.inputTwo);
            
            writer = new PrintWriter(this.output);
        } catch (FileNotFoundException ex) {
            return false;
        }
        
        while(scOne.hasNext()) {
            bufferOne.add(scOne.nextInt());
        }
        
        while(scTwo.hasNext()) {
            bufferTwo.add(scTwo.nextInt());
        }
        
        //Closing readers
        scOne.close();
        scTwo.close();
        
        while(bufferOne.size() > 0 && bufferTwo.size() > 0) {
            if(bufferOne.get(0) <= bufferTwo.get(0)) {
                writer.println(bufferOne.get(0));
                bufferOne.remove(0);
            } else {
                writer.println(bufferTwo.get(0));
                bufferTwo.remove(0);
            }
        }
        
        //Clearing buffers
        while(bufferOne.size() > 0) {
            writer.println(bufferOne.get(0));
            bufferOne.remove(0);
        }
        
        while(bufferTwo.size() > 0) {
            writer.println(bufferTwo.get(0));
            bufferTwo.remove(0);
        }
        
        writer.close();
        
        return true;
    }
    
    
    
    
}
