package com.runner.example.reader;

import com.runner.example.dto.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class ReaderManager {

    private LinkedBlockingQueue<Message> queue;
    private int readerParallelism;
    private List<ReaderThread> readerThreadList;

    public ReaderManager(LinkedBlockingQueue<Message> messageQueue, int readerParallelism) {
        this.queue = messageQueue;
        this.readerParallelism = readerParallelism;
        this.readerThreadList = new ArrayList<>();
    }

    public void start() {
        for (int i = 0; i < readerParallelism; i++) {

            ReaderThread readerThread = new ReaderThread("Reader_" + i, "" + i, queue);
            Thread thread = new Thread(readerThread);
            readerThreadList.add(readerThread);
            thread.start();
        }
    }

    public void stop() {

        for (int i = 0; i < readerParallelism; i++) {
            readerThreadList.get(i).stop();
        }
    }
}
