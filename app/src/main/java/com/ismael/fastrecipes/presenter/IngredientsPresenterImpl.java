package com.ismael.fastrecipes.presenter;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.ismael.fastrecipes.interfaces.IngredientPresenter;
import com.ismael.fastrecipes.provider.FastRecipesContract;

import java.util.List;

import static com.ismael.fastrecipes.provider.FastRecipesProvider.INGREDIENT;

/**
 * Created by Ismael on 28/01/2018.
 */

public class IngredientsPresenterImpl implements IngredientPresenter , LoaderManager.LoaderCallbacks<Cursor>{

    List<String> ingredientsOk;
    private Context context;
    private IngredientPresenter.View view;


    public IngredientsPresenterImpl(IngredientPresenter.View vista) {
        this.view = vista;
        this.context = vista.getContext();
    }

    /**
     *
     * @param id
     * @param bundle
     * @return
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        Loader<Cursor> loader = null;
        try{
            String search = "'";
            search += bundle.getString("s") + "%'";
            switch (id){
                case INGREDIENT : loader = new CursorLoader(context, FastRecipesContract.Ingredient.CONTENT_URI, null, null, new String[]{search}, null);
                break;
            }
        }catch (Exception e){
                Log.d("EXCTIONNOCONTROL", e.getMessage());
        }

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        try{
            data.setNotificationUri(context.getContentResolver(), FastRecipesContract.Ingredient.CONTENT_URI);
            view.setIngCursorData(data);
        }catch (Exception e){
            Log.d("EXCTIONNOCONTROL", e.getMessage());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        view.setIngCursorData(null);
    }


    /**
     *
     * @param list
     */
    @Override
    public void addIngredientToList(int list) {

    }

    @Override
    public void getIngredients(String s) {
        Bundle b = new Bundle();
        b.putString("s", s);
        ((Activity) context).getLoaderManager().restartLoader(INGREDIENT, b, this);
    }


}
