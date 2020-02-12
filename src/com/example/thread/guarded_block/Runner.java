package com.example.thread.guarded_block;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class Runner {
    public static void main(String[] args) {
        BlockingQueue<String> drop = new SynchronousQueue<>();
        (new Thread(new Producer(drop))).start();
        (new Thread(new Consumer(drop))).start();
    }
}
