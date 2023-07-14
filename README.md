# Distributed-Sort-Java

This project is a basic distributed sort that uses merge sort on multiple machines cocurrently to increase the speed of sorting a large integer array that is stored in a text file.
To increase speeds I have done a basic implementation of multithreading not using a thread pool but by creating threads and then having them terminate after performance their specific task.
Threads are for I/O events like Reading and Writing files, and Reading and Writing to socket connections.
