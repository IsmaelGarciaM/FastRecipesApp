package com.ismael.fastrecipes.interfaces;

/**
 * Created by ismael on 30/05/17.
 */

public interface RegisterPresenter {

    void validateMail(String email);
    void validatePassword(String password);
    void validateData(String name);
    void registerUser(String name, String mail, String password);
    void registerUserRest(String token, String email, String name, String regDate, String uid);


    interface View{
        void showInputError();
        void showLogin();
    }
}
