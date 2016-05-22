package com.example.faiz.mylogin.model;

/**
 * Created by Moosa moosa.bh@gmail.com on 5/23/2016 23 May.
 * Everything is possible in programming.
 */
public class TempRefObj {
    private String userId;
    private String conversationId;

    public TempRefObj() {
    }

    public TempRefObj(String userId, String conversationId) {
        this.userId = userId;
        this.conversationId = conversationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
