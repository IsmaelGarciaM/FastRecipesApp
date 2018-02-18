package com.ismael.fastrecipes;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.ismael.fastrecipes.exceptions.DataEntryException;
import com.ismael.fastrecipes.interfaces.LoginPresenter;
import com.ismael.fastrecipes.presenter.LoginPresenterImpl;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LoginActivity extends Activity implements LoginPresenter.View{

    @BindView(R.id.btnEntry)
    Button btnEntry;

    @BindView(R.id.btnRegister)
    Button btnRegister;

    @BindView(R.id.edtMailLogin)
    EditText edtMail;

    @BindView(R.id.edtPasswordLogin)
    EditText edtPass;

    @BindView(R.id.tilMailLogin)
    TextInputLayout tilMailL;

    @BindView(R.id.tilPasswordLogin)
    TextInputLayout tilPassL;

    @BindView(R.id.pgbLoading)
    ProgressBar pgbLoadingAnimation;

    @BindView(R.id.login_data_layout)
    View dataView;

    LoginPresenter presenter;
    View mView = null;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    //private UserLoginTask mAuthTask = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        presenter = new LoginPresenterImpl(this, getApplicationContext());

        edtPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(!isOnline()){
                    showNetworkError();
                    return false;
                }
                else {
                    if (actionId == R.id.activity_login || actionId == EditorInfo.IME_NULL) {
                        attemptLogin();
                        return true;
                    }
                    return false;
                }
            }
        });

        btnEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mView = v;
                if(!isOnline()){
                    showNetworkError();

                }else {
                    attemptLogin();
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registrarse();
            }
        });

    }

    void Registrarse(){
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
        finish();
    }

    void attemptLogin() {

        //try network, always try network conection

        //reset posible errors
        tilMailL.setErrorEnabled(false);
        tilPassL.setErrorEnabled(false);

        tilMailL.setError(null);
        tilPassL.setError(null);


        //get values
        String mail = String.valueOf(edtMail.getText());
        String pass = String.valueOf(edtPass.getText());

        //if an error appear, then login attempt is cancelled; cancel = true
        boolean cancel = false;
        View focusView = null;

        try {
            presenter.validateMail(mail);
        } catch (DataEntryException exc) {
            tilMailL.setErrorEnabled(true);
            tilMailL.setError(exc.getMessage());
            focusView = tilMailL;
            cancel = true;
        }

        try {
            presenter.validatePassword(pass);
        } catch (DataEntryException exc) {
            tilPassL.setErrorEnabled(true);
            tilPassL.setError(exc.getMessage());
            focusView = tilPassL;
            cancel = true;
        }

        if(cancel){
            focusView.requestFocus();
        }
        else{
            showProgress(true);
            presenter.logIn(mail, pass);
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        dataView.setVisibility(show ? View.GONE : View.VISIBLE);
        dataView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                dataView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        pgbLoadingAnimation.setVisibility(show ? View.VISIBLE : View.GONE);
        pgbLoadingAnimation.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                pgbLoadingAnimation.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void showLoginError(){
        Snackbar.make(mView, "Incorrect user or e-mail", Snackbar.LENGTH_LONG).show();
    }

    private void showNetworkError(){
        Snackbar.make(mView, "Errors de conexión con el servidor", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showHome(Bundle userInfo){
        Intent i = new Intent(this, HomeActivity.class);
//        i.putExtras(userInfo);
        startActivity(i);
        finish();
    }

    @Override
    public void updateUI(FirebaseUser currentUser) {
        if (currentUser != null){
            presenter.logInRest(currentUser);
        }
        else
            Snackbar.make(mView, "El usuario no existe", Snackbar.LENGTH_SHORT).show();
    }


    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
}
