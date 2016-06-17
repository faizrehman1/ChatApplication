package com.example.faiz.mylogin.model;

public class Group_Detail {

    private String groupName;
    private String adminName;
    private String imgUrl;

    public Group_Detail(String groupName, String adminName,String imgUrl) {
        this.groupName = groupName;
        this.adminName = adminName;
        this.imgUrl = imgUrl;
    }

    public Group_Detail() {
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


}
