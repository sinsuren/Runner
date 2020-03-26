package com.runner.example.consumer;

import com.runner.example.dto.Message;

import java.util.concurrent.LinkedBlockingQueue;

public class ConsumerThread implements Runnable {

    private Integer threadId;
    private LinkedBlockingQueue<Message> messages = new LinkedBlockingQueue<>();

    public ConsumerThread(Integer threadId) {
        this.threadId = threadId;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message message = messages.take();
                System.out.println(threadId + " -> " + message.toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void push(Message message) {
        try {
            messages.put(message);
        } catch (Exception e) {

        }

    }
}
