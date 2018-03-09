package com.ismael.fastrecipes.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Recipe -> Clase POJO para recetas
 * @author Ismael Garcia
 */

public class Recipe implements Parcelable, Serializable{

    private int idr;

    private int idAuthor;

    private String authorName;

    private String name;

    private String categories;

    private String ingredients;

    private String elaboration;

    private int time;

    private String difficulty;

    private String nPers;

    private String date;

    private String image;

    private String source;

    private float rating;

    public int getFav() {
        return fav;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }

    private int fav;

    public Recipe() {
        this.date = getFormatedDate();
    }

    public int getAuthor() {
        return idAuthor;
    }

    public void setAuthor(int author) {
        this.idAuthor = author;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


    public Recipe(int idr, int idAuthor, String authorName, String name, String categories, String ingredients, String elaboration, int time, String difficulty, String nPers, String date, String image, String source, float rating, int fav) {
        this.idr = idr;
        this.idAuthor = idAuthor;
        this.authorName = authorName;
        this.name = name;
        this.categories = categories;
        this.ingredients = ingredients;
        this.elaboration = elaboration;
        this.time = time;
        this.difficulty = difficulty;
        this.nPers = nPers;
        this.date = date;
        this.image = image;
        this.source = source;
        this.rating = rating;
        this.fav = fav;
    }

    public Recipe(int idr, int idAuthor, String authorName, String name, String categories, String ingredients, String elaboration, int time, String difficulty, String nPers, String date, String image, String source, float rating) {
        this.idr = idr;
        this.idAuthor = idAuthor;
        this.authorName = authorName;
        this.name = name;
        this.categories = categories;
        this.ingredients = ingredients;
        this.elaboration = elaboration;
        this.time = time;
        this.difficulty = difficulty;
        this.nPers = nPers;
        this.date = date;
        this.image = image;
        this.source = source;
        this.rating = rating;
    }


    public Recipe(int idAuthor, String name, String categories, String ingredients, String elaboration, int time, String difficulty,  String nPers, String date, String image, String source,  float rating) {
        this.idAuthor = idAuthor;
        this.name = name;
        this.categories = categories;
        this.ingredients = ingredients;
        this.elaboration = elaboration;
        this.time = time;
        this.difficulty = difficulty;
        this.nPers = nPers;
        this.date = date;
        this.image = image;
        this.source = source;
        this.rating = rating;
    }

    public Recipe(int idr, int author, String authorName, String name, String categories, String ingredients, String elaboration, int time, String difficulty, String nPers, String date, String image, String source) {
        this.idr = idr;
        this.authorName = authorName;
        this.idAuthor = author;
        this.name = name;
        this.categories = categories;
        this.ingredients = ingredients;
        this.elaboration = elaboration;
        this.time = time;
        this.difficulty = difficulty;
        this.nPers = nPers;
        this.date = date;
        this.image = image;
        this.source = source;
    }


    public Recipe(String name, String categories, String ingredients, String elaboration, int time, String difficulty,  String nPers, String date, String image, String source) {
        this.name = name;
        this.categories = categories;
        this.ingredients = ingredients;
        this.elaboration = elaboration;
        this.time = time;
        this.difficulty = difficulty;
        this.nPers = nPers;
        this.date = date;
        this.image = image;
        this.source = source;
    }


    protected Recipe(Parcel in) {
        idr = in.readInt();
        idAuthor = in.readInt();
        authorName = in.readString();
        name = in.readString();
        categories = in.readString();
        ingredients = in.readString();
        elaboration = in.readString();
        time = in.readInt();
        difficulty = in.readString();
        nPers = in.readString();
        date =in.readString();
        image = in.readString();
        source = in.readString();
        rating = in.readFloat();
        fav = in.readInt();
    }
    @SuppressWarnings({"unchecked"})
    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public int getIdr() {
        return idr;
    }

    public void setIdr(int idr) {
        this.idr = idr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getnPers() {
        return nPers;
    }

    public void setnPers(String nPers) {
        this.nPers = nPers;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getElaboration() {
        return elaboration;
    }

    public void setElaboration(String elaboration) {
        this.elaboration = elaboration;
    }
    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(idr);
        parcel.writeInt(idAuthor);
        parcel.writeString(authorName);
        parcel.writeString(name);
        parcel.writeString(categories);
        parcel.writeString(ingredients);
        parcel.writeString(elaboration);
        parcel.writeInt(time);
        parcel.writeString(difficulty);
        parcel.writeString(nPers);
        parcel.writeString(date);
        parcel.writeString(image);
        parcel.writeString(source);
        parcel.writeFloat(rating);
        parcel.writeInt(fav);
    }

    private String getFormatedDate(){

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        return formattedDate;
    }
}
