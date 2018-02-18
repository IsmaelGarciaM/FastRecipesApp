package com.ismael.fastrecipes.interfaces;

import android.content.Context;

import com.ismael.fastrecipes.model.Comment;

import java.util.ArrayList;

/**
 * Created by Ismael on 23/01/2018.
 */

public interface CommentPresenter {
    public interface View{
        Context getContext();
        void setCursorData();
        void setCommentsData(ArrayList<Comment> comments);
    }

    void showRecipeComments(int idRecipe);
    void showMyComments(int idUser);

}
