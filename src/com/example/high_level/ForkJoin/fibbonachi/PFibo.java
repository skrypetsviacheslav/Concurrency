package com.example.high_level.ForkJoin.fibbonachi;

import java.util.concurrent.ForkJoinPool;

public class PFibo extends Thread {
    private int x;
    public long answer;

    public PFibo(int x) {
        this.x = x;
    }

    public void run() {
        if (x <= 2)
            answer = 1;
        else {
            try {
                PFibo t = new PFibo(x - 1);
                t.start();

                long y = RFibo(x - 2);

                t.join();
                answer = t.answer + y;

            } catch (InterruptedException ex) {
            }
        }
    }


    public static long RFibo(int no) {
        if (no == 1 || no == 2) {
            return 1;
        }
        return RFibo(no - 1) + RFibo(no - 2);
    }

    public static void main(String[] args) throws Exception {
        try {
            int number = 47;
//-----------------------------------------------------------------------------------------------------------------------
            long start = System.currentTimeMillis();
            PFibo f = new PFibo(number);
            f.start();
            f.join();
            long end = System.currentTimeMillis();
            System.out.println("Parallel-Fibonacci:" + f.answer + "\tTime:" + (end - start));
//-----------------------------------------------------------------------------------------------------------------------
            start = System.currentTimeMillis();
            long result = RFibo(number);
            end = System.currentTimeMillis();
            System.out.println("Normal-Fibonacci:" + result + "\tTime:" + (end - start));
//-----------------------------------------------------------------------------------------------------------------------
            ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
            Fib fib = new Fib(number);
            forkJoinPool.execute(fib);
            start = System.currentTimeMillis();
            Integer ForkJoinResult = fib.join();
            end = System.currentTimeMillis();
            System.out.println("ForkJoin-Fibonacci:" + result + "\tTime:" + (end - start));
        } catch (Exception e) {
        }
    }
}