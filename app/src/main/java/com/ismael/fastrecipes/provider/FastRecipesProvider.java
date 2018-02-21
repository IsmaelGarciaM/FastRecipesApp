package com.ismael.fastrecipes.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.ismael.fastrecipes.R;
import com.ismael.fastrecipes.db.DatabaseContract;
import com.ismael.fastrecipes.db.DatabaseHelper;

/**
 * Created by Ismael on 24/01/2018.
 */

public class FastRecipesProvider extends ContentProvider {

    public static final int RECIPE = 1;
    public static final int INGREDIENT = 2;
    public static final int FAVRECIPES = 3;

    public static final int RECIPE_ID= 11;
    public static final int FAVRECIPE_ID= 12;

    private SQLiteDatabase _database;

    public static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(FastRecipesContract.AUTHORITY, FastRecipesContract.Recipe.CONTENT_PATH, RECIPE);
        uriMatcher.addURI(FastRecipesContract.AUTHORITY, FastRecipesContract.Recipe.CONTENT_PATH+"/#", RECIPE_ID);

        uriMatcher.addURI(FastRecipesContract.AUTHORITY, FastRecipesContract.Ingredient.CONTENT_PATH, INGREDIENT);

        uriMatcher.addURI(FastRecipesContract.AUTHORITY, FastRecipesContract.FavRecipe.CONTENT_PATH, FAVRECIPES);
        uriMatcher.addURI(FastRecipesContract.AUTHORITY, FastRecipesContract.FavRecipe.CONTENT_PATH+"/#", FAVRECIPE_ID);
    }


    @Override
    public boolean onCreate() {
        _database = DatabaseHelper.getInstance().openDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        int affected = -1;
        String rowId;
        switch (uriMatcher.match(uri)){
            case RECIPE:
                builder.setTables(DatabaseContract.RecipeEntry.TABLE_NAME);
                projection = FastRecipesContract.Recipe.PROJECTION;
                break;

            case RECIPE_ID:
                rowId = uri.getPathSegments().get(1);
                selection = DatabaseContract.RecipeEntry._ID+"="+rowId;
                builder.setTables(DatabaseContract.RecipeEntry.TABLE_NAME);

                break;

            case INGREDIENT:
                builder.setTables(DatabaseContract.IngredientEntry.TABLE_NAME);
                projection = FastRecipesContract.Ingredient.PROJECTION;
                selection = FastRecipesContract.Ingredient.SELECTION + selectionArgs[0] ;
                break;

            case FAVRECIPES:
                builder.setTables(DatabaseContract.FavouriteRecipeEntry.TABLE_NAME);
                projection = FastRecipesContract.FavRecipe.PROJECTION;
                break;

            case FAVRECIPE_ID:
                rowId = uri.getPathSegments().get(1);
                builder.setTables(DatabaseContract.FavouriteRecipeEntry.TABLE_NAME);
                projection = FastRecipesContract.FavRecipe.PROJECTION;
                selection = DatabaseContract.RecipeEntry._ID+"="+rowId;
                break;

            case UriMatcher.NO_MATCH:
                throw new IllegalArgumentException("Invalid URI");
        }
        String query = builder.buildQuery(projection, selection, null, null, sortOrder, null);
        Log.i("PROVIDER", query);
        cursor = builder.query(_database, projection, selection, null, null, null, sortOrder);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        String res = "";
        switch (uriMatcher.match(uri)) {
            case RECIPE:
                res = "vnd.android.cursor.dir/vnd.ismael.fastrecipesprovider.recipe";
                break;
            case RECIPE_ID:
                res = "vnd.android.cursor.item/vnd.ismael.fastrecipesprovider.recipe";
                break;

            case INGREDIENT:
                res = "vnd.android.cursor.dir/vnd.ismael.fastrecipesprovider.ingredient";
                break;

            case FAVRECIPES:
                res = "vnd.android.cursor.dir/vnd.ismael.fastrecipesprovider.favrecipes";
                break;

            case FAVRECIPE_ID:
                res = "vnd.android.cursor.item/vnd.ismael.fastrecipesprovider.favrecipes";
                break;

        }

        return res;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Uri u = null;
        long regId = -1;
        switch (uriMatcher.match(uri)) {
            case RECIPE:
                regId = _database.insert(DatabaseContract.RecipeEntry.TABLE_NAME, null, contentValues);
                u = ContentUris.withAppendedId(uri, regId);
                break;
            case FAVRECIPES:
                regId = _database.insert(DatabaseContract.FavouriteRecipeEntry.TABLE_NAME, null, contentValues);
                u = ContentUris.withAppendedId(uri, regId);
                break;
            default: u = null;
        }

        if(regId != -1){
            getContext().getContentResolver().notifyChange(u, null);
        }
        else
            throw new SQLException("Error al insertar");
        return u;    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int nRows = -1;
        switch (uriMatcher.match(uri)){
            case RECIPE:
                s = FastRecipesContract.Recipe._ID+"="+uri.getLastPathSegment();
                nRows = _database.delete(DatabaseContract.RecipeEntry.TABLE_NAME, s, null);
                break;
            case FAVRECIPES:
                s = FastRecipesContract.FavRecipe._ID+"="+uri.getLastPathSegment();
                nRows = _database.delete(DatabaseContract.FavouriteRecipeEntry.TABLE_NAME, s, null);
                break;
        }

        if(nRows != -1)
            getContext().getContentResolver().notifyChange(uri, null);

        return nRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {


        int affected = -1;
        switch (uriMatcher.match(uri)) {

            case RECIPE_ID:
                affected = _database.update(DatabaseContract.RecipeEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
 /*
            case INGREDIENT:
                affected = _database.update(DatabaseContract.IngredientEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;


            case FAVRECIPES_ID:
                rowId = uri.getLastPathSegment();
                selection = DatabaseContract.RecipeIngredientsEntry._ID+"=";
                selectionArgs = new String[]{rowId};
                affected = _database.update(DatabaseContract.RecipeIngredientsEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;*/
        }

        if(affected != -1){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        else
            throw new SQLException("Error al actualizar");

        return affected;
    }
}
