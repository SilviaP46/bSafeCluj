package com.example.bsafecluj;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private int idUser;
    private String username;
    private Long phoneNumber;
    private Integer birthYear;


    public User() {
    }


    public User(int idUser, Long phoneNumber) {
        this.idUser = idUser;
        this.phoneNumber = phoneNumber;
    }


    public User(int idUser, String username, Long phoneNumber, Integer birthYear) {
        this.idUser = idUser;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.birthYear=birthYear;
    }

    protected User(Parcel in) {
        idUser = in.readInt();
        username = in.readString();
        if (in.readByte() == 0) {
            phoneNumber = null;
        } else {
            phoneNumber = in.readLong();
        }
        if (in.readByte() == 0) {
            birthYear = null;
        } else {
            birthYear = in.readInt();
        }
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

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", username='" + username + '\'' +
                ", phoneNumber=" + phoneNumber +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idUser);
        dest.writeString(username);
        if (phoneNumber == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(phoneNumber);
        }

        if (birthYear == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(birthYear);
        }

    }
}
