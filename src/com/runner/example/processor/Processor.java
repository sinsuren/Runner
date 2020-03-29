package com.runner.example.processor;

import com.runner.example.dto.Message;
import com.runner.example.task.ProcessorTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Processor {

    String processorId;
    int subProcessorCount;
    int subProcessorQueueSize;
    Map<String, SubProcess> subProcessors;


    public Processor(String processorId, int subProcessorCount, int subProcessorQueueSize) {

        this.processorId = processorId;
        this.subProcessorCount = subProcessorCount;
        this.subProcessorQueueSize = subProcessorQueueSize;
        subProcessors = new HashMap<>();

        for (Integer index = 0; index < subProcessorCount; index++) {
            String subProcessorName = processorId + "_SP_" + index.toString();

            SubProcess subProcess = new SubProcess(subProcessorQueueSize, subProcessorName);

            subProcessors.put(index.toString(), subProcess);
        }
    }

    public Map<String, SubProcess> getSubProcessors() {
        return subProcessors;
    }

    public void stopExecution() {
        //New Thread pool to stop
        ExecutorService executorService = Executors.newFixedThreadPool(Math.min(50, subProcessorCount));

        if (subProcessors != null && subProcessors.size() != 0) {
            List<Future<?>> futures = new ArrayList<>();

            for (SubProcess subProcess : subProcessors.values()) {
                futures.add(executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        subProcess.shutdown();
                    }
                }));
            }

            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }

        executorService.shutdown();
        //Clear the map as well
        subProcessors.clear();

    }

    public void haltExecution() {
        stopExecution();
    }
}
