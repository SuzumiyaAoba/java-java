package com.github.suzumiyaaoba.java.java.fork_join_pool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class MergeSortTask<T extends Comparable<? super T>, U extends Comparator<T>> extends RecursiveTask<List<T>> {

    /** ソート対象リスト。 */
    private final List<T> xs;

    private final Comparator<T> comparator;

    public MergeSortTask(List<T> xs, Comparator<T> comparator) {
        this.xs = xs;
        this.comparator = comparator;
    }

    @Override
    protected List<T> compute() {
        // リストが空、もしくは要素数が 1 の場合はソート済みとして返す
        if (xs.size() <= 1) {
            return xs;
        }

        // リストを左右に分割
        final var mid = Math.floorDiv(xs.size(), 2);
        final var left = new MergeSortTask<>(xs.subList(0, mid), comparator);
        final var right = new MergeSortTask<>(xs.subList(mid, xs.size()), comparator);

        // 右側のソートの非同期実行を調整
        right.fork();

        final var sortedLeft = left.compute();
        final var sortedRight = right.join();

        final var sorted = new ArrayList<T>();
        int l = 0, r = 0;
        while (l < sortedLeft.size() && r < sortedRight.size()) {
            final var x = sortedLeft.get(l);
            final var y = sortedRight.get(r);

            if (comparator.compare(x, y) <= 0) {
                sorted.add(x);
                l++;
            } else {
                sorted.add(y);
                r++;
            }
        }

        while (l < sortedLeft.size()) {
            sorted.add(sortedLeft.get(l++));
        }

        while (r < sortedRight.size()) {
            sorted.add(sortedRight.get(r++));
        }

        return Collections.unmodifiableList(sorted);
    }
}

