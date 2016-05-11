package com.example.faiz.mylogin;



public class ToDo_Objects {

    private String title;
    private String discription;
    private boolean check;

    public ToDo_Objects(String title, String discription, boolean check) {
        this.title = title;
        this.discription = discription;
        this.check = check;
    }

    public ToDo_Objects(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }


}
