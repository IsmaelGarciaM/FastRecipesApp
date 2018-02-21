package com.ismael.fastrecipes.interfaces;

import android.content.Context;

import com.ismael.fastrecipes.model.Recipe;
import com.ismael.fastrecipes.model.User;

import java.util.ArrayList;

/**
 * Created by Ismael on 17/02/2018.
 */

public interface ProfilePresenter {
    void getUserByComment(int idComment);
    void getUser(int idUser);
    void getUsersFav(int idRecipe);
    void getUserRecipes(int idUser);
    void validateMail(String mail);
    void validateName(String name);
    void editProfile(User userData);

    interface View{
        Context getContext();
        void setUserData(ArrayList<User> u);
        void setUserRecipesData(ArrayList<Recipe> recs);
    }
}
