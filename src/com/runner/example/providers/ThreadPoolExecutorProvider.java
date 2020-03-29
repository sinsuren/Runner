package com.runner.example.providers;

import java.util.concurrent.*;
import java.util.logging.Logger;

public class ThreadPoolExecutorProvider {
    public static ThreadPoolExecutor getExecutor(final String threadGroup, final String executorGroup, int corePoolSize, int maxPoolSize,
                                                 long keepAliveTime, int queueSize, RejectedExecutionHandler rejectedExecutionHandler, final Logger logger) {

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(queueSize), new ThreadFactory() {
            ThreadGroup group = new ThreadGroup(threadGroup);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(group, r, executorGroup + "_" + threadGroup);
            }
        }, rejectedExecutionHandler) {

            // See link: https://stackoverflow.com/questions/35366018/overriding-threadpoolexecutor-afterexecute-method-any-cons
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                if (t == null && r instanceof Future<?>) {
                    try {
                        ((Future<?>) r).get();
                    } catch (CancellationException ce) {
                        t = ce;
                    } catch (ExecutionException ee) {
                        t = ee.getCause();
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt(); // ignore/reset
                    }
                }

                if (t != null) {
                    System.exit(-1);
                }
            }
        };

        threadPoolExecutor.prestartAllCoreThreads();
        return threadPoolExecutor;
    }
}
