package com.ismael.fastrecipes.interfaces;

import android.content.Context;

import com.ismael.fastrecipes.model.User;

import java.util.ArrayList;

/**
 * Created by Ismael on 17/02/2018.
 */

public interface ProfilePresenter {
    void getUser(int idComment);

    void getUsersFav(int idRecipe);

    interface View{
        Context getContext();
        void setUserData(ArrayList<User> u);
    }
}
