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
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_AUTHORNAME = "authorname";
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

        public static final String SQL_CREATE_ENTRIES = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "%s INTEGER NOT NULL," +
                "%s TEXT NOT NULL," +
                "%s TEXT NOT NULL," +
                "%s TEXT, " +
                "%s TEXT NOT NULL," +
                "%s TEXT NOT NULL," +
                "%s INTEGER," +
                "%s TEXT," +
                "%s INTEGER," +
                "%s TEXT NOT NULL," +
                "%s TEXT NOT NULL," +
                "%s TEXT NOT NULL" +
                        ")", TABLE_NAME, BaseColumns._ID,COLUMN_AUTHOR, COLUMN_AUTHORNAME, COLUMN_NAME, COLUMN_CATEGORIES, COLUMN_INGREDIENTS,COLUMN_ELABORATION, COLUMN_TIME,
                COLUMN_DIFFICULTY, COLUMN_NPERS, COLUMN_DATE,COLUMN_IMAGE, COLUMN_SOURCE);



        public static final String SQL_DELETE_ENTRIES = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);



        public static final String[] ALL_COLUMNS = new String[]{BaseColumns._ID, COLUMN_AUTHOR, COLUMN_AUTHORNAME, COLUMN_NAME, COLUMN_CATEGORIES, COLUMN_INGREDIENTS,COLUMN_ELABORATION, COLUMN_TIME,
                COLUMN_DIFFICULTY, COLUMN_NPERS, COLUMN_DATE,COLUMN_IMAGE, COLUMN_SOURCE};
        public static final String DEFAULT_SORT = COLUMN_NAME;


    }

    public static class IngredientEntry implements BaseColumns {
        public static final String CONTENT_PATH = "ingredient";
        public static final String TABLE_NAME = "ingredient";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_MEASUREMENT = "measurement";



        public static final String SQL_CREATE_ENTRIES = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT NOT NULL," +
                        "%s TEXT NOT NULL" +
                        ")", TABLE_NAME, BaseColumns._ID, COLUMN_NAME, COLUMN_MEASUREMENT);


        public static final String SQL_DELETE_ENTRIES = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);


        public static final String[] ALL_COLUMNS = new String[]{BaseColumns._ID, COLUMN_NAME, COLUMN_MEASUREMENT};
        public static final String DEFAULT_SORT = COLUMN_NAME;
    }

    public static class RecipeIngredientsEntry implements BaseColumns {
        public static final String CONTENT_PATH = "recipeingredients";
        public static final String TABLE_NAME = "recipeingredients";
        public static final String COLUMN_IDRECIPE = "id_recipe";
        public static final String COLUMN_IDINGREDIENT = "id_ingredient";
        public static final String COLUMN_QUANTITY= "quantity";



        public static final String SQL_CREATE_ENTRIES = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "%s INTEGER NOT NULL," +
                "%s INTEGER NOT NULL," +
                "%s REAL NOT NULL" +
                ")", TABLE_NAME, BaseColumns._ID, COLUMN_IDRECIPE, COLUMN_IDINGREDIENT, COLUMN_QUANTITY);


        public static final String SQL_DELETE_ENTRIES = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);

        public static final String[] ALL_COLUMNS = new String[]{BaseColumns._ID, COLUMN_IDRECIPE, COLUMN_IDINGREDIENT, COLUMN_QUANTITY};
    }


    public static class FavouriteRecipeEntry implements BaseColumns {
        public static final String CONTENT_PATH = "favrecipes";
        public static final String TABLE_NAME = "favrecipes";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_AUTHORNAME = "authorname";
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



        public static final String SQL_CREATE_ENTRIES = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s INTEGER NOT NULL," +
                        "%s TEXT NOT NULL," +
                        "%s TEXT NOT NULL," +
                        "%s TEXT, " +
                        "%s TEXT NOT NULL," +
                        "%s TEXT NOT NULL," +
                        "%s INTEGER," +
                        "%s TEXT," +
                        "%s INTEGER," +
                        "%s TEXT," +
                        "%s TEXT, " +
                        "%s TEXT" +
                        ")", TABLE_NAME, BaseColumns._ID,COLUMN_AUTHOR, COLUMN_AUTHORNAME, COLUMN_NAME, COLUMN_CATEGORIES, COLUMN_INGREDIENTS, COLUMN_ELABORATION, COLUMN_TIME,
                COLUMN_DIFFICULTY, COLUMN_NPERS, COLUMN_DATE,COLUMN_IMAGE, COLUMN_SOURCE );



        public static final String SQL_DELETE_ENTRIES = String.format("DROP TABLE IF EXISTS %s ", TABLE_NAME);



        public static final String[] ALL_COLUMNS = new String[]{BaseColumns._ID, COLUMN_NAME};
        public static final String DEFAULT_SORT = COLUMN_NAME;


    }

}
