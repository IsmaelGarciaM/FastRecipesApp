package com.ismael.fastrecipes.presenter;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.ismael.fastrecipes.FastRecipesApplication;
import com.ismael.fastrecipes.interfaces.RecipesPresenter;
import com.ismael.fastrecipes.model.Comment;
import com.ismael.fastrecipes.model.Recipe;
import com.ismael.fastrecipes.provider.FastRecipesContract;
import com.ismael.fastrecipes.utils.FastRecipesService;
import com.ismael.fastrecipes.utils.Result;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ismael.fastrecipes.provider.FastRecipesProvider.FAVRECIPES;
import static com.ismael.fastrecipes.provider.FastRecipesProvider.FAVRECIPE_ID;
import static com.ismael.fastrecipes.provider.FastRecipesProvider.RECIPE;
import static com.ismael.fastrecipes.provider.FastRecipesProvider.RECIPE_ID;

/**
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

    @Override
    public void getMyRecipesList() {
        ((Activity)context).getLoaderManager().restartLoader(RECIPE, null, this);
    }

    @Override
    public void addRecipe() {

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
                            int a = 0;
                        }
                    }

                });
    }


/*        Recipe recipe = mService.getRecipe(id);

        CompositeDisposable mCompo;
*/



    @Override
    public void getFavRecipes(){
        ((Activity)context).getLoaderManager().restartLoader(FAVRECIPES, null, this);
    }

    @Override
    public void getMyRecipe(int idRecipe) {
        Bundle b = new Bundle();
        b.putInt("id", idRecipe);
        ((Activity)context).getLoaderManager().restartLoader(RECIPE_ID, b, this);
    }

    @Override
    public void getFavRecipe(int idRecipe) {
        Bundle b = new Bundle();
        b.putInt("id", idRecipe);
        ((Activity)context).getLoaderManager().restartLoader(FAVRECIPE_ID, b, this);

    }

    @Override
    public void setFavourite(int idUser, int idRecipe) {
        final Recipe[] r = new Recipe[1];
        final ArrayList<Comment> c = new ArrayList<>();
        //Observable<Recipe> call = mService.getRecipe(id);
        mService.setFavouriteRec(idUser, idRecipe).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {
                       view.setFavState();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("SUBSCRIBER FAILED", e.getMessage());
                    }

                    @Override
                    public void onNext(Result result) {
                    }

                });
    }

    @Override
    public void deleteRecipe(int idRecipe) {

    }

    private Recipe consRecipe(Cursor data){
        data.moveToPosition(0);
        return new Recipe(data.getInt(0), data.getInt(1), data.getString(2),
                data.getString(3), data.getString(4), data.getString(5), data.getInt(6),
                data.getString(7), data.getInt(8), data.getString(9), data.getString(10),
                data.getString(11), 0);

    }

}
