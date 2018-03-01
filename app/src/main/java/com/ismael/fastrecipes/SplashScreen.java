package com.ismael.fastrecipes;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.ismael.fastrecipes.interfaces.LoginPresenter;
import com.ismael.fastrecipes.model.User;
import com.ismael.fastrecipes.presenter.LoginPresenterImpl;
import com.ismael.fastrecipes.prefs.SessionPrefs;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity implements LoginPresenter.View{

    //Duración de splash screen
    private static final long SPLASH_SCREEN_DELAY = 2000;
    LoginPresenterImpl presenter;
    Context c;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_splash_screen);
        presenter = new LoginPresenterImpl(this, this.getApplicationContext());
        c = this;
        Typeface font = Typeface.createFromAsset(getAssets(), "yummycupcakes.ttf");
        TextView title = (TextView) findViewById(R.id.txvInit);
        title.setTypeface(font);


       TimerTask task = new TimerTask() {
            @Override
            public void run() {

                // Envío a HomeActivity si el usuario está guardado y si no a LogInActivity


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

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void showProgress(boolean show) {

    }

    @Override
    public void showLoginError(String s) {
        startActivity(new Intent().setClass(c, LoginActivity.class));
        finish();
    }

    @Override
    public void showHome(Bundle userInfo) {
        startActivity(new Intent().setClass(c, HomeActivity.class).putExtras(userInfo));
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
