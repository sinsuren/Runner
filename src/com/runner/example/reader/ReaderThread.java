package com.runner.example.reader;

import com.runner.example.data.store.MessageStore;
import com.runner.example.dto.Message;
import sun.jvm.hotspot.debugger.remote.amd64.RemoteAMD64Thread;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

public class ReaderThread implements Runnable {

    private String name;
    private String id;
    private LinkedBlockingQueue<Message> messageQueue;
    private MessageStore messageStore;
    private volatile Boolean shouldRun;

    public ReaderThread(String name, String id, LinkedBlockingQueue<Message> messageQueue) {
        this.name = name;
        this.id = id;
        this.messageQueue = messageQueue;
        this.messageStore = new MessageStore();
        this.shouldRun = true;
    }

    @Override
    public void run() {

        while (true && shouldRun) {
            //Reading at max 10 messages at one time.
            messageQueue.addAll(messageStore.getMessage(new Random().nextInt(10)));
        }
    }

    public void stop() {
        shouldRun = false;
    }
}
