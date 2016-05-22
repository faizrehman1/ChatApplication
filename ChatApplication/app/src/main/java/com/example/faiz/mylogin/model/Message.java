package com.example.faiz.mylogin.model;



public class Message {

    private String msg;
    private String U_id;
    private String time;

    public Message(String msg, String u_id, String time) {
        this.msg = msg;
        U_id = u_id;
        this.time = time;
    }

    public Message(){

    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getU_id() {
        return U_id;
    }

    public void setU_id(String u_id) {
        U_id = u_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
