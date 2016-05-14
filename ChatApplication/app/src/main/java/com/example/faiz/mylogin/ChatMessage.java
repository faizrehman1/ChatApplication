package com.example.faiz.mylogin;

/**
 * Created by Kamran ALi on 5/15/2016.
 */
public class ChatMessage {
    private long id;
    private boolean isMe;
    private String message;
    private Long userId;
    private String dateTime;

    public ChatMessage() {
    }

    public ChatMessage(long id, boolean isMe, String message, Long userId, String dateTime) {
        this.id = id;
        this.isMe = isMe;
        this.message = message;
        this.userId = userId;
        this.dateTime = dateTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean me) {
        isMe = me;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
