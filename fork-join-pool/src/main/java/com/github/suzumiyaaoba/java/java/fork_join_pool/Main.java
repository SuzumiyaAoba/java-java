package com.github.suzumiyaaoba.java.java.fork_join_pool;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/** ForkJoinPool の基本的な使い方とその効果検証。*/
public class Main {

    public static void main(String[] argv) {
        final var as = IntStream.range(0, 100_000_000)
            .mapToObj(i -> i)
            .collect(Collectors.toList());

        Collections.shuffle(as);

        final var sortTime = measurement(() -> sort(as));
        System.out.println(sortTime);

        final var bs = new ArrayList<>(as);
        final var mergeSortWithForkJoinPool = measurement(() -> mergeSortWithForkJoinPool(bs, Comparator.naturalOrder()));
        System.out.println(mergeSortWithForkJoinPool);

        final var cs = as.stream().mapToLong(i -> i).toArray();
        final var sortActionForkJoinPool = measurement(() -> sortActionForkJoinPool(cs));
        System.out.println(sortActionForkJoinPool);

        final var ds = as.stream().toList();
        final var parallelSort = measurement(() -> ds.parallelStream().sorted().toList());
        System.out.println(parallelSort);
    }

    public static <T> List<T> sort(List<T> xs) {
        return xs.stream().sorted().toList();
    }

    public static <T extends Comparable<? super T>> List<T> mergeSortWithForkJoinPool(List<T> xs, Comparator<T> comparator) {
        final var forkJoinPool = new ForkJoinPool();
        final var result = forkJoinPool.invoke(new MergeSortTask<>(xs, comparator));

        return result;
    }

    public static void sortActionForkJoinPool(long[] xs) {
        final var forkJoinPool = new ForkJoinPool();
        forkJoinPool.invoke(new SortAction(xs));
    }

    public static Duration measurement(Runnable runnable) {
        long start = System.nanoTime();

        runnable.run();

        long end = System.nanoTime();

        return Duration.ofNanos(end - start);
    }
}

