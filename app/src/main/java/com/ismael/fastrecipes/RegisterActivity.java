package com.ismael.fastrecipes;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ismael.fastrecipes.exceptions.DataEntryException;
import com.ismael.fastrecipes.interfaces.RegisterPresenter;
import com.ismael.fastrecipes.presenter.RegisterPresenterImpl;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ismael.fastrecipes.FastRecipesApplication.getContext;

/**
 * RegisterActivity.class - Clase contenedora de la vista del formulario de registro para un usuario
 * @author Ismael Garcia
 */
public class RegisterActivity extends AppCompatActivity implements RegisterPresenter.View{

    @BindView(R.id.tilNameRegister)
    TextInputLayout tilNameRegister;
    @BindView(R.id.regTit)
    TextView txvRegTit;
    @BindView(R.id.edtNameRegister)
    TextInputEditText edtNameRegister;
    @BindView(R.id.tilEmailRegister)
    TextInputLayout tilEmailRegister;
    @BindView(R.id.edtEmailRegister)
    TextInputEditText edtEmailRegister;
    @BindView(R.id.tilPasswordRegister)
    TextInputLayout tilPasswordRegister;
    @BindView(R.id.edtPasswordRegister)
    TextInputEditText edtPasswordRegister;
    @BindView(R.id.btnAceptRegister)
    Button btnOkRegister;
    @BindView(R.id.btnSeePass)
    Button btnSeePass;
    @BindView(R.id.pgbLoadingReg)
    ProgressBar pgbLoadingReg;
    RegisterPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        presenter = new RegisterPresenterImpl(this, getApplicationContext());
        btnOkRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser();
                }
        });
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "yummycupcakes.ttf");
        txvRegTit.setTypeface(font);
        btnSeePass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch ( motionEvent.getAction() ) {
                    case MotionEvent.ACTION_UP:
                        edtPasswordRegister.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        edtPasswordRegister.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                }
                return true;
            }

        });

    }


    /**
     * Devuelve a la pantalla de login
     */
    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    /**
     * Comprueba los campos introducidos y realiza el registro mediante el presentador
     */
    void attemptRegister() {

        //Comprobar conexión de red

        //resetear posibles errores
        tilNameRegister.setErrorEnabled(false);
        tilEmailRegister.setErrorEnabled(false);
        tilPasswordRegister.setErrorEnabled(false);

        tilNameRegister.setError(null);
        tilEmailRegister.setError(null);
        tilPasswordRegister.setError(null);
        //obtener valores
        String name = String.valueOf(edtNameRegister.getText());
        String mail = String.valueOf(edtEmailRegister.getText());
        if(mail.endsWith(" ")){
            mail = mail.trim();
        }
        String pass = String.valueOf(edtPasswordRegister.getText());

        boolean cancel = false;
        View focusView = null;

        //Comprobar datos
        try {
            presenter.validateMail(mail);
        } catch (DataEntryException exc) {
            tilEmailRegister.setErrorEnabled(true);
            tilEmailRegister.setError(exc.getMessage());
            focusView = tilEmailRegister;
            cancel = true;
        }

        try {
            presenter.validatePassword(pass);
        } catch (DataEntryException exc) {
            tilPasswordRegister.setErrorEnabled(true);
            tilPasswordRegister.setError(exc.getMessage());
            focusView = tilPasswordRegister;
            cancel = true;
        }

        try {
            presenter.validateData(name);
        } catch (DataEntryException exc) {
            tilNameRegister.setErrorEnabled(true);
            tilNameRegister.setError(exc.getMessage());
            focusView = tilNameRegister;
            cancel = true;
        }

        if(cancel){
            //cancelar registro
            focusView.requestFocus();
        }
        else{
            //proceder con el registro
            presenter.registerUser(name, mail, pass);
            showProgress(true);
        }
    }


    @Override
    public void showInputError(String msg) {
        showProgress(false);
        Snackbar.make(btnOkRegister, msg, Snackbar.LENGTH_LONG).show();
    }

    /**
     *
     */
    @Override
    public void showLogin() {
        startActivity(new Intent().setClass(this, SplashScreen.class));
        finish();
    }

    void saveUser(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.Theme_AppCompat_DayNight_Dialog);
        dialog.setCancelable(false);
        dialog.setTitle(R.string.titleSaveUser);
        dialog.setMessage("Puedes volver a configurar esto en las preferencias.");
        dialog.setNegativeButton(getResources().getString(R.string.dontrem), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getPreferences(MODE_PRIVATE).edit().putBoolean("remember_user", false).apply();
                String m = String.valueOf(edtEmailRegister.getText());
                if(m.endsWith(" ")){
                    m = m.trim();
                }
                getSharedPreferences("fastrecipessp", MODE_PRIVATE).edit().putString("um", m).apply();
                getSharedPreferences("fastrecipessp", MODE_PRIVATE).edit().putString("up", String.valueOf(edtPasswordRegister.getText())).apply();
                attemptRegister();

            }
        });

        dialog.setPositiveButton(getResources().getString(R.string.rem), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getSharedPreferences("fastrecipessp", MODE_PRIVATE).edit().putBoolean("remember_user", true).apply();
                String m = String.valueOf(edtEmailRegister.getText());
                if(m.endsWith(" ")){
                    m = m.trim();
                }

                getSharedPreferences("fastrecipessp", MODE_PRIVATE).edit().putString("um",m).apply();
                getSharedPreferences("fastrecipessp", MODE_PRIVATE).edit().putString("up", String.valueOf(edtPasswordRegister.getText())).apply();
                attemptRegister();

            }
        }).show();
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

        pgbLoadingReg.setVisibility(show ? View.VISIBLE : View.GONE);
        pgbLoadingReg.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                pgbLoadingReg.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

}
