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

    private static final int VERSION = 14;
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
            db.execSQL(DatabaseContract.IngredientEntry.SQL_CREATE_ENTRIES);
            Log.d("database", "create table ingredients");

            db.execSQL(DatabaseContract.RecipeEntry.SQL_CREATE_ENTRIES);
            db.execSQL(DatabaseContract.FavouriteRecipeEntry.SQL_CREATE_ENTRIES);
            Log.d("database", "create table favrecipes");

            //db.execSQL(DatabaseContract.RecipeIngredientsEntry.SQL_CREATE_ENTRIES);
            db.execSQL("insert into " + DatabaseContract.IngredientEntry.TABLE_NAME + " values (0, 'sal', 'pizca')");
            db.execSQL("insert into " + DatabaseContract.IngredientEntry.TABLE_NAME + " values (1, 'azucar', 'mg')");
            db.execSQL("insert into " + DatabaseContract.IngredientEntry.TABLE_NAME + " values (2, 'agua', 'ml')");
            db.execSQL("insert into " + DatabaseContract.IngredientEntry.TABLE_NAME + " values (3, 'harina', 'mg')");
            db.execSQL("insert into " + DatabaseContract.IngredientEntry.TABLE_NAME + " values (4, 'levadura', 'gr')");
            db.execSQL("insert into " + DatabaseContract.IngredientEntry.TABLE_NAME + " values (5, 'aceite de oliva', 'ml')");
            db.execSQL("insert into " + DatabaseContract.IngredientEntry.TABLE_NAME + " values (6, 'patatas', 'uds')");
            db.execSQL("insert into " + DatabaseContract.IngredientEntry.TABLE_NAME + " values (7, 'calabacín', 'uds')");
            db.execSQL("insert into " + DatabaseContract.IngredientEntry.TABLE_NAME + " values (8, 'pimienta', '')");
            db.execSQL("insert into " + DatabaseContract.IngredientEntry.TABLE_NAME + " values (9, 'perejil', '')");
            db.execSQL("insert into " + DatabaseContract.IngredientEntry.TABLE_NAME + " values (10, 'pollo', 'gr')");
            db.execSQL("insert into " + DatabaseContract.IngredientEntry.TABLE_NAME + " values (11, 'atun', 'gr')");
            db.execSQL("insert into " + DatabaseContract.IngredientEntry.TABLE_NAME + " values (12, 'huevo(s)', 'uds')");
            db.execSQL("insert into " + DatabaseContract.IngredientEntry.TABLE_NAME + " values (13, 'vinagre', 'ml')");
            db.execSQL("insert into " + DatabaseContract.IngredientEntry.TABLE_NAME + " values (14, 'cebolla', 'gr')");
            db.execSQL("insert into " + DatabaseContract.IngredientEntry.TABLE_NAME + " values (15, 'alcachofas', 'uds')");
            db.execSQL("insert into " + DatabaseContract.IngredientEntry.TABLE_NAME + " values (16, 'cacao', 'gr')");
            db.execSQL("insert into " + DatabaseContract.IngredientEntry.TABLE_NAME + " values (17, 'naranja', 'uds')");
            db.execSQL("insert into " + DatabaseContract.IngredientEntry.TABLE_NAME + " values (18, 'mantequilla', 'gr')");
            db.execSQL("insert into " + DatabaseContract.IngredientEntry.TABLE_NAME + " values (19, 'nueces', 'gr')");
            db.execSQL("insert into " + DatabaseContract.IngredientEntry.TABLE_NAME + " values (20, 'romero', 'gr')");
            db.execSQL("insert into " + DatabaseContract.IngredientEntry.TABLE_NAME + " values (21, 'tabasco', '')");





            db.execSQL("insert into " + DatabaseContract.FavouriteRecipeEntry.TABLE_NAME + " values (0,'Fasthecipesteam','Tarta de nueces y chocolate','Dulces y postres','150 gr azúcar \r\n 150 gr nueces\r\n 100 gr mantequilla\r\n 3 huevos\r\n 2 cucharadas harina\r\n Una pizca de sal\r\n 150 gr chocolate fondant\r\n 20 gr Mantequilla','1. Lo primero que haremos será rayar las nueces, NO triturarlas, sobre un cuenco amplio. Esto es un paso muy importante ya que si hacemos \"harina\" con las nueces la tarta no quedará igual.\r\n 2. Cuando terminemos, mezclamos con la mitad del azúcar y una pizca de sal.\\r\\n 3. Colocamos la mantequilla en un vaso y disolvemos durante unos segundos en el microondas. Sacamos, añadimos la otra mitad del azúcar y mezclamos bien.\r\n 4. Vertemos sobre la mezcla de nueces y añadimos los huevos de uno en uno. Es decir, hasta que no tengamos integrado en la masa un huevo, no añadimos otro.\r\n 5. Incorporamos la harina y mezclamos nuevamente.\\r\\n 6. Engrasamos un molde de 22 o 24 cm, y vertemos la masa de la tarta. La Tarta de nuez y chocolate debe de quedar fina, de dos centímetros de altura como mucho.\r\n 7. Introducimos el molde en el horno, que tendremos precalentado a 200º, durante 15-20 minutos.\r\n 8. Sacamos y dejamos enfriar.\\r\\n 9. Finalmente preparamos la cobertura de chocolate, para ello derretimos el chocolate al baño maría o en el microondas. Añadimos la mantequilla y mezclamos muy bien.\r\n 10. Repartimos el chocolate sobre toda la superficie de la tarta y decoramos con unas nueces.\r\n 11. Dejamos enfriar nuevamente. Servimos y degustamos.',90,'Fácil','4','18/02/18','http://www.eladerezo.com/wp-content/uploads/2017/10/tarta-de-nuez-y-chocolate-4-1200x599.jpg', '')");
            db.execSQL("insert into " + DatabaseContract.FavouriteRecipeEntry.TABLE_NAME + " values (1,'Fasthecipesteam','Pollo asado fácil y diferente','Carnes','1 Pollo entero y limpio \r\n 6 patatas pequeñas\r\n 1 Calabacín\r\n  200 g Salsa fresca de mango con especias\r\n  sal\r\n  pimienta\r\n  perejil',' 1. Limpiamos el pollo de posibles restos de plumas y quitamos el exceso de grasa que encontremos\r\n 2. Salpimentamos por el interior y exterior.\r\n 3. Lavamos las patatas y el calabacín. Este último le cortamos en rodajas de unos tres centímetros.\r\n 4. Colocamos el pollo con la parte de las pechugas hacia el fondo de una fuente de horno. Repartimos las patatas y el calabacín alrededor del pollo. Sazonamos las verduras a nuestro gusto. Vertemos 100 ml de agua en la fuente del horno.\r\n 5. Encendemos a 200º el horno e introducimos el pollo en el centro del mismo.\r\n 6. Dejamos hacer durante 20 minutos.\r\n 7. Sacamos la bandeja del horno. Embadurnamos el pollo con la salsa fresca de mango con la ayuda de un pincel.\r\n 8. Colocamos de nuevo el pollo, ahora con la parte de las pechugas hacia arriba, esparcimos un poco de perejil picado por encima y horneamos durante una hora y cuarto a 170º.\r\n 9. Pasado el tiempo, comprobamos que el pollo esté hecho. ¿Cómo se comprueba que el pollo está hecho? Pinchándole con una brocheta o aguja de hacer punto. Si entra sin hacer fuerza, la carne está en su punto. Si notamos resistencia, dejamos hacer durante más tiempo.',50,'Fácil','4','18/02/18','http://www.eladerezo.com/wp-content/uploads/2017/05/pollo-asado-13-751x500.jpg', '')");
            db.execSQL("insert into " + DatabaseContract.FavouriteRecipeEntry.TABLE_NAME + " values (2,'Fasthecipesteam','Tarta de nueces y chocolate','Dulces y postres','150 gr azúcar \r\n 150 gr nueces\r\n 100 gr mantequilla\r\n 3 huevos\r\n 2 cucharadas harina\r\n Una pizca de sal\r\n 150 gr chocolate fondant\r\n 20 gr Mantequilla','1. Lo primero que haremos será rayar las nueces, NO triturarlas, sobre un cuenco amplio. Esto es un paso muy importante ya que si hacemos \"harina\" con las nueces la tarta no quedará igual.\r\n 2. Cuando terminemos, mezclamos con la mitad del azúcar y una pizca de sal.\\r\\n 3. Colocamos la mantequilla en un vaso y disolvemos durante unos segundos en el microondas. Sacamos, añadimos la otra mitad del azúcar y mezclamos bien.\r\n 4. Vertemos sobre la mezcla de nueces y añadimos los huevos de uno en uno. Es decir, hasta que no tengamos integrado en la masa un huevo, no añadimos otro.\r\n 5. Incorporamos la harina y mezclamos nuevamente.\\r\\n 6. Engrasamos un molde de 22 o 24 cm, y vertemos la masa de la tarta. La Tarta de nuez y chocolate debe de quedar fina, de dos centímetros de altura como mucho.\r\n 7. Introducimos el molde en el horno, que tendremos precalentado a 200º, durante 15-20 minutos.\r\n 8. Sacamos y dejamos enfriar.\\r\\n 9. Finalmente preparamos la cobertura de chocolate, para ello derretimos el chocolate al baño maría o en el microondas. Añadimos la mantequilla y mezclamos muy bien.\r\n 10. Repartimos el chocolate sobre toda la superficie de la tarta y decoramos con unas nueces.\r\n 11. Dejamos enfriar nuevamente. Servimos y degustamos.',90,'Fácil','4','18/02/18','http://www.eladerezo.com/wp-content/uploads/2017/10/tarta-de-nuez-y-chocolate-4-1200x599.jpg', '')");
            db.execSQL("insert into " + DatabaseContract.FavouriteRecipeEntry.TABLE_NAME + " values (3,'Fasthecipesteam','Pollo asado fácil y diferente','Carnes','1 Pollo entero y limpio \r\n 6 patatas pequeñas\r\n 1 Calabacín\r\n  200 g Salsa fresca de mango con especias\r\n  sal\r\n  pimienta\r\n  perejil',' 1. Limpiamos el pollo de posibles restos de plumas y quitamos el exceso de grasa que encontremos\r\n 2. Salpimentamos por el interior y exterior.\r\n 3. Lavamos las patatas y el calabacín. Este último le cortamos en rodajas de unos tres centímetros.\r\n 4. Colocamos el pollo con la parte de las pechugas hacia el fondo de una fuente de horno. Repartimos las patatas y el calabacín alrededor del pollo. Sazonamos las verduras a nuestro gusto. Vertemos 100 ml de agua en la fuente del horno.\r\n 5. Encendemos a 200º el horno e introducimos el pollo en el centro del mismo.\r\n 6. Dejamos hacer durante 20 minutos.\r\n 7. Sacamos la bandeja del horno. Embadurnamos el pollo con la salsa fresca de mango con la ayuda de un pincel.\r\n 8. Colocamos de nuevo el pollo, ahora con la parte de las pechugas hacia arriba, esparcimos un poco de perejil picado por encima y horneamos durante una hora y cuarto a 170º.\r\n 9. Pasado el tiempo, comprobamos que el pollo esté hecho. ¿Cómo se comprueba que el pollo está hecho? Pinchándole con una brocheta o aguja de hacer punto. Si entra sin hacer fuerza, la carne está en su punto. Si notamos resistencia, dejamos hacer durante más tiempo.',50,'Fácil','4','18/02/18','http://www.eladerezo.com/wp-content/uploads/2017/05/pollo-asado-13-751x500.jpg', '')");

            db.execSQL("insert into " + DatabaseContract.RecipeEntry.TABLE_NAME + " values (0,'Fasthecipesteam','Tarta de nueces y chocolate','Dulces y postres','150 gr azúcar \r\n 150 gr nueces\r\n 100 gr mantequilla\r\n 3 huevos\r\n 2 cucharadas harina\r\n Una pizca de sal\r\n 150 gr chocolate fondant\r\n 20 gr Mantequilla','1. Lo primero que haremos será rayar las nueces, NO triturarlas, sobre un cuenco amplio. Esto es un paso muy importante ya que si hacemos \"harina\" con las nueces la tarta no quedará igual.\r\n 2. Cuando terminemos, mezclamos con la mitad del azúcar y una pizca de sal.\\r\\n 3. Colocamos la mantequilla en un vaso y disolvemos durante unos segundos en el microondas. Sacamos, añadimos la otra mitad del azúcar y mezclamos bien.\r\n 4. Vertemos sobre la mezcla de nueces y añadimos los huevos de uno en uno. Es decir, hasta que no tengamos integrado en la masa un huevo, no añadimos otro.\r\n 5. Incorporamos la harina y mezclamos nuevamente.\\r\\n 6. Engrasamos un molde de 22 o 24 cm, y vertemos la masa de la tarta. La Tarta de nuez y chocolate debe de quedar fina, de dos centímetros de altura como mucho.\r\n 7. Introducimos el molde en el horno, que tendremos precalentado a 200º, durante 15-20 minutos.\r\n 8. Sacamos y dejamos enfriar.\\r\\n 9. Finalmente preparamos la cobertura de chocolate, para ello derretimos el chocolate al baño maría o en el microondas. Añadimos la mantequilla y mezclamos muy bien.\r\n 10. Repartimos el chocolate sobre toda la superficie de la tarta y decoramos con unas nueces.\r\n 11. Dejamos enfriar nuevamente. Servimos y degustamos.',90,'Fácil','4','18/02/18','http://www.eladerezo.com/wp-content/uploads/2017/10/tarta-de-nuez-y-chocolate-4-1200x599.jpg', '')");
            db.execSQL("insert into " + DatabaseContract.RecipeEntry.TABLE_NAME + " values (1,'Fasthecipesteam','Pollo asado fácil y diferente','Carnes','1 Pollo entero y limpio \r\n 6 patatas pequeñas\r\n 1 Calabacín\r\n  200 g Salsa fresca de mango con especias\r\n  sal\r\n  pimienta\r\n  perejil',' 1. Limpiamos el pollo de posibles restos de plumas y quitamos el exceso de grasa que encontremos\r\n 2. Salpimentamos por el interior y exterior.\r\n 3. Lavamos las patatas y el calabacín. Este último le cortamos en rodajas de unos tres centímetros.\r\n 4. Colocamos el pollo con la parte de las pechugas hacia el fondo de una fuente de horno. Repartimos las patatas y el calabacín alrededor del pollo. Sazonamos las verduras a nuestro gusto. Vertemos 100 ml de agua en la fuente del horno.\r\n 5. Encendemos a 200º el horno e introducimos el pollo en el centro del mismo.\r\n 6. Dejamos hacer durante 20 minutos.\r\n 7. Sacamos la bandeja del horno. Embadurnamos el pollo con la salsa fresca de mango con la ayuda de un pincel.\r\n 8. Colocamos de nuevo el pollo, ahora con la parte de las pechugas hacia arriba, esparcimos un poco de perejil picado por encima y horneamos durante una hora y cuarto a 170º.\r\n 9. Pasado el tiempo, comprobamos que el pollo esté hecho. ¿Cómo se comprueba que el pollo está hecho? Pinchándole con una brocheta o aguja de hacer punto. Si entra sin hacer fuerza, la carne está en su punto. Si notamos resistencia, dejamos hacer durante más tiempo.',50,'Fácil','4','18/02/18','http://www.eladerezo.com/wp-content/uploads/2017/05/pollo-asado-13-751x500.jpg', '')");


            Log.d("database", "insertsok");



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
            db.execSQL(DatabaseContract.IngredientEntry.SQL_DELETE_ENTRIES);
            db.execSQL(DatabaseContract.RecipeEntry.SQL_DELETE_ENTRIES);
            db.execSQL(DatabaseContract.FavouriteRecipeEntry.SQL_DELETE_ENTRIES);
            db.execSQL(DatabaseContract.RecipeIngredientsEntry.SQL_DELETE_ENTRIES);
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
