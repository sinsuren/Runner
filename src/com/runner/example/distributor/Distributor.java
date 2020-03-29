package com.runner.example.distributor;

import com.runner.example.processor.Processor;
import com.runner.example.dto.Message;
import com.runner.example.task.ProcessorTask;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Distributor implements Runnable {

    private BlockingQueue<Message> queue;
    private Processor processor;
    //Because atomicInt automatically handles volatility and Thread safety.
    private AtomicInteger shouldProcess = new AtomicInteger(1);
    int processorParallelism;

    public Distributor(BlockingQueue<Message> queue, Processor processor, int processorParallelism) {
        this.queue = queue;
        this.processor = processor;
        this.processorParallelism = processorParallelism;
    }

    @Override
    public void run() {
        Message message;
        try {
            while (shouldProcess.get() == 1 && ((message = queue.take()) != null)) {

                ProcessorTask processorTask = new ProcessorTask(message);
                processor.getSubProcessors().get(getSubProcessorId(processorTask)).submitTask(processorTask);
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    private String getSubProcessorId(ProcessorTask task) {
        Message message = task.getMessage();

        Integer hashKey = Math.abs(message.getGroupId().hashCode()) % processorParallelism;

        return hashKey.toString();
    }


    public void stopExecution() {
        shouldProcess.incrementAndGet();
    }

    public void haltExecution() {
        shouldProcess.incrementAndGet();
    }
}
