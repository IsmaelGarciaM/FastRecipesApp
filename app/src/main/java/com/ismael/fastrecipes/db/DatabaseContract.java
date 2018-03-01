package com.ismael.fastrecipes.db;

import android.provider.BaseColumns;

/**
 * Created by Ismael on 24/01/2018.
 */

public class DatabaseContract {
    private DatabaseContract(){}

    public static class RecipeEntry implements BaseColumns {
        public static final String CONTENT_PATH = "recipe";
        public static final String TABLE_NAME = "recipe";
        public static final String COLUMN_ID = "idr";
        public static final String COLUMN_AUTHOR = "idAuthor";
        public static final String COLUMN_AUTHORNAME = "authorName";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CATEGORIES = "categories";
        public static final String COLUMN_INGREDIENTS = "ingredients";
        public static final String COLUMN_ELABORATION= "elaboration";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_DIFFICULTY = "difficulty";
        public static final String COLUMN_NPERS = "npers";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_SOURCE = "source";

        public static final String SQL_CREATE_ENTRIES = String.format("CREATE TABLE %s ( %s INTEGER UNIQUE, " +
                "%s INTEGER NOT NULL," +
                "%s TEXT NOT NULL," +
                "%s TEXT NOT NULL," +
                "%s TEXT, " +
                "%s TEXT NOT NULL," +
                "%s TEXT NOT NULL," +
                "%s INTEGER," +
                "%s TEXT," +
                "%s TEXT," +
                "%s TEXT NOT NULL," +
                "%s TEXT NOT NULL," +
                "%s TEXT NOT NULL" +
                        ")", TABLE_NAME, COLUMN_ID,COLUMN_AUTHOR, COLUMN_AUTHORNAME, COLUMN_NAME, COLUMN_CATEGORIES, COLUMN_INGREDIENTS,COLUMN_ELABORATION, COLUMN_TIME,
                COLUMN_DIFFICULTY, COLUMN_NPERS, COLUMN_DATE,COLUMN_IMAGE, COLUMN_SOURCE);



        public static final String SQL_DELETE_ENTRIES = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);



        public static final String[] ALL_COLUMNS = new String[]{COLUMN_ID, COLUMN_AUTHOR, COLUMN_AUTHORNAME, COLUMN_NAME, COLUMN_CATEGORIES, COLUMN_INGREDIENTS,COLUMN_ELABORATION, COLUMN_TIME,
                COLUMN_DIFFICULTY, COLUMN_NPERS, COLUMN_DATE,COLUMN_IMAGE, COLUMN_SOURCE};
        public static final String DEFAULT_SORT = COLUMN_NAME;


    }

    public static class FavouriteRecipeEntry implements BaseColumns {
        public static final String CONTENT_PATH = "favrecipes";
        public static final String TABLE_NAME = "favrecipes";
        public static final String COLUMN_ID = "idr";
        public static final String COLUMN_AUTHOR = "idAuthor";
        public static final String COLUMN_AUTHORNAME = "authorName";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CATEGORIES = "categories";
        public static final String  COLUMN_INGREDIENTS = "ingredients";
        public static final String COLUMN_ELABORATION= "elaboration";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_DIFFICULTY = "difficulty";
        public static final String COLUMN_NPERS = "npers";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_SOURCE = "source";
        public static final String COLUMN_FAV = "fav";




        public static final String SQL_CREATE_ENTRIES = String.format("CREATE TABLE %s (%s INTEGER UNIQUE, " +
                        "%s INTEGER NOT NULL," +
                        "%s TEXT NOT NULL," +
                        "%s TEXT NOT NULL," +
                        "%s TEXT, " +
                        "%s TEXT NOT NULL," +
                        "%s TEXT NOT NULL," +
                        "%s INTEGER," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s TEXT, " +
                        "%s TEXT," +
                        "%s INTEGER NOT NULL" +
                        ")", TABLE_NAME, COLUMN_ID,COLUMN_AUTHOR, COLUMN_AUTHORNAME, COLUMN_NAME, COLUMN_CATEGORIES, COLUMN_INGREDIENTS, COLUMN_ELABORATION, COLUMN_TIME,
                COLUMN_DIFFICULTY, COLUMN_NPERS, COLUMN_DATE,COLUMN_IMAGE, COLUMN_SOURCE, COLUMN_FAV );



        public static final String SQL_DELETE_ENTRIES = String.format("DROP TABLE IF EXISTS %s ", TABLE_NAME);



        public static final String[] ALL_COLUMNS = new String[]{COLUMN_ID, COLUMN_NAME};
        public static final String DEFAULT_SORT = COLUMN_NAME;


    }

}
