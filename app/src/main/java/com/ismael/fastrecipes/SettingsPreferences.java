package com.ismael.fastrecipes;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;


import com.ismael.fastrecipes.interfaces.Prefs;

/**
 * Created by Ismael on 10/02/2018.
 */

public class SettingsPreferences extends PreferenceFragment implements Prefs.View{
    private Preference prefCloseSession;
    private SwitchPreference rememberUser;
    private Preference changePassword;
    private Preference help;
    private Preference terms;
    private Preference sendEmail;
    PrefsListener mCallback;
    static SettingsPreferences pfInstance;


    public interface PrefsListener{
        void showConfig();
        void closeSession();
    }


    public static SettingsPreferences getInstance(Bundle args){

        if(pfInstance == null){
            pfInstance = new SettingsPreferences();
        }
        pfInstance.setArguments(args);
        return  pfInstance;
    }

    public SettingsPreferences(){

    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        try {
            mCallback = (PrefsListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.getMessage() + " activity must implement PrefsListener interface");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Indicate here the XML resource you created above that holds the preferences
        addPreferencesFromResource(R.xml.general_settings);
        prefCloseSession = (Preference)  findPreference("close_session");
        prefCloseSession.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                mCallback.closeSession();
                //Log.d("Preferences", "Cerrar sesión");//code for what you want it to do
                return true;
            }
        });
        rememberUser = (SwitchPreference)  findPreference("remember_user");

        final SharedPreferences sp = FastRecipesApplication.getContext().getSharedPreferences("fastrecipessp", Context.MODE_PRIVATE);

        boolean tmp = sp.getBoolean("remember_user", false);
        rememberUser.setChecked(tmp);
        rememberUser.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if(rememberUser.isChecked())
                    sp.edit().putBoolean("remember_user", true).apply();
                else
                    sp.edit().putBoolean("remember_user", false).apply();

                return true;
            }
        });



    }

    SharedPreferences getPreferences(){
        return getContext().getSharedPreferences("fastrecipessp",Context.MODE_PRIVATE);
    }

}
