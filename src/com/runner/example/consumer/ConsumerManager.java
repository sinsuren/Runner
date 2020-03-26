package com.runner.example.consumer;

import java.util.HashMap;
import java.util.Map;

public class ConsumerManager {

    private Map<Integer, Thread> consumerThreadMap = new HashMap<>();
    private Map<Integer, ConsumerThread> consumerThreadRunnable = new HashMap<>();

    public ConsumerManager(int consumerThreadCount) {

        for (int i = 0; i < consumerThreadCount; i++) {
            ConsumerThread consumerThread = new ConsumerThread(i);
            Thread thread = new Thread(consumerThread);

            consumerThreadRunnable.put(i, consumerThread);
            consumerThreadMap.put(i, thread);

        }
    }

    public void start() {

        for (Thread thread : consumerThreadMap.values()) {
            thread.start();
        }
    }

    public ConsumerThread get(Integer i) {
        return consumerThreadRunnable.get(i);
    }

}
