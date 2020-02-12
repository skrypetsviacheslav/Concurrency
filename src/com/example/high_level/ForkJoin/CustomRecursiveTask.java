package com.example.high_level.ForkJoin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class CustomRecursiveTask extends RecursiveTask<Integer> {
    private int[] arr;

    private static final int THRESHOLD = 20;

    public CustomRecursiveTask(int[] arr) {
        this.arr = arr;
    }

    @Override
    protected Integer compute() {
        if (arr.length > THRESHOLD) {
            return ForkJoinTask.invokeAll(createSubtasks())
                    .stream()
                    .mapToInt(ForkJoinTask::join)
                    .sum();
        } else {
            return processing(arr);
        }
    }

    private Collection<CustomRecursiveTask> createSubtasks() {
        List<CustomRecursiveTask> dividedTasks = new ArrayList<>();
        dividedTasks.add(new CustomRecursiveTask(
                Arrays.copyOfRange(arr, 0, arr.length / 2)));
        dividedTasks.add(new CustomRecursiveTask(
                Arrays.copyOfRange(arr, arr.length / 2, arr.length)));
        return dividedTasks;
    }

    private Integer processing(int[] arr) {
        return Arrays.stream(arr)
                .filter(a -> a > 10 && a < 27)
                .map(a -> a * 10)
                .sum();
    }


    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

        int[] arr = IntStream.generate(() -> ThreadLocalRandom.current().nextInt(0, 100))
                .limit(100)
                .toArray();

        CustomRecursiveTask customRecursiveTask = new CustomRecursiveTask(arr);

        forkJoinPool.execute(customRecursiveTask);
        int result = customRecursiveTask.join();
        System.out.printf("First Fork/Join pool is Done(%s) and get %d\n", customRecursiveTask.isDone(), result);

        forkJoinPool.submit(customRecursiveTask);
        int resultTwo = customRecursiveTask.join();
        System.out.printf("Second Fork/Join pool is Done(%s) and get %d\n", customRecursiveTask.isDone(), resultTwo);
    }
}
