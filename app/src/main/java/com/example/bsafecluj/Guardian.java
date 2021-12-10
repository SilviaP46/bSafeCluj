package com.example.bsafecluj;

import java.util.List;

public class Guardian {

    private int idGuardian;
    private String username;
    private Long phoneNumber;
    private List<Guardian> guardianList;

    public Guardian(int idGuardian, String username, Long phoneNumber) {
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

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
