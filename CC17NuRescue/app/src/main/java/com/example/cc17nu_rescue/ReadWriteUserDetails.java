package com.example.cc17nu_rescue;

public class ReadWriteUserDetails {
    public String name, dob, gender, number;

    public ReadWriteUserDetails() {
    }

    public ReadWriteUserDetails(String textBday, String textGender, String textNumber, String textFullame) {
        this.name = textFullame;
        this.dob = textBday;
        this.gender = textGender;
        this.number = textNumber;
    }
}
