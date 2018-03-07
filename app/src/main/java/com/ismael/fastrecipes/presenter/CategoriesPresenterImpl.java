package com.ismael.fastrecipes.presenter;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.ismael.fastrecipes.R;
import com.ismael.fastrecipes.interfaces.CategoriesPresenter;
import com.ismael.fastrecipes.model.Category;

import java.util.ArrayList;

/**
 * Created by Ismael on 28/01/2018.
 */

public class CategoriesPresenterImpl implements CategoriesPresenter {

    private Context context;
    private CategoriesPresenter.View view;

    public CategoriesPresenterImpl(CategoriesPresenter.View vista){
        this.view = vista;
        this.context = vista.getContext();
    }

     public ArrayList<Category> getCategories(boolean state){
         ArrayList<Category> cats = new ArrayList<>();
         ///
         Category cTmp;
         String[] catNames;
         Drawable[] catImages = new Drawable[13];
         catNames = context.getResources().getStringArray(R.array.categories_array);
         catImages[0] = context.getResources().getDrawable(R.drawable.cat_drinks);
         catImages[1] = context.getResources().getDrawable(R.drawable.cat_rice);
         catImages[2] = context.getResources().getDrawable(R.drawable.cat_veget);
         catImages[3] = context.getResources().getDrawable(R.drawable.cat_desserts);
         catImages[4] = context.getResources().getDrawable(R.drawable.cat_soups);
         catImages[5] = context.getResources().getDrawable(R.drawable.cat_meat);
         catImages[6] = context.getResources().getDrawable(R.drawable.cat_nuts);
         catImages[7] = context.getResources().getDrawable(R.drawable.cat_fish);
         catImages[8] = context.getResources().getDrawable(R.drawable.cat_fruits);
         catImages[9] = context.getResources().getDrawable(R.drawable.cat_pasta);
         catImages[10] = context.getResources().getDrawable(R.drawable.cat_dairy);
         catImages[11] = context.getResources().getDrawable(R.drawable.cat_aperitifs);
         catImages[12] = context.getResources().getDrawable(R.drawable.cat_take);
         for (int i = 0; i <catImages.length ; i++){
             cTmp = new Category(catNames[i], catImages[i], state);
             cats.add(cTmp);
         }
         return cats;
    }
}
