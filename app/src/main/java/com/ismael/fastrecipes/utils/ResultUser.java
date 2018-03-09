package com.ismael.fastrecipes.utils;

import com.ismael.fastrecipes.model.User;

import java.util.List;

/**
 * ResultUser -> Clase POJO para objetos devueltos por la API que contengan usuarios
 * @author Ismael Garcia
 */

public class ResultUser {
    boolean code;
    int status;
    String message;
    List<User> user;
    int last;
    public boolean getCode() {return code;}
    public void setCode(boolean code) { this.code = code;}
    public int getStatus() {return status;}
    public void setStatus(int status) {this.status = status;}
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public List<User> getUsers() { return user; }
    public void setUsers(List<User> user) { this.user = user; }
    public int getLast() { return last; }
    public void setLast(int last) { this.last = last; }
}
