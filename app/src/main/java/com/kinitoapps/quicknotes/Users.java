package com.kinitoapps.quicknotes;

/**
 * Created by HP on 21-09-2017.
 */

public class Users {

    public String userDesc;
    public String userName;
    public String profilePicLink;
    public String studentGrade;
    public Users() {
    }



    public Users(String name, String link, String desc, String stuGrade) {
        this.userName = name;
        this.profilePicLink = link;
        this.userDesc=desc;

        this.studentGrade=stuGrade;
    }
    public String getUserDesc() {
        return userDesc;}

    public String getUserName() {
        return userName;
    }


    public String getProfilePicLink() {
        return profilePicLink;
    }
    public String getStudentGrade() {
        return studentGrade;
    }
}
