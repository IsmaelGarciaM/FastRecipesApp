package com.ismael.fastrecipes.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Comment.class -> Clase POJO para objetos comentario
 * @author Ismael Garcia
 */

public class Comment implements Serializable, Parcelable{
    private int id;
    private int idAuthor;
    private String nameAuthor;
    private int idRecipe;
    private String content;
    private String date;
    private int del;
    private String image;

    protected Comment(Parcel in) {
        id = in.readInt();
        idAuthor = in.readInt();
        nameAuthor = in.readString();
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


    public int getIdAuthor() {
        return idAuthor;
    }

    public void setIdAuthor(int idAuthor) {
        this.idAuthor = idAuthor;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getNameAuthor() {
        return nameAuthor;
    }

    public void setNameAuthor(String nameAuthor) {
        this.nameAuthor = nameAuthor;
    }

    public Comment(){}

    public Comment(int id, int idAuthor, String nameAuthor, int idRecipe, String content, String img) {
        this.id = id;
        this.idAuthor = idAuthor;
        this.nameAuthor = nameAuthor;
        this.idRecipe = idRecipe;
        this.content = content;
        this.date = getFormatedDate();
        this.image = img;
    }


    public Comment(int idAuthor, String nameAuthor, int idRecipe, String content, String image) {
        this.idAuthor = idAuthor;
        this.nameAuthor = nameAuthor;
        this.idRecipe = idRecipe;
        this.content = content;
        this.date = getFormatedDate();
        this.image = image;
    }



    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(idAuthor);
        parcel.writeString(nameAuthor);
        parcel.writeInt(idRecipe);
        parcel.writeString(content);
        parcel.writeString(date);
        parcel.writeInt(del);
        parcel.writeString(image);
    }

    private String getFormatedDate(){

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        return formattedDate;
    }
}
