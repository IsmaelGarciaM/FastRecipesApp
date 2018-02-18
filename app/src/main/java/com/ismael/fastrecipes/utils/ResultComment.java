package com.ismael.fastrecipes.utils;

import com.ismael.fastrecipes.model.Comment;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ismael on 17/02/2018.
 */

public class ResultComment implements Serializable {
    boolean code;
    int status;
    String message;
    List<Comment> comments;
    int last;
    public boolean getCode() {return code;}
    public void setCode(boolean code) { this.code = code;}
    public int getStatus() {return status;}
    public void setStatus(int status) {this.status = status;}
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }
    public int getLast() { return last; }
    public void setLast(int last) { this.last = last; }
}
