/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package max.distributed.sort.java;

/**
 *
 * @author max
 */
public class LinkedQueue<E> {
    //Fields
    private Node<E> headNode;
    private Node<E> tailNode;
    private int size;
    private boolean done;
    
    
    //Constructor
    public LinkedQueue() {
        this.done = false;
        this.size = 0;
        this.headNode = null;
        this.tailNode = null;
    }
    
    
    //Methods
    public synchronized int size() {
        return this.size;
    }
    
    public synchronized void enqueue(E value) {
        Node<E> tempNode = new Node<>(value);
        
        if(this.headNode == null) {
            this.headNode = tempNode;
        } else {
            this.tailNode.next = tempNode;
        }
        
        this.tailNode = tempNode;
        this.size++;
    } 
    
    public synchronized E dequeue() {
        //Checking for empty array
        if(this.headNode == null) {
            return null;
        } 
        
        Node<E> returnNode = this.headNode;
        if(returnNode.next == null) {
            this.headNode = null;
            this.tailNode = null;
        } else {
            this.headNode = this.headNode.next;
        }
        
        this.size--;
        return returnNode.value;
    }
    
    public synchronized String toString() {
        String tempString = "<-- ";
        Node<E> nodePtr = this.headNode;
        
        for(int i = 0; i < this.size; i++) {
            tempString += nodePtr.value;
            if(i < this.size-1) {
                tempString += ", ";
            }
            
            nodePtr = nodePtr.next;
        }
        
        return tempString;
    }
    
    public E peek() {
        return this.headNode.value;
    }

    
    public synchronized boolean isDone() {
        return done;
    }

    public synchronized void setDone(boolean done) {
        this.done = done;
    }
    
    
    
    
    
    //Private Inner Class
    private class Node<E> {
        public E value;
        public Node<E> next;

        public Node(E value) {
            this.value = value;
        }
    }
    
    //Main Method
    public static void main(String[] args) {
        LinkedQueue<Integer> queue = new LinkedQueue<>();
        
        for(int i = 0; i < 5; i++) {
            queue.enqueue(i);
        }
        
        queue.dequeue();
        System.out.println(queue.size());
        
        System.out.println(queue.toString());
    }
}
