package com.example.craterradar.UserSide.ModelClass;

public class User {
    public String Fullname,Email,Phoneno,Password,ProfileImagePath;

    public User() {
    }

    public User(String fullname, String email, String phoneno, String password,String profileImagePath) {
        Fullname = fullname;
        Email = email;
        Phoneno = phoneno;
        Password = password;
        ProfileImagePath = profileImagePath;

    }
}
