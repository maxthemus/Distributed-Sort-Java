/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.maxthemus.client;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 *
 * @author Maxth
 */
public class FileSpliter {
    //Fields
    private File inputFile;
    private File[] outputFiles;
    
    
    //Constructor
    public FileSpliter(File inputFile, File[] outputFiles) {
        this.inputFile = inputFile;
        this.outputFiles = outputFiles;
    }
    
    
    //Methods
    public boolean readAndWriteToFiles() {
        Scanner sc;
        PrintWriter[] writers = new PrintWriter[this.outputFiles.length];
        try {
            sc = new Scanner(this.inputFile);
            
            for(int i = 0; i < this.outputFiles.length; i++) {
                writers[i] = new PrintWriter(this.outputFiles[i]);
            }
        } catch(Exception e) {
            System.out.println(e);
            return false;
        }
        
        
        
        int outputIndex = 0;
        int maxIndex = this.outputFiles.length;
        while(sc.hasNext()) {
            int number = sc.nextInt();
            writers[outputIndex++].println(number);
            
            if(outputIndex >= maxIndex) {
                outputIndex = 0;
            }
        }
        
        for(int i = 0; i < writers.length; i++) {
            writers[i].close();
        }
        sc.close();
        

        return true;
    }
    
    public static void main(String[] args) {
        File[] outputFiles = new File[2];
        outputFiles[0] = new File("files/client/tmp/one");
        outputFiles[1] = new File("files/client/tmp/two");
        FileSpliter spliter = new FileSpliter(new File("files/client/input"), outputFiles);
        
        spliter.readAndWriteToFiles();
    }

}
