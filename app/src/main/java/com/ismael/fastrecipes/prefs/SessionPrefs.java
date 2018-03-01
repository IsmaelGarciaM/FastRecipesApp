package com.ismael.fastrecipes.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by ismael on 26/05/17.
 */

public class SessionPrefs {

    public static final String PREFS_NAME = "FASTRECIPES_PREFS";
    public static final String PREF_USER_ID = "PREF_USER_ID";
    public static final String PREF_USER_TOKEN = "PREF_USER_TOKEN";

    private static SessionPrefs INSTANCE;
    private final SharedPreferences mPrefs;
    private boolean mIsLoggedIn = false;



    private SessionPrefs(Context context) {

        mPrefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        mIsLoggedIn = !TextUtils.isEmpty(mPrefs.getString(PREF_USER_TOKEN, null));
    }

    public static SessionPrefs get(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SessionPrefs(context);
        }
        return INSTANCE;
    }
/*
    public void saveUser(Client cliente) {
        if (cliente != null) {
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putInt(PREF_USER_ID, cliente.getIdr());
            editor.putString(PREF_USER_TOKEN, cliente.getType());
            editor.apply();
            mIsLoggedIn = true;
        }
    }/*/

    public void logOut(){
        mIsLoggedIn = false;
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREF_USER_ID, null);
        editor.putString(PREF_USER_TOKEN, null);
        editor.apply();
    }

    public boolean isLoggedIn(){
        return mIsLoggedIn;
    }
}
