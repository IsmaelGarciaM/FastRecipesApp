package com.ismael.fastrecipes;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.ismael.fastrecipes.model.Recipe;
import com.ismael.fastrecipes.presenter.LoginPresenterImpl;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * LoginActivity.java - Clase que controla el acceso de los usuarios a los servidores
 * @author Ismael Garcia
 */
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

    @BindView(R.id.tilForgetPass)
    TextInputLayout tilForget;

    @BindView(R.id.pgbLoading)
    ProgressBar pgbLoadingAnimation;

    @BindView(R.id.login_data_layout)
    View dataView;
    Bundle newR;
    LoginPresenter presenter;
    View mView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Intent tmp = getIntent();
        if(tmp.getExtras() != null)
        {
            newR = tmp.getExtras();
        }
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
                    saveUser();
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registrarse();
            }
        });

        tilForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mView = view;
                tilMailL.setError(null);
                tilMailL.setErrorEnabled(false);
                String mail = String.valueOf(edtMail.getText());
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
                if(cancel){
                    focusView.requestFocus();
                }
                //El presentador realiza el envío
                else{
                    showConfirmDialog(mail);

                }

            }
        });
        if(getIntent().getBooleanExtra("close", false)) {
            presenter.closeFirebaseSession();
        }
    }

    /**
     * Cambia a la ventana de registro
     */
    void Registrarse(){
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
        finish();
    }


    /**
     * Comprueba que el email y contraseña están bien formadas y de ser así, realiza el login mediante el presentador
     */
    void attemptLogin() {

        //probar conexion de red

        //resetear posibles errores
        tilMailL.setErrorEnabled(false);
        tilPassL.setErrorEnabled(false);

        tilMailL.setError(null);
        tilPassL.setError(null);

        //obtener valores
        String mail = String.valueOf(edtMail.getText());
        String pass = String.valueOf(edtPass.getText());

        //si aparece un error se cancela el login; cancel = true
        boolean cancel = false;
        View focusView = null;

        //El presentador comprueba los datos
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
        //El presentador realiza el login
        else{
            showProgress(true);
            presenter.logIn(mail, pass);
        }
    }

    /**
     * Muestra la animación de carga de la siguiente pantalla
     * @param show Determina si es visible o no
     */
    @Override
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

    /**
     * Muestra un error general de login
     */
    @Override
    public void showLoginError(String msg){
        if(mView != null) {
            Snackbar.make(mView, msg, Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Muestra un error de conexión
     */
    private void showNetworkError(){
        Snackbar.make(mView, "Error de conexión de red.", Snackbar.LENGTH_LONG).show();
    }

    /**
     * Si el login es correcto, el servidor devuelve la informacion del usuario y se accede a la aplicación
     * @param userInfo Información del usuario
     */
    @Override
    public void showHome(Bundle userInfo){
        Intent i = new Intent(this, HomeActivity.class);
        if(newR != null) {
            userInfo.putParcelable("newR", newR);
        }
        i.putExtras(userInfo);
        startActivity(i);
        finish();
    }

    /**
     * Realiza el login en el servidor rest
     * @param currentUser Usuario firebase para login en servidor rest
     */
    @Override
    public void updateUI(FirebaseUser currentUser) {
        if (currentUser != null){
            presenter.logInRest(currentUser);
        }
        else
            Snackbar.make(mView, "El usuario no existe", Snackbar.LENGTH_SHORT).show();
    }


    /**
     * Comprueba la conexión de red del dispositivo
     * @return Devuelve true o false en función de la disponibilidad de la conexión del dispositivo
     */
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }


    /**
     * Muestra un cuadro de diálogo para saber si se debe guardar la sesión activa o no
     */
    void saveUser(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.Theme_Dialog_Translucent);
        dialog.setCancelable(false);
        dialog.setTitle(R.string.titleSaveUser);
        dialog.setMessage("Puedes volver a configurar esto en las preferencias de la app.");
        dialog.setNegativeButton(getResources().getString(R.string.dontrem), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getPreferences(MODE_PRIVATE).edit().putBoolean("remember_user", false).apply();
                String m = String.valueOf(edtMail.getText());
                if(m.endsWith(" ")){
                    m = m.trim();
                }
                getSharedPreferences("fastrecipessp", MODE_PRIVATE).edit().putString("um", m).apply();
                getSharedPreferences("fastrecipessp", MODE_PRIVATE).edit().putString("up", String.valueOf(edtPass.getText())).apply();
                attemptLogin();

            }
        });

        dialog.setPositiveButton(getResources().getString(R.string.rem), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getSharedPreferences("fastrecipessp", MODE_PRIVATE).edit().putBoolean("remember_user", true).apply();
                String m = String.valueOf(edtMail.getText());
                if(m.endsWith(" ")){
                    m = m.trim();
                }
                getSharedPreferences("fastrecipessp", MODE_PRIVATE).edit().putString("um", m).apply();
                getSharedPreferences("fastrecipessp", MODE_PRIVATE).edit().putString("up", String.valueOf(edtPass.getText())).apply();
                attemptLogin();

            }
        }).show();
    }

    /**
     * Muestra un cuadro de diálogo para confirmar el envío de un correo para cambiar la contraseña
     * @param email Dirección a la que se enviará el correo si está registrado como usuario
     */
    private void showConfirmDialog(String email){
        final String[] t = {email};

        AlertDialog.Builder customDialog = new AlertDialog.Builder(this, R.style.Theme_Dialog_Translucent);
        customDialog.setCancelable(false);
        customDialog.setTitle("¿Enviar correo de recuperación a "+email+"?");
        customDialog.setNegativeButton(getResources().getString(R.string.back), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //cancel
            }
        });

        customDialog.setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showProgress(true);
                presenter.forgetPass(t[0]);

            }
        }).show();

    }

}
