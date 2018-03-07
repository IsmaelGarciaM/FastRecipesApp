package com.ismael.fastrecipes.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by ismael on 17/05/17.
 */

public class User implements Parcelable, Serializable{


    private int id;
    private String email;
    private String name;
    private String surname;
    private String location;
    private String regdate;
    private int nrecipes;
    private String image;
    private String uid;

    public User(){}

    protected User(Parcel in) {
        id = in.readInt();
        email = in.readString();
        name = in.readString();
        surname = in.readString();
        location = in.readString();
        regdate = in.readString();
        nrecipes = in.readInt();
        image = in.readString();
        uid = in.readString();
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


    //Get y set para todos los campos
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String userAddress) {
        this.location = userAddress;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public int getNrecipes() {
        return nrecipes;
    }

    public void setNrecipes(int nrecipes) {
        this.nrecipes = nrecipes;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }



    //Constructores

    public User(int id, String userEmail, String userName, String userSurname, String userLocation, String userRegDate, int nRecipes, String image, String uid) {
        this.id = id;
        this.name = userName;
        this.surname = userSurname;
        this.email = userEmail;
        this.location = userLocation;
        this.regdate = userRegDate;
        this.nrecipes = nRecipes;
        this.image = image;
        this.uid = uid;
    }

    public User(String userEmail, String userName, String userRegDate, String uid) {
        this.name = userName;
        this.regdate = userRegDate;
        this.email = userEmail;
        this.uid = uid;
    }
    public User(int id, String userName, String location, String image) {
        this.id = id;
        this.name = userName;
        this.location = location;
        this.image = image;
    }




    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(email);
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeString(location);
        dest.writeString(regdate);
        dest.writeInt(nrecipes);
        dest.writeString(image);
        dest.writeString(uid);
    }
}
