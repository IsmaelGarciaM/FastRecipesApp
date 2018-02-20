package com.ismael.fastrecipes.interfaces;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import com.ismael.fastrecipes.model.Recipe;

import java.util.ArrayList;

/**
 * Created by Ismael on 24/01/2018.
 */

public interface RecipesPresenter {
    void getFavRecipes();
    void getMyRecipe(int idRecipe);
    void getFavRecipe(int idRecipe);
    void deleteRecipe(int idRecipe);
    void setFavourite(int id, int id1, int fav);
    void getFilteredRecipes(Recipe rModel);
    void sendComment(String comment, int id, int id1);

    interface View{
        Context getContext();
        void setCursorData(Cursor data);
        void showRecipeInfo(Bundle recipe);
        void setFavState(Recipe recipe);
        void setListData(ArrayList<Recipe> recs);
    }

    void getMyRecipesList();
    void addRecipe(Recipe addR);
    void addMyRecipe(Recipe addR);
    void addFavRecipe(Recipe addR);
    void deleteMyRecipe(int idRecipe);
    void deleteFavRecipe(int idRecipe);
    void modifyMyRecipe(Recipe addR);
    void modifyRecipe(Recipe addR);
    void getRecipe(int id);
}
