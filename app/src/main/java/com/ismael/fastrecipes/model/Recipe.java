package com.ismael.fastrecipes.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Ismael on 23/01/2018.
 */

public class Recipe implements Parcelable, Serializable{

    private int id;
    private int idAuthor;
    private String authorName;
    private String name;
    private String categories;
    private String ingredients;
    private String elaboration;
    private int time;
    private String difficulty;
    private int nPers;
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

    public Recipe() {}

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


    public Recipe(int id, int idAuthor, String authorName, String name, String categories, String ingredients, String elaboration, int time, String difficulty,  int nPers, String date, String image, String source,  float rating, int fav) {
        this.id = id;
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

    public Recipe(int idAuthor, String name, String categories, String ingredients, String elaboration, int time, String difficulty,  int nPers, String date, String image, String source,  float rating) {
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

    public Recipe(int id, int author,String authorName, String name, String categories, String ingredients, String elaboration, int time, String difficulty,  int nPers, String date, String image, String source) {
        this.id = id;
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


    public Recipe(String name, String categories, String ingredients, String elaboration, int time, String difficulty,  int nPers, String date, String image, String source) {
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
        id = in.readInt();
        idAuthor = in.readInt();
        authorName = in.readString();
        name = in.readString();
        categories = in.readString();
        ingredients = in.readString();
        elaboration = in.readString();
        time = in.readInt();
        difficulty = in.readString();
        nPers = in.readInt();
        date =in.readString();
        image = in.readString();
        source = in.readString();
        rating = in.readFloat();
        fav = in.readInt();
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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


    public int getnPers() {
        return nPers;
    }

    public void setnPers(int nPers) {
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
        return hashCode();
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(idAuthor);
        parcel.writeString(authorName);
        parcel.writeString(name);
        parcel.writeString(categories);
        parcel.writeString(ingredients);
        parcel.writeString(elaboration);
        parcel.writeInt(time);
        parcel.writeString(difficulty);
        parcel.writeInt(nPers);
        parcel.writeString(date);
        parcel.writeString(image);
        parcel.writeString(source);
        parcel.writeFloat(rating);
        parcel.writeInt(fav);
    }
}
