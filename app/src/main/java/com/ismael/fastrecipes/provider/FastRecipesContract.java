package com.ismael.fastrecipes.provider;

import android.content.Context;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Ismael on 24/01/2018.
 */

public class FastRecipesContract {

    public static final String AUTHORITY="com.ismael.fastrecipes";
    public static final Uri AUTHORITY_URI = Uri.parse("content://"+AUTHORITY);


    private FastRecipesContract(Context c){

    }


    public static class Recipe implements BaseColumns {

        public static final String CONTENT_PATH = "recipe";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_PATH);
        public static final String AUTHOR = "author";
        public static final String AUTHORNAME = "authorname";
        public static final String NAME = "name";
        public static final String CATEGORIES = "categories";
        public static final String ELABORATION= "elaboration";
        public static final String TIME = "time";
        public static final String DIFFICULTY = "difficulty";
        public static final String NPERS = "npers";
        public static final String DATE = "date";
        public static final String IMAGE = "image";
        public static final String SOURCE = "source";
        public static final String[] PROJECTION = new String[]{"*"};

    }

    public static class Ingredient implements BaseColumns {

        public static final String CONTENT_PATH = "ingredient";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_PATH);
        public static final String NAME = "name";
        public static final String MEASUREMENT = "measurement";
        public static final String[] PROJECTION = new String[]{BaseColumns._ID, NAME};
        public static final String SELECTION =  "name LIKE ";

    }

    public static class FavRecipe implements BaseColumns {

        public static final String CONTENT_PATH = "favrecipes";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, CONTENT_PATH);
        public static final String AUTHOR = "author";
        public static final String AUTHORNAME = "authorname";
        public static final String NAME = "name";
        public static final String CATEGORIES = "categories";
        public static final String INGREDIENTS = "ingredients";
        public static final String ELABORATION= "elaboration";
        public static final String TIME = "time";
        public static final String DIFFICULTY = "difficulty";
        public static final String NPERS = "npers";
        public static final String DATE = "date";
        public static final String IMAGE = "image";
        public static final String SOURCE = "source";
        public static final String[] PROJECTION = new String[]{"*"};

    }




}
