package com.runner.example;

import com.runner.example.distributor.Distributor;
import com.runner.example.dto.Message;
import com.runner.example.reader.ReaderManager;

import java.util.concurrent.LinkedBlockingQueue;

public class Runner {

    public static void main(String[] args) throws InterruptedException {

        LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<>(100);

        ReaderManager readerManager = new ReaderManager(queue, 10);
        Distributor distributor = new Distributor(queue, 10);

        readerManager.start();
        distributor.start();


        //readerManager.stop();

        //System.out.println(queue.size());

    }
}
