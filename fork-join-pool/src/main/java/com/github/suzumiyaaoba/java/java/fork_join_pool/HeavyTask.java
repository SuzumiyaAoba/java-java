package com.github.suzumiyaaoba.java.java.fork_join_pool;

import java.util.concurrent.RecursiveTask;

public class HeavyTask extends RecursiveTask<Void> {

    private long millis;

    public HeavyTask(long millis) {
        this.millis = millis;
    }

    @Override
    protected Void compute() {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }
}
