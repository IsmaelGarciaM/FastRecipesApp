package com.ismael.fastrecipes;

import android.app.Application;
import android.content.Context;

import com.ismael.fastrecipes.utils.FastRecipesService;

/**
 * Created by Ismael on 22/01/2018.
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
