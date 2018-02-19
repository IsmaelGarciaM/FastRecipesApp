package com.ismael.fastrecipes;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ismael.fastrecipes.exceptions.DataEntryException;
import com.ismael.fastrecipes.interfaces.RegisterPresenter;
import com.ismael.fastrecipes.presenter.RegisterPresenterImpl;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * RegisterActivity.class - Clase contenedora de la vista del formulario de registro para un usuario
 */
public class RegisterActivity extends AppCompatActivity implements RegisterPresenter.View{

    @BindView(R.id.tilNameRegister)
    TextInputLayout tilNameRegister;
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
                attemptRegister();
            }
        });
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
        }
    }


    @Override
    public void showInputError() {

    }

    /**
     *
     */
    @Override
    public void showLogin() {
        startActivity(new Intent().setClass(this, LoginActivity.class).putExtra("registrado", true));
        finish();
    }
}
