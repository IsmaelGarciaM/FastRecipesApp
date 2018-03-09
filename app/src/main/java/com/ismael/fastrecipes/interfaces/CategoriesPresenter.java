package com.ismael.fastrecipes.interfaces;

import android.content.Context;

import com.ismael.fastrecipes.model.Category;

import java.util.ArrayList;

/**
 * CategoriesPresenter -> Interfaz para categorías
 * @author Ismael Garcia
 */

public interface CategoriesPresenter {
    interface View{
        Context getContext();
    }

    ArrayList<Category> getCategories(boolean state);
}
