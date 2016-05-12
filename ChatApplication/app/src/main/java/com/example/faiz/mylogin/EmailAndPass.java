package com.example.faiz.mylogin;

public class EmailAndPass {
    private String fname;
    private String lname;
    private String email;
    private String password;
    private String dob;
    private String gender;
    private String U_Id;
    private String imgUrl;

    //setting Up construtor for all the fields


    public EmailAndPass(String fname, String lname, String email, String password, String dob, String gender, String u_Id, String imgUrl) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
        this.dob = dob;
        this.gender = gender;
        this.U_Id = u_Id;
        this.imgUrl = imgUrl;
    }

    public EmailAndPass() {
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
}
