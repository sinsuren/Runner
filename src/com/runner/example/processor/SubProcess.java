package com.runner.example.processor;

import com.runner.example.core.SubProcessorWaitPolicy;
import com.runner.example.providers.ThreadPoolExecutorProvider;
import com.runner.example.task.ProcessorTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class SubProcess {

    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    String id;
    int queueSize;
    ThreadPoolExecutor threadPoolExecutor;


    public SubProcess(int queueSize, String id) {
        this.id = id;
        this.queueSize = queueSize;
        threadPoolExecutor = ThreadPoolExecutorProvider.getExecutor("SubProcessor_" + this.id, this.id,
                1, 1, 1,
                this.queueSize, new SubProcessorWaitPolicy(), logger);
    }

    public void submitTask(ProcessorTask task) {
        threadPoolExecutor.submit(task);
    }


    public void shutdown() {
        try {
            BlockingQueue<Runnable> queue = threadPoolExecutor.getQueue();

            List<Runnable> list = new ArrayList<>();

            queue.drainTo(list);

            //Now try shutting down the threadpool executor

            if (threadPoolExecutor != null) {

                threadPoolExecutor.shutdown();

                if (!threadPoolExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                    threadPoolExecutor.shutdownNow();
                }
            }

            logger.info("Thread Pool executor for subprocessor has been stopped");
        } catch (Exception e) {
            logger.info("Failed to shutdown logger");
        }
    }


    public void halt() {
        shutdown();
    }
}
