package com.example.craterradar.AdminSide.ModelClass;

public class UserList {
    String uid,userName,userProfilePicPath,email,phone,pass;

    public UserList(String uid, String userName, String userProfilePicPath,String email,String phone,String pass) {
        this.uid = uid;
        this.userName = userName;
        this.userProfilePicPath = userProfilePicPath;
        this.email = email;
        this.phone = phone;
        this.pass = pass;
    }



    public String getUid() {
        return uid;
    }

    public String getUserName() {
        return userName;
    }


    public String getUserProfilePicPath() {
        return userProfilePicPath;
    }


    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPass() {
        return pass;
    }
}
