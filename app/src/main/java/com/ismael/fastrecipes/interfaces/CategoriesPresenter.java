package com.ismael.fastrecipes.interfaces;

import android.content.Context;

import com.ismael.fastrecipes.model.Category;

import java.util.ArrayList;

/**
 * Created by Ismael on 28/01/2018.
 */

public interface CategoriesPresenter {
    interface View{
        Context getContext();
    }

    ArrayList<Category> getCategories(boolean state);
}
