/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package standard_sort;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Maxth
 */
public class BubbleSorter {
    //Fields
    
    
    //Constructor
    public boolean sortFile(File input, File output) {
        ArrayList<Integer> buffer = new ArrayList<>();
        
        PrintWriter writer;
        Scanner sc;
        try {
            writer = new PrintWriter(output);
            sc = new Scanner(input);
        } catch(Exception e) {
            System.out.println(e);
            return false;
        }
        
        //Reading from file
        while(sc.hasNext()) {
            buffer.add(sc.nextInt());
        }
        sc.close();
        
        buffer = sortArray(buffer);
        
        //Writing back to file
        for(int i = 0; i < buffer.size(); i++) {
           writer.println(buffer.get(i)); 
        }
        
        writer.close();
        return true;
    }
    
    
    //Methods
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
    
    //Main Method
    public static void main(String[] args) {
        BubbleSorter sorter = new BubbleSorter();
        sorter.sortFile(new File("files\\client\\input"), new File("files\\client\\localoutput"));
        
    }
}
