package com.ismael.fastrecipes;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


    public interface PrefsListener{
        void showConfig();
    }

    public SettingsPreferences(){

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
                Log.d("Preferences", "Cerrar sesión");//code for what you want it to do
                return true;
            }
        });

    }

}
