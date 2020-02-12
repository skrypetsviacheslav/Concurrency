package com.example.high_level.ForkJoin.fibbonachi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class Fib extends RecursiveTask<Integer> {
    private static final int THRESHOLD = 13;
    volatile int number;

    public Fib(int number) {
        this.number = number;
    }

    @Override
    protected Integer compute() {
        if (number > THRESHOLD) {
            return ForkJoinTask.invokeAll(createSubTasks())
                    .stream()
                    .mapToInt(ForkJoinTask::join)
                    .sum();
        } else {
            return seqFib(number);
        }
    }

    private Collection<Fib> createSubTasks() {
        List<Fib> dividedTasks = new ArrayList<>();
        dividedTasks.add(new Fib(number - 1));
        dividedTasks.add(new Fib(number - 2));
        return dividedTasks;
    }


    int seqFib(int n) {
        if (n <= 1) return n;
        else return seqFib(n - 1) + seqFib(n - 2);
    }

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

        int number = 45;
        Fib fib = new Fib(number);

        forkJoinPool.execute(fib);
        long start = System.currentTimeMillis();
        Integer ForkJoinResult = fib.join();
        long end = System.currentTimeMillis();

        System.out.printf("ForkJoin Fibonacci function of %d = %d\n", number, ForkJoinResult);
        System.out.println("Execution of ForkJoin " + " took "
                + (end - start) + "msec.");


        start = System.currentTimeMillis();
        Integer OneThreadResult = fib(number);
        end = System.currentTimeMillis();
        System.out.printf("Default Fibonacci function of %d = %d\n", number, OneThreadResult);
        System.out.println("Execution of Default " + " took "
                + (end - start) + "msec.");
    }

    static int fib(int n) {
        if (n <= 1)
            return n;
        return fib(n - 1) + fib(n - 2);
    }
}
