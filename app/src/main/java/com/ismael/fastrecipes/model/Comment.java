package com.ismael.fastrecipes.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by ismael on 17/05/17.
 */

public class Comment implements Serializable, Parcelable{
    private int id;
    private String idAuthor;
    private int idRecipe;
    private String content;
    private String date;
    private int del;
    private String image;

    protected Comment(Parcel in) {
        id = in.readInt();
        idAuthor = in.readString();
        idRecipe = in.readInt();
        content = in.readString();
        date = in.readString();
        del = in.readInt();
        image = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public int getDel() {
        return del;
    }

    public void setDel(int del) {
        this.del = del;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdAuthor() {
        return idAuthor;
    }

    public void setIdAuthor(String idUser) {
        this.idAuthor = idUser;
    }

    public int getIdRecipe() {
        return idRecipe;
    }

    public void setIdRecipe(int idRecipe) {
        this.idRecipe = idRecipe;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String comment) {
        this.content = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserImg() {
        return image;
    }

    public void setUserImg(String userImg) {
        this.image = userImg;
    }



    public Comment(int id, String idUser, int idRecipe, String content, String date, int del, String userImg) {
        this.id = id;
        this.idAuthor = idUser;
        this.idRecipe = idRecipe;
        this.content = content;
        this.date = date;
        this.image = userImg;
        this.del = del;
    }

    public Comment(String idUser, int idRecipe, String content, String date, int del, String userImg) {
        this.id = id;
        this.idAuthor = idUser;
        this.idRecipe = idRecipe;
        this.content = content;
        this.date = date;
        this.image = userImg;
        this.del = del;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(idAuthor);
        parcel.writeInt(idRecipe);
        parcel.writeString(content);
        parcel.writeString(date);
        parcel.writeInt(del);
        parcel.writeString(image);




    }
}
