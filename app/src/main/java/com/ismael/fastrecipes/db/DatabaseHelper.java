package com.ismael.fastrecipes.db;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.ismael.fastrecipes.FastRecipesApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Ismael on 24/01/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 26;
    private static String NAME = "fastrecipes.db";
    private volatile static DatabaseHelper mDatabaseHelper;
    private static AtomicInteger aint;
    private SQLiteDatabase mDatabase;

    private DatabaseHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    public synchronized static DatabaseHelper getInstance(){
        if(mDatabaseHelper == null) {
            mDatabaseHelper = new DatabaseHelper(FastRecipesApplication.getContext());
            aint = new AtomicInteger();
        }

        return mDatabaseHelper;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.beginTransaction();
        try {
            db.execSQL(DatabaseContract.RecipeEntry.SQL_CREATE_ENTRIES);
            db.execSQL(DatabaseContract.FavouriteRecipeEntry.SQL_CREATE_ENTRIES);
            Log.d("database", "create table favrecipes");
            db.setTransactionSuccessful();
        }catch (SQLiteException sqlEx) {
            Log.d("database", sqlEx.getMessage() + "; Error al crear la base de datos");
        }
        finally {
            db.endTransaction();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
/*
        Log.d("database", "Updating table from " + oldVersion + " to " + newVersion);
        // You will not need to modify this unless you need to do some android specific things.
        // When upgrading the database, all you need to do is add a file to the assets folder and name it:
        // from_1_to_2.sql with the version that you are upgrading to as the last version.
        try {
            for (int i = oldVersion; i < newVersion; ++i) {
                String migrationName = String.format("from_%d_to_%d.sql", i, (i + 1));
                Log.d("database", "Looking for migration file: " + migrationName);
                readAndExecuteSQLScript(db, FastRecipesApplication.getContext(), migrationName);
            }
        } catch (Exception exception) {
            Log.e("database", "Exception running upgrade script:", exception);
        }

*/
        try {
            db.beginTransaction();
            db.execSQL(DatabaseContract.RecipeEntry.SQL_DELETE_ENTRIES);
            db.execSQL(DatabaseContract.FavouriteRecipeEntry.SQL_DELETE_ENTRIES);
            db.setTransactionSuccessful();
            Log.d("database", "DROP DATABASE COMPLETED");

        } catch (SQLiteException sqlEx) {
            Log.d("database", sqlEx.getMessage() + "; Error al crear la base de datos");
        } finally

        {
            db.endTransaction();
        }
        onCreate(db);
    }






    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        //if(db.isOpen())
                //db.setForeignKeyConstraintsEnabled(true);


    }

    public synchronized SQLiteDatabase openDatabase(){
        //if(aint.incrementAndGet() == 1){
            mDatabase = getWritableDatabase();
        //}
        return  mDatabase;
    }

    public synchronized void closeDatabase(){
        //if(aint.decrementAndGet() == 0){
            mDatabase.close();
        //}
    }

    private void readAndExecuteSQLScript(SQLiteDatabase db, Context ctx, String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            Log.d("database", "SQL script file name is empty");
            return;
        }

        Log.d("database", "Script found. Executing...");
        AssetManager assetManager = ctx.getAssets();
        BufferedReader reader = null;

        try {
            InputStream is = assetManager.open(fileName);
            InputStreamReader isr = new InputStreamReader(is);
            reader = new BufferedReader(isr);
            executeSQLScript(db, reader);
        } catch (IOException e) {
            Log.e("database", "IOException:", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("database", "IOException:", e);
                }
            }
        }

    }

    private void executeSQLScript(SQLiteDatabase db, BufferedReader reader) throws IOException {
        String line;
        StringBuilder statement = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            statement.append(line);
            statement.append("\n");
            if (line.endsWith(";")) {
                db.execSQL(statement.toString());
                statement = new StringBuilder();
            }
        }
    }
}
