package com.ismael.fastrecipes.presenter;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.ismael.fastrecipes.FastRecipesApplication;
import com.ismael.fastrecipes.db.DatabaseContract;
import com.ismael.fastrecipes.interfaces.RecipesPresenter;
import com.ismael.fastrecipes.model.Comment;
import com.ismael.fastrecipes.model.Recipe;
import com.ismael.fastrecipes.provider.FastRecipesContract;
import com.ismael.fastrecipes.utils.FastRecipesService;
import com.ismael.fastrecipes.utils.Result;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ismael.fastrecipes.provider.FastRecipesProvider.FAVRECIPES;
import static com.ismael.fastrecipes.provider.FastRecipesProvider.FAVRECIPE_ID;
import static com.ismael.fastrecipes.provider.FastRecipesProvider.RECIPE;
import static com.ismael.fastrecipes.provider.FastRecipesProvider.RECIPE_ID;

/**
 * RecipesPresenterImpl.java - Clase que controla todas las conexiones para la obtención, modificación, borrado y añadido de recetas. Local o externo
 * Created by Ismael on 24/01/2018.
 */

public class RecipesPresenterImpl implements RecipesPresenter, LoaderManager.LoaderCallbacks<Cursor>{
    private Context context;
    private RecipesPresenter.View view;
    private FastRecipesService mService;
    Result r = new Result();

    public RecipesPresenterImpl(RecipesPresenter.View vista){
        this.view = vista;
        this.context = vista.getContext();
        mService = FastRecipesApplication.getFastRecipesService();
    }


    /**
     * Crea un Loader para realizar una consulta a la base de datos sqlite
     * @param id Identificador para el loader
     * @param data Datos para asociar a la búsqueda
     * @return Devuelve el loader con una lista de objetos según el id
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle data) {
        Loader<Cursor> loader = null;
        switch (id){
            case RECIPE:
                loader = new CursorLoader(context, FastRecipesContract.Recipe.CONTENT_URI, null, null, null, null);
                break;

            case FAVRECIPES: loader = new CursorLoader(context, FastRecipesContract.FavRecipe.CONTENT_URI, null, null, null, null);
                break;

            case FAVRECIPE_ID:
                loader = new CursorLoader(context, Uri.withAppendedPath(FastRecipesContract.FavRecipe.CONTENT_URI,"/" + String.valueOf(data.getInt("id"))), null, null, null, null);
                break;

            case RECIPE_ID:
                loader = new CursorLoader(context, Uri.withAppendedPath(FastRecipesContract.Recipe.CONTENT_URI,"/" + String.valueOf(data.getInt("id"))), null, null, null, null);
                break;

        }

        return loader;
    }

    /**
     * Carga los datos del loader en la interfaz gráfica
     * @param loader Loader con un cursor obtenido de la consulta
     * @param data Cursor con los datos obtenidos en la consulta
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Bundle b = new Bundle();

        switch (loader.getId()){
            case RECIPE:
                data.setNotificationUri(context.getContentResolver(), FastRecipesContract.Recipe.CONTENT_URI);
                view.setCursorData(data);
                break;
            case RECIPE_ID:
                data.setNotificationUri(context.getContentResolver(), FastRecipesContract.Recipe.CONTENT_URI);
                b.putParcelable("recipe", consRecipe(data));
                view.showRecipeInfo(b);
            case FAVRECIPES:
                data.setNotificationUri(context.getContentResolver(), FastRecipesContract.FavRecipe.CONTENT_URI);
                view.setCursorData(data);
                break;
            case FAVRECIPE_ID:
                data.setNotificationUri(context.getContentResolver(), FastRecipesContract.FavRecipe.CONTENT_URI);
                b.putParcelable("recipe", consRecipe(data));
                view.showRecipeInfo(b);
                break;
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        view.setCursorData(null);
    }




    /*
        Métodos para la gestión de 'MyRecipes'
     */

    /**
     * Obtiene el listado de recetas propias desde sqlite
     */
    @Override
    public void getMyRecipesList() {
        ((Activity)context).getLoaderManager().restartLoader(RECIPE, null, this);
    }


    @Override
    public void getMyRecipe(int idRecipe) {
        Bundle b = new Bundle();
        b.putInt("id", idRecipe);
        ((Activity)context).getLoaderManager().restartLoader(RECIPE_ID, b, this);
    }


    @Override
    public void addMyRecipe(Recipe addR) {
        ContentValues cv = recipeToValues(addR);
        context.getContentResolver().insert(FastRecipesContract.Recipe.CONTENT_URI, cv);
    }

    @Override
    public void deleteMyRecipe(int idRecipe) {
        context.getContentResolver().delete(Uri.withAppendedPath(FastRecipesContract.Recipe.CONTENT_URI, String.valueOf(idRecipe)), null, null);
    }


    @Override
    public void modifyMyRecipe(Recipe addR) {
        ContentValues cv = recipeToValues(addR);
        cv.put(DatabaseContract.RecipeEntry._ID, addR.getId());
        context.getContentResolver().update(FastRecipesContract.Recipe.CONTENT_URI, cv, FastRecipesContract.Recipe._ID+"="+String.valueOf(addR.getId()), null);

    }

     /*
        Métodos para la gestión de 'FavRecipes'
     */

    /**
     * Obtiene el listado de recetas propias desde sqlite
     */


    @Override
    public void getFavRecipes(){
        ((Activity)context).getLoaderManager().restartLoader(FAVRECIPES, null, this);
    }

    @Override
    public void addFavRecipe(Recipe addR) {
        ContentValues cv = recipeToValues(addR);
        context.getContentResolver().insert(FastRecipesContract.FavRecipe.CONTENT_URI, cv);
    }



    @Override
    public void deleteFavRecipe(int idRecipe) {
        context.getContentResolver().delete(Uri.withAppendedPath(FastRecipesContract.FavRecipe.CONTENT_URI, String.valueOf(idRecipe)), null, null);
    }


    @Override
    public void getFavRecipe(int idRecipe) {
        Bundle b = new Bundle();
        b.putInt("id", idRecipe);
        ((Activity)context).getLoaderManager().restartLoader(FAVRECIPE_ID, b, this);

    }


    /*
    Métodos para la gestion de recetas filtradas
     */


    @Override
    public void addRecipe(final Recipe addR) {
        final Recipe[] r = new Recipe[1];
        mService.addRecipe(addR).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {
                        addMyRecipe(addR);
                        Log.d("SUBSCRIBER ADD INFO", "RECIPE ADDED");
                        //view.showSocialFragment();

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("SUBSCRIBER FAILED", e.getMessage());
                    }

                    @Override
                    public void onNext(Result result) {
                        r[0] = result.getRecipes().get(0);
                    }

                });
    }


    @Override
    public void modifyRecipe(final Recipe addR) {
        final Recipe[] r = new Recipe[1];
        mService.modifyRecipe(addR).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {
                        modifyMyRecipe(addR);
                        Log.d("SUBSCRIBER ADD INFO", "RECIPE ADDED");
                        //view.showSocialFragment();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("SUBSCRIBER FAILED", e.getMessage());
                    }

                    @Override
                    public void onNext(Result result) {
                        r[0] = result.getRecipes().get(0);
                    }

                });
    }




    @Override
    public void getRecipe(int id) {

        final Recipe[] r = new Recipe[1];
        final ArrayList<Comment> c = new ArrayList<>();
        //Observable<Recipe> call = mService.getRecipe(id);
        mService.getRecipe(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {
                        Bundle b = new Bundle();
                        b.putParcelable("recipe", r[0]);
                        b.putParcelableArrayList("comments", c);
                        view.showRecipeInfo(b);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("SUBSCRIBER FAILED", e.getMessage());
                    }

                    @Override
                    public void onNext(Result result) {
                        r[0] = result.getRecipes().get(0);
                        List<Comment> c;
                        if(result.getComments().size() > 0) {
                            c = new ArrayList<>();
                            c.addAll(result.getComments());
                        }
                    }

                });
    }





    @Override
    public void setFavourite(int idUser, int idRecipe, int fav) {
        final Recipe[] r = new Recipe[1];
        final ArrayList<Comment> c = new ArrayList<>();
        //Observable<Recipe> call = mService.getRecipe(id);
        mService.setFavouriteRec(idUser, idRecipe, fav).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {
                        if(r[0].getFav() == 0){
                            //La ha desmarcado. Borrar de FavRecipes
                        }
                        else if(r[0].getFav() == 1){
                            //La ha marcado. Añadir a fav recipes

                        }
                       view.setFavState(r[0]);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("SUBSCRIBER FAILED", e.getMessage());
                    }

                    @Override
                    public void onNext(Result result) {
                        r[0] = result.getRecipes().get(0);
                    }

                });
    }

    @Override
    public void getFilteredRecipes(Recipe rModel) {
        final ArrayList<Recipe> recs = new ArrayList<>();
        //Observable<Recipe> call = mService.getRecipe(id);
        mService.getRecipesFiltered(rModel).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {
                        view.setListData(recs);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("SUBSCRIBER FAILED", e.getMessage());
                    }

                    @Override
                    public void onNext(Result result) {
                        if (result.getRecipes().size() > 0){
                            recs.addAll(result.getRecipes());
                        }
                    }

                });
    }

    @Override
    public void sendComment(String comment, int id, int id1) {


        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);

        Comment com = new Comment("", id, comment, formattedDate,0);
        final Comment[] r = new Comment[1];
        mService.sendComment(id1, com).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {
                        Log.d("SUBSCRIBER COMMENT INFO", "COMMENT SENT");
                        //view.ADD COMMENT TO LIST();

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("SUBSCRIBER FAILED", e.getMessage());
                    }

                    @Override
                    public void onNext(Result result) {
                        r[0] = result.getComments().get(0);
                    }

                });
    }

    @Override
    public void deleteRecipe(final int idRecipe) {
        final int[] state = new int[1];
        //Observable<Recipe> call = mService.getRecipe(id);
        mService.removeRecipe(idRecipe).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {
                        if(state[0] == 0){
                            deleteMyRecipe(idRecipe);
                        }
                        else if(state[0] == 1){
                            Log.d("SUBSCRIBER DELETE", "LA RECETA NO SE HA BORRADO.");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("SUBSCRIBER FAILED", e.getMessage());
                    }

                    @Override
                    public void onNext(Result result) {
                        if(result.getCode() == true && result.getStatus() == 200)
                            state[0] = 0;
                        else{
                            state[0] = 1;
                        }
                    }

                });
    }

    private Recipe consRecipe(Cursor data){
        data.moveToPosition(0);
        return new Recipe(data.getInt(0), data.getInt(1), data.getString(2), data.getString(3),
                data.getString(4), data.getString(5), data.getString(6), data.getInt(7),
                data.getString(8), data.getInt(9), data.getString(10), data.getString(11),
                data.getString(12), data.getFloat(13), data.getInt(14));

    }

    private ContentValues recipeToValues(Recipe tmp){
        ContentValues cv = new ContentValues();

        cv.put(DatabaseContract.RecipeEntry.COLUMN_AUTHOR, tmp.getAuthor());
        cv.put(DatabaseContract.RecipeEntry.COLUMN_AUTHORNAME, tmp.getAuthorName());
        cv.put(DatabaseContract.RecipeEntry.COLUMN_NAME, tmp.getName());
        cv.put(DatabaseContract.RecipeEntry.COLUMN_CATEGORIES, tmp.getCategories());
        cv.put(DatabaseContract.RecipeEntry.COLUMN_INGREDIENTS, tmp.getIngredients());
        cv.put(DatabaseContract.RecipeEntry.COLUMN_ELABORATION, tmp.getElaboration());
        cv.put(DatabaseContract.RecipeEntry.COLUMN_TIME , tmp.getTime());
        cv.put(DatabaseContract.RecipeEntry.COLUMN_DIFFICULTY, tmp.getDifficulty());
        cv.put(DatabaseContract.RecipeEntry.COLUMN_NPERS, tmp.getnPers());
        cv.put(DatabaseContract.RecipeEntry.COLUMN_DATE , tmp.getDate());
        cv.put(DatabaseContract.RecipeEntry.COLUMN_IMAGE, tmp.getImage());
        cv.put(DatabaseContract.RecipeEntry.COLUMN_SOURCE, tmp.getSource());
        return cv;
    }

}
