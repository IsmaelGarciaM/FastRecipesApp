package com.ismael.fastrecipes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;


import com.ismael.fastrecipes.interfaces.Prefs;

/**
 * SettingPreferences -> Vista de la configuración de la aplicación
 * @author Ismael Garcia
 */

public class SettingsPreferences extends PreferenceFragment implements Prefs.View{
    private Preference prefCloseSession;
    private SwitchPreference rememberUser;
    private Preference credits;
    private Preference conditions;
    private Preference aboutUs;
    private Preference profesional;
    PrefsListener mCallback;
    static SettingsPreferences pfInstance;


    public interface PrefsListener{
        void showConfig();
        void closeSession();
    }


    public static SettingsPreferences getInstance(Bundle args){

        if(pfInstance == null){
            pfInstance = new SettingsPreferences();
            pfInstance.setArguments(new Bundle());
        }
        if(args!=null) {
            pfInstance.getArguments().putAll(args);
        }
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
        rememberUser = (SwitchPreference)  findPreference("remember_user");

        credits = (Preference)  findPreference("credits");
        conditions = (Preference)  findPreference("conditions");
        profesional = (Preference)  findPreference("profesional");
        aboutUs = (Preference)  findPreference("about_us");
        //rememberUser = (SwitchPreference)  findPreference("remember_user");

        prefCloseSession.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                mCallback.closeSession();
                //Log.d("Preferences", "Cerrar sesión");//code for what you want it to do
                return true;
            }
        });

        aboutUs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Uri uri = Uri.parse("http://www.fastrecipesapp.com/sobre-nosotros");
                showContent(uri);

                return false;
            }
        });

        credits.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Uri uri = Uri.parse("http://www.fastrecipesapp.com/creditos");
                showContent(uri);
                return false;
            }
        });

        conditions.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Uri uri = Uri.parse("http://www.fastrecipesapp.com/terminos-y-condiciones");
                showContent(uri);
                return false;
            }
        });

        profesional.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Uri uri = Uri.parse("https://www.linkedin.com/in/ismael-garc%C3%ADa-mart%C3%ADnez-a17a94139/");

                return false;
            }
        });

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

    void showContent(Uri uri){
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

}
