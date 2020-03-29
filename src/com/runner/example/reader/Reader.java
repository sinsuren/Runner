package com.runner.example.reader;

import com.runner.example.data.store.MessageStore;
import com.runner.example.dto.Message;
import com.runner.example.providers.ThreadPoolExecutorProvider;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Reader implements Runnable {

    private String readerId;
    private BlockingQueue<Message> messageQueue;
    private MessageStore messageStore;
    private volatile Boolean shouldRun;

    public Reader(String readerId, BlockingQueue<Message> messageQueue) {
        this.readerId = readerId;
        this.messageQueue = messageQueue;
        this.messageStore = new MessageStore();
        this.shouldRun = true;
    }

    @Override
    public void run() {
        try {
            while (true && shouldRun) {
                Thread.sleep(1);
                Integer randomSize = new Random().nextInt(2);
                //Reading at max 10 messages at one time.
                messageQueue.addAll(messageStore.getMessage(randomSize));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //Start a thread again.
            //Thread thread = new Thread(this);
            //thread.start();
        }
    }

    public void stop() {
        shouldRun = false;
    }
}
