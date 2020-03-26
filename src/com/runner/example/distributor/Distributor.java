package com.runner.example.distributor;

import com.runner.example.consumer.ConsumerManager;
import com.runner.example.consumer.ConsumerThread;
import com.runner.example.dto.Message;

import java.util.concurrent.LinkedBlockingQueue;

public class Distributor {

    private LinkedBlockingQueue<Message> queue;
    private ConsumerManager consumerManager;
    private int parallelismDegree;

    public Distributor(LinkedBlockingQueue<Message> queue, int processingParallelism) {
        this.queue = queue;
        this.parallelismDegree = processingParallelism;
        this.consumerManager = new ConsumerManager(processingParallelism);
    }

    public void start() throws InterruptedException {
        consumerManager.start();

        while (true) {
            Message message = queue.take();

            int hashFunctionOutput = message.getGroupId().hashCode() % parallelismDegree;

            ConsumerThread thread = consumerManager.get(hashFunctionOutput);
            thread.push(message);
        }
    }
}
