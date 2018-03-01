package com.ismael.fastrecipes.interfaces;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseUser;
import com.ismael.fastrecipes.model.User;

/**
 * Created by ismael on 3/05/17.
 */

public interface LoginPresenter {
    void validateMail(String email);
    void validatePassword(String password);
    void logIn(String email, String pass);
    void logInRest(FirebaseUser currentUser);
    void forgetPass(String email);
    void closeFirebaseSession();

    interface View{
        void showProgress(boolean show);
        void showLoginError(String msg);
        void showHome(Bundle userInfo);
        void updateUI(FirebaseUser currentUser);
    }
}
