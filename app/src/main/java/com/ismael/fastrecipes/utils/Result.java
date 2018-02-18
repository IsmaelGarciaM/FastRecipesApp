package com.ismael.fastrecipes.utils;

import com.ismael.fastrecipes.model.Comment;
import com.ismael.fastrecipes.model.Recipe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ismael on 17/02/2018.
 */

public class Result implements Serializable {
    boolean code;
    int status;
    String message;
    List<Recipe> recipes;
    List<Comment> comments;
    int last;
    public boolean getCode() {return code;}
    public void setCode(boolean code) { this.code = code;}
    public int getStatus() {return status;}
    public void setStatus(int status) {this.status = status;}
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public List<Recipe> getRecipes() { return recipes; }
    public void setRecipes(List<Recipe> recipes) { this.recipes = recipes; }
    public int getLast() { return last; }
    public void setLast(int last) { this.last = last; }
    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }
}
