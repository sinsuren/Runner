package com.runner.example.task;

import com.runner.example.dto.Message;

public class ProcessorTask implements Runnable {

    Message message;

    public ProcessorTask(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getThreadGroup() +  + message.toString());
    }
}
