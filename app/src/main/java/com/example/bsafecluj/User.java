package com.example.bsafecluj;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class User implements Parcelable {

    private int idUser;
    private String username;
    private String phoneNumber;
    private Integer birthYear;
    private String loggedStatus;
    private List<Guardian> guardianList;


    User(){}



    public User(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public User(int idUser, String username, String phoneNumber, Integer birthYear, String loggedStatus) {
        this.idUser = idUser;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.birthYear = birthYear;
        this.loggedStatus = loggedStatus;
    }

    protected User(Parcel in) {
        idUser = in.readInt();
        username = in.readString();
        phoneNumber = in.readString();
        if (in.readByte() == 0) {
            birthYear = null;
        } else {
            birthYear = in.readInt();
        }
        loggedStatus = in.readString();
    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idUser);
        dest.writeString(username);
        dest.writeString(phoneNumber);
        if (birthYear == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(birthYear);
        }
        dest.writeString(loggedStatus);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public String getLoggedStatus() {
        return loggedStatus;
    }

    public void setLoggedStatus(String loggedStatus) {
        this.loggedStatus = loggedStatus;
    }

    public List<Guardian> getGuardianList() {
        return guardianList;
    }

    public void setGuardianList(List<Guardian> guardianList) {
        this.guardianList = guardianList;
    }
}
