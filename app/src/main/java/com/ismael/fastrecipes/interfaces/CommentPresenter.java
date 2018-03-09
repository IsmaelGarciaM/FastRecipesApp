package com.ismael.fastrecipes.interfaces;

import android.content.Context;

import com.ismael.fastrecipes.model.Comment;

import java.util.ArrayList;

/**
 * CommentsPresenter -> Interfaz para comentarios
 * @author Ismael Garcia
 */

public interface CommentPresenter {
    void deleteComment(int id);

    public interface View{
        Context getContext();
        void setCursorData();
        void setCommentsData(ArrayList<Comment> comments);
    }

    void showRecipeComments(int idRecipe);
    void showMyComments(int idUser);

}
