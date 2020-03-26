package com.runner.example.dto;

public class Message {

    private String message;
    private Long id;
    private String groupId;
    private String messageId;

    public Message(String message, Long id, String messageId, String groupId) {
        this.message = message;
        this.id = id;
        this.groupId = groupId;
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public Long getId() {
        return id;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getMessageId() {
        return messageId;
    }

    @Override
    public String toString() {
        return id + ": " + " : " + groupId + " : " + messageId + " : " + message;
    }
}