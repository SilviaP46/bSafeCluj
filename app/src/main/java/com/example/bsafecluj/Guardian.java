package com.example.bsafecluj;


public class Guardian {

    private int idGuardian;
    private String username;
    private String phoneNumber;


    public Guardian(int idGuardian, String username, String phoneNumber) {
        this.idGuardian = idGuardian;
        this.username = username;
        this.phoneNumber = phoneNumber;
    }

    public int getIdGuardian() {
        return idGuardian;
    }

    public void setIdGuardian(int idGuardian) {
        this.idGuardian = idGuardian;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
