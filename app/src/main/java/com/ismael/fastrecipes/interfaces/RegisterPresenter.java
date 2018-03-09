package com.ismael.fastrecipes.interfaces;

/**
 * RegisterPresenter -> Interfaz para el registro
 * @author Ismael Garcia
 */

public interface RegisterPresenter {

    void validateMail(String email);
    void validatePassword(String password);
    void validateData(String name);
    void registerUser(String name, String mail, String password);
    void registerUserRest(String token, String email, String name, String regDate, String uid);


    interface View{
        void showProgress(boolean show);
        void showInputError(String msg);
        void showLogin();
    }
}
