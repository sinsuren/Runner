package com.runner.example.data.store;

import com.runner.example.dto.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class MessageStore {
    //Assuming message store has infinite number of message which can produce messages.
    private static AtomicLong messageId = new AtomicLong(1);


    public List<Message> getMessage(int size) {

        List<Message> messages = new ArrayList<>();

        IntStream.of(size).forEach(i -> {
            Long value = messageId.getAndIncrement();
            messages.add(new Message("Message " + value, value, UUID.randomUUID().toString(), new Random().nextInt(10) + ""));
        });


        return messages;
    }
}
