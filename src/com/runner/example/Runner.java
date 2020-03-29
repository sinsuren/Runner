package com.runner.example;

import com.runner.example.distributor.Distributor;
import com.runner.example.dto.Message;
import com.runner.example.processor.Processor;
import com.runner.example.reader.Reader;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Runner {

    private String runnerId;
    Reader reader;
    Distributor distributor;
    Processor processor;
    int readerMainQueueSize;
    int readerParallelismDegree;
    int processorParallelismDegree;
    int processorQueueSize;
    BlockingQueue<Message> mainQueue;
    private Boolean isRunning = false;

    public Runner(String runnerId, int readerMainQueueSize, int readerParallelismDegree, int processorParallelismDegree,
                  int processorQueueSize) {
        this.runnerId = runnerId;
        this.readerMainQueueSize = readerMainQueueSize;
        this.readerParallelismDegree = readerParallelismDegree;
        this.processorParallelismDegree = processorParallelismDegree;
        this.processorQueueSize = processorQueueSize;
    }

    public void init() {
        synchronized (this) {
            if (!isRunning) {
                try {
                    initializeRunner();
                } catch (Exception e) {
                    throw e;
                }
            }
        }
    }

    private void initializeRunner() {

        mainQueue = new LinkedBlockingQueue<>(readerMainQueueSize);

        this.processor = new Processor("Processor_" + runnerId, processorParallelismDegree, processorQueueSize);

        this.distributor = new Distributor(mainQueue, processor, processorParallelismDegree);
        //Chances that this thread might stop
        Thread distributorThread = new Thread(distributor, "Distributor_Thread");
        distributorThread.start();


        this.reader = new Reader(runnerId, mainQueue);

        Thread readerThread = new Thread(reader);
        readerThread.start();

        isRunning = true;
    }

    public void stopRunner() {
        synchronized (this) {
            this.reader.stop();
            this.distributor.stopExecution();
            this.processor.stopExecution();
            this.isRunning = false;
        }
    }
}
