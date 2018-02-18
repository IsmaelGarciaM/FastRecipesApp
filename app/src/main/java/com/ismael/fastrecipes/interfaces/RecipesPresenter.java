package com.ismael.fastrecipes.interfaces;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import com.ismael.fastrecipes.model.Recipe;

/**
 * Created by Ismael on 24/01/2018.
 */

public interface RecipesPresenter {
    void getFavRecipes();
    void getMyRecipe(int idRecipe);
    void getFavRecipe(int idRecipe);
    void deleteRecipe(int idRecipe);

    void setFavourite(int id, int id1);

    interface View{
        Context getContext();
        void setCursorData(Cursor data);
        void showRecipeInfo(Bundle recipe);

        void setFavState();
    }

    void getMyRecipesList();
    void addRecipe();
    void getRecipe(int id);
}
