package com.ismael.fastrecipes.interfaces;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

/**
 * Created by Ismael on 28/01/2018.
 */

public interface IngredientPresenter {

    void addIngredientToList(int list);

    interface View {
        Context getContext();
        void setIngCursorData(String[] data);
    }

    void getIngredients();
}
