package com.ismael.fastrecipes;

import android.app.Application;
import android.content.Context;

/**
 * Created by Ismael on 18/01/2018.
 */

public class FastRecipesApplication extends Application {
    static Context fra;

    @Override
    public void onCreate() {
        super.onCreate();
        fra = this;
    }

    public static Context getContext(){
        return  fra;
    }
}
