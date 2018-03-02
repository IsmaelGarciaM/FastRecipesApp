package com.ismael.fastrecipes.interfaces;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.ismael.fastrecipes.model.Comment;
import com.ismael.fastrecipes.model.Recipe;

import java.util.ArrayList;

/**
 * Created by Ismael on 24/01/2018.
 */

public interface RecipesPresenter {
    void getFavRecipes(int idUser);
    void deleteRecipe(int idRecipe);
    void setFavourite(int id, int id1, int fav);
    void getFilteredRecipes(Recipe rModel);
    void sendComment(String comment, int id, int id1, String nameAut);
    void setRating(int id, int id1, int newRating);
    void validateField(String field);
    void validateName(String name);
    void validateEla(String elaboration);
    void loadImage(Recipe tmp, Uri mImageUri);

    interface View{
        Context getContext();
        void showRecipeInfo(Bundle recipe);
        void setFavState(Recipe recipe);
        void setListData(ArrayList<Recipe> recs);
        void cancelSearch();
        void addNewComment(ArrayList<Comment> newComment);
    }

    void getMyRecipesList(int idUser);
    void addRecipe(Recipe addR, Uri mImageUri);
    void modifyRecipe(Recipe addR, Uri mImageUri);
    void getRecipe(int id, int idu);
}
