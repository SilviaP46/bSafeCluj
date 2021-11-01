package com.example.bsafecluj;

public class User {

    private int idUser;
    private String username;
    private Long phoneNumber;


    public User() {
    }

    public User(int idUser, Long phoneNumber) {
        this.idUser = idUser;
        this.phoneNumber = phoneNumber;
    }


    public User(int idUser, String username, Long phoneNumber) {
        this.idUser = idUser;
        this.username = username;
        this.phoneNumber = phoneNumber;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", username='" + username + '\'' +
                ", phoneNumber=" + phoneNumber +
                '}';
    }
}
