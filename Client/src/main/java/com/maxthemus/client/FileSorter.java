/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.maxthemus.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Maxth
 */
public class FileSorter {
    //Fields
    private File outputFile;
    private File fileToSort;
    
    
    //Constructor
    public FileSorter(File outputFile, File fileToSort) {
        this.outputFile = outputFile;
        this.fileToSort = fileToSort;
    }
    
    
    //Methods
    public boolean sortFile() {
        Scanner sc;
        try {
            sc = new Scanner(this.fileToSort);
        } catch (FileNotFoundException ex) {
            return false;
        }
        
        ArrayList<Integer> buffer = new ArrayList<>();
        
        //Reading file into buffer
        while(sc.hasNextInt()) {
            buffer.add(sc.nextInt());
        }
        sc.close(); //Closing scanner
        
        //Sorting array
        buffer = sortArray(buffer);
        
        //Opening print writer
        PrintWriter writer;
        try {
            writer = new PrintWriter(this.outputFile);
        } catch(Exception e) {
            System.out.println(e);
            return false;
        }
        
        //Writing to output array
        for(int i = 0; i < buffer.size(); i++) {
            writer.println(buffer.get(i));
        }
        
        writer.close();

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
    
}
