package com.ismael.fastrecipes;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseUser;
import com.ismael.fastrecipes.interfaces.LoginPresenter;
import com.ismael.fastrecipes.model.Recipe;
import com.ismael.fastrecipes.model.User;
import com.ismael.fastrecipes.presenter.LoginPresenterImpl;
import com.ismael.fastrecipes.prefs.SessionPrefs;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

public class SplashScreen extends AppCompatActivity implements LoginPresenter.View{

    //Duración de splash screen
    private static final long SPLASH_SCREEN_DELAY = 2000;
    LoginPresenterImpl presenter;
    Context c;
    Recipe newR;
    Intent create;
    TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_splash_screen);
        presenter = new LoginPresenterImpl(this, this.getApplicationContext());
        c = this;
        Typeface font = Typeface.createFromAsset(getAssets(), "yummycupcakes.ttf");
        title = (TextView) findViewById(R.id.txvInit);
        title.setTypeface(font);
        Intent iAction = getIntent();
        final String action = iAction.getAction();
        final String type = iAction.getType();


        if(IsPlayServicesAvailable()) {

            TimerTask task = new TimerTask() {
                @Override
                public void run() {

                    // Envío a HomeActivity si el usuario está guardado y si no a LogInActivity

                    if (Intent.ACTION_SEND.equals(action) && type != null) {
                        if ("text/plain".equals(type)) {
                            newR = createNewRecipe();
                        }
                    }

                    //if(user is preferences){
                    presenter.logIn("", "");

                    /*if (presenter.logIn(user from preferences)) {
                        Intent mainIntent = new Intent().setClass(SplashScreen.this, HomeActivity.class);
                        startActivity(mainIntent);
                    }*/
                    //}
                    //else{

                    //}

                    // Cierre de la actividad para que no se almacene en la pila
                }
            };


            // Simulate a long loading process on application startup.
            Timer timer = new Timer();
            timer.schedule(task, SPLASH_SCREEN_DELAY);
        }


    }

    private Recipe createNewRecipe() {
        Recipe rTmp = new Recipe();
        /*Looper.getMainLooper().prepare();
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);*/
        String text = getIntent().getStringExtra(Intent.EXTRA_TEXT).toLowerCase();
        // = String.valueOf(clipboard.getText()).toLowerCase();//.getPrimaryClip().getItemAt(0).coerceToText(FastRecipesApplication.getContext())).toLowerCase();


        /*if(clipboard.hasPrimaryClip()) {
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            text = item.getText().toString().toLowerCase();
        }*/

        if(text.contains("ingredients")) {
            if(text.contains("description")) {
                rTmp.setIngredients(text.substring(text.indexOf("ingredients") + 13, text.indexOf("description") - 1));
                rTmp.setElaboration(text.substring(text.indexOf("description") + "description".length(), text.length()));
            }
            else if(text.contains("elaboration")) {
                rTmp.setIngredients(text.substring(text.indexOf("ingredients") + 13, text.indexOf("elaboration") - 1));
                rTmp.setElaboration(text.substring(text.indexOf("elaboration") + "elaboration".length(), text.length()));
            }
            else{
                rTmp.setIngredients(text.substring(text.indexOf("ingredients") + 13, text.length()));
            }

        }
        if(text.contains("ingredientes")) {
            if(text.contains("descripci")) {
                rTmp.setIngredients(text.substring(text.indexOf("ingredientes") + "ingredientes".length() +1, text.indexOf("descripci") - 1));
                rTmp.setElaboration(text.substring(text.indexOf("descripci") + "descripcion".length()+1, text.length()));
            }
            else if(text.contains("elaboraci")) {
                rTmp.setIngredients(text.substring(text.indexOf("ingredientes") + "ingredientes".length()+1, text.indexOf("elaboraci") - 1));
                rTmp.setElaboration(text.substring(text.indexOf("elaboraci") + "elaboracion".length() + 1, text.length()));
            }
            else if(text.contains("preparaci")) {
                rTmp.setIngredients(text.substring(text.indexOf("ingredientes") + "ingredientes".length()+1, text.indexOf("preparaci") - 1));
                rTmp.setElaboration(text.substring(text.indexOf("preparaci") + "preparacion".length() + 1, text.length()));
            }
            else if(text.contains("pasos")) {
                rTmp.setIngredients(text.substring(text.indexOf("ingredientes") + "ingredientes".length()+1, text.indexOf("pasos") - 1));
                rTmp.setElaboration(text.substring(text.indexOf("pasos") + "pasos".length() + 1, text.length()));
            }
            else if(text.contains("indicaciones")) {
                rTmp.setIngredients(text.substring(text.indexOf("ingredientes") + "ingredientes".length()+1, text.indexOf("indicaciones") - 1));
                rTmp.setElaboration(text.substring(text.indexOf("indicaciones") + "indidcaciones".length() + 1, text.length()));
            }
            else{
                rTmp.setIngredients(text.substring(text.indexOf("ingredientes")+"ingredientes".length()+1,text.length()));
            }

        }
        else if(text.contains("description")) {
            rTmp.setElaboration(text.substring(text.indexOf("description") + "description".length()+1, text.length()-1));
        }else if(text.contains("elaboration")) {
            rTmp.setElaboration(text.substring(text.indexOf("elaboration") + "elaboration".length()+1, text.length()));
        }
        else if(text.contains("elaboraci")){
            rTmp.setElaboration(text.substring(text.indexOf("elaboraci") + "elaboracion".length() +1, text.length()));
        }
        else if(text.contains("preparaci")) {
            rTmp.setElaboration(text.substring(text.indexOf("preparaci") + "preparacion".length() + 1, text.length()));
        }
        else if(text.contains("pasos")) {
            rTmp.setElaboration(text.substring(text.indexOf("pasos") + "pasos".length() + 1, text.length()));
        }else if(text.contains("indicaciones")) {
            rTmp.setElaboration(text.substring(text.indexOf("indicaciones") + "indidcaciones".length() + 1, text.length()));
        }

        if(rTmp.getIngredients() == null && rTmp.getElaboration() == null)
            rTmp = null;
        else if(rTmp.getIngredients().equals("") && rTmp.getElaboration().equals(""))
            rTmp = null;

        return rTmp;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public boolean IsPlayServicesAvailable ()
    {
        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS)
        {
            if (GoogleApiAvailability.getInstance().isUserResolvableError (resultCode)) {
                Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(SplashScreen.this, resultCode, GoogleApiAvailability.GOOGLE_PLAY_SERVICES_VERSION_CODE);
                dialog.show();
            }
            else {
                Snackbar.make(title, "Para usar esta app es necesario instalar GooglePlay Services", Snackbar.LENGTH_LONG).show();
            }
            return false;
        }
        else
        {
            return true;
        }
    }
    @Override
    public void showProgress(boolean show) {

    }

    @Override
    public void showLoginError(String s) {
        Intent iTmp = new Intent().setClass(c, LoginActivity.class);
        if(newR!=null) {
            Bundle b = new Bundle();
            b.putParcelable("newR", newR);
            iTmp.putExtras(b);
        }
        startActivity(iTmp);
        finish();
    }

    @Override
    public void showHome(Bundle userInfo) {
        Intent iTmp = new Intent().setClass(c, HomeActivity.class);
        if(newR!=null) {
            userInfo.putParcelable("newR", newR);
        }
        iTmp.putExtras(userInfo);
        startActivity(iTmp);
        finish();
    }

    @Override
    public void updateUI(FirebaseUser currentUser) {
        if(currentUser == null){
            startActivity(new Intent().setClass(c, LoginActivity.class));
            finish();
        }
        else {
            presenter.logInRest(currentUser);
        }

    }
}
