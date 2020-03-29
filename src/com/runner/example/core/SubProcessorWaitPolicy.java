package com.runner.example.core;

import com.runner.example.processor.SubProcess;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SubProcessorWaitPolicy implements RejectedExecutionHandler {


    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        try {
            while (!executor.isShutdown() && !executor.getQueue().offer(r, 1, TimeUnit.SECONDS)) {
                Thread.sleep(100);
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();

            throw new RejectedExecutionException(ie);
        }
    }
}
