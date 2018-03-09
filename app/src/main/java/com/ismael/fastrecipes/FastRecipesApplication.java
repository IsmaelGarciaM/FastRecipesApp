package com.ismael.fastrecipes;

import android.app.Application;
import android.content.Context;
import com.ismael.fastrecipes.utils.FastRecipesService;

/**
 * FastRecipesApplication -> Clase Application
 * @author Ismael Garcia
 */
public class FastRecipesApplication extends Application {
    private static FastRecipesApplication instance;
    private static FastRecipesService fastRecipesService;

    @Override
    public void onCreate() {
        super.onCreate();
        fastRecipesService = new FastRecipesService();
    }

    public static FastRecipesService getFastRecipesService(){
        return fastRecipesService;
    }

    public FastRecipesApplication(){
        this.instance = this;
    }

    public static Context getContext(){
        return instance;
    }
}
