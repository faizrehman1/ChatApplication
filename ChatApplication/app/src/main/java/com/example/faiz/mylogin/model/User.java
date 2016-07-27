package com.example.faiz.mylogin.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    private String fname;
    private String lname;
    private String email;
    private String password;
    private String dob;
    private String gender;
    private String U_Id;
    private String imgUrl;
    private String status;

    public User(String fname, String lname, String email, String password, String dob, String gender, String u_Id, String imgUrl, String  status) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
        this.dob = dob;
        this.gender = gender;
        U_Id = u_Id;
        this.imgUrl = imgUrl;
        this.status = status;
    }

    public User() {
    }

    protected User(Parcel in) {
        this.fname = in.readString();
        this.lname = in.readString();
        this.email = in.readString();
        this.password = in.readString();
        this.dob = in.readString();
        this.gender = in.readString();
        this.U_Id = in.readString();
        this.imgUrl = in.readString();
        this.status=in.readString();
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getU_Id() {
        return U_Id;
    }

    public void setU_Id(String u_Id) {
        U_Id = u_Id;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fname);
        dest.writeString(this.lname);
        dest.writeString(this.email);
        dest.writeString(this.password);
        dest.writeString(this.dob);
        dest.writeString(this.gender);
        dest.writeString(this.U_Id);
        dest.writeString(this.imgUrl);
    }
}
