package com.ismael.fastrecipes.interfaces;

import android.content.Context;
import android.net.Uri;

import com.ismael.fastrecipes.model.Recipe;
import com.ismael.fastrecipes.model.User;

import java.util.ArrayList;

/**
 * ProfilePresenter -> Interfaz para los usuarios
 * @author Ismael Garcia
 */

public interface ProfilePresenter {
    void getUser(int idUser);
    void getUsersFav(int idRecipe);
    void getUserRecipes(int idUser);
    void validateMail(String mail);
    void validateName(String name);
    void editProfile(User userData, boolean imageChanged, Uri mImageUri);

    interface View{
        Context getContext();
        void setUserData(User u);
        void setUserRecipesData(ArrayList<Recipe> recs);
        void showProgress(boolean show);
        void showNetworkError(String msg);
        void updateCurrentUser(User u);

        void setUserListData(ArrayList<User> u);

        void cancelSearch();
    }
}
