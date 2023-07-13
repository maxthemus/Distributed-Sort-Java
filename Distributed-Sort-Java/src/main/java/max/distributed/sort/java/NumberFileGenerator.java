/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package max.distributed.sort.java;

import java.io.File;
import java.io.PrintWriter;

/**
 *
 * @author Maxth
 */
public class NumberFileGenerator {
    //Fields
    private final int MAX_VALUE;
    private final int MIN_VALUE;
    private final int NUM_VALUES;
    private final File output;
    
    
    //Constructor
    public NumberFileGenerator(int MAX_VALUE, int MIN_VALUE, int NUM_VALUES, File output) {
        this.MAX_VALUE = MAX_VALUE;
        this.MIN_VALUE = MIN_VALUE;
        this.NUM_VALUES = NUM_VALUES;
        this.output = output;
        
        this.createFile();
    }

    //Methods
    private void createFile() {
        try {
            PrintWriter pw = new PrintWriter(this.output);
            
            for(int i = 0; i < NUM_VALUES; i++) {
                int randomInt = (int)(Math.random() * (MAX_VALUE - MIN_VALUE + 1) + MIN_VALUE);
                pw.println(randomInt);
            }
            
            pw.close();
        } catch(Exception e) {
            System.out.println(e);
            System.exit(1);
        }
        System.out.println("Numbers loaded onto : " + this.output.toPath());
    }
    
    //Main Method
    public static void main(String[] args) {
        File output = new File("files/client/input");
        NumberFileGenerator gen = new NumberFileGenerator(1000, 0, 100000, output);
    }
}
