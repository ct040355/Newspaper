package com.example.newspaper.Models;

public class Users {
    //SignUp Constructor
    public Users(String userName, String mail, String password, String profilepic) {
        this.userName = userName;
        this.mail = mail;
        this.password = password;
        this.profilepic = profilepic;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    String profilepic, userName, mail, password, userId, lastMessgae;

    public Users(String profilepic, String userName, String mail, String password, String userId) {
        this.profilepic = profilepic;
        this.userName = userName;
        this.mail = mail;
        this.password = password;
        this.userId = userId;
    }
    public Users(){}


}
