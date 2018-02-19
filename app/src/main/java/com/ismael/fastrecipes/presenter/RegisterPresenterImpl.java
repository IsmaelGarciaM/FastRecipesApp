package com.ismael.fastrecipes.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.ismael.fastrecipes.FastRecipesApplication;
import com.ismael.fastrecipes.interfaces.FastRecipesApi;
import com.ismael.fastrecipes.interfaces.RegisterPresenter;
import com.ismael.fastrecipes.exceptions.DataEntryException;
//import com.ismael.fastrecipes.interfaces.ProFinderApi;
import com.ismael.fastrecipes.model.Errors;
import com.ismael.fastrecipes.model.User;
import com.ismael.fastrecipes.utils.FastRecipesService;
import com.ismael.fastrecipes.utils.ResultUser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

/**
 * RegisterPresenterImpl.java - Presentador que controla los datos y las conexiones de la vista de registro
 * @author Ismael Garcia
 * @see RegisterPresenter
 */

public class RegisterPresenterImpl implements RegisterPresenter {


    RegisterPresenter.View vista;
    Context context;
    FirebaseAuth mAuth;
    private FastRecipesService mService;

    /**
     * Constructor del presentador
     * @param view Vista a la que está ligado
     * @param c contexto de la aplicación
     */
    public RegisterPresenterImpl(RegisterPresenter.View view, Context c){
        this.vista = view;
        this.context = c;
        //Instancia de AUTENTICACION DE FIREBASE
        mAuth = FirebaseAuth.getInstance();
        //Conexión con la api
        mService = FastRecipesApplication.getFastRecipesService();
    }

    /**
     * Comprueba que el email no está vació y llama a otro método para validarlo
     * @param email Email a comprobar
     */
    @Override
    public void validateMail(String email) {
        if(TextUtils.isEmpty(email))
            throw new DataEntryException(Errors.EMPTYFIELD_EXCEPTION, context.getApplicationContext());

        if(!isEmailValid(email)){
            throw new DataEntryException(Errors.INVALIDEMAIL_EXCEPTION, context);
        }
    }

    /**
     * Comprueba la validez y robustez de la contraseña
     * @param password Contraseña a comprobar
     */
    @Override
    public void validatePassword(String password) {
        if (TextUtils.isEmpty(password))
            throw new DataEntryException(Errors.EMPTYFIELD_EXCEPTION, context);

        if (password.length() < 8 || password.length() > 15) {
            throw new DataEntryException(Errors.PASSWORDLENGHT_EXCEPTION, context);
        }

        if (!(password.matches("(.*)\\p{Lower}(.*)") && password.matches("(.*)\\p{Upper}(.*)"))) {
            throw new DataEntryException(Errors.PASSCAPLETTER_EXCEPTION, context);
        }

        if (!(password.matches("(.*)\\d(.*)"))) {
            throw new DataEntryException(Errors.PASSNUMBER_EXCEPTION, context);
        }
    }

    /**
     * Comprueba que el nombre no esté vacío
     * @param name Nombre a comprobar
     */
    @Override
    public void validateData(String name) {
        if(TextUtils.isEmpty(name))
            throw new DataEntryException(Errors.EMPTYFIELD_EXCEPTION, context.getApplicationContext());
    }


    /**
     * Realiza el registro del usuario en firebase, obtiene el token y si se completa con exito se llama al metodo para registrar al usuario el servidor rest
     * @param name Nombre del usuario
     * @param mail Email del usuario
     * @param password Contraseña del usuario
     */
    @Override
    public void registerUser(String name, String mail, String password) {
        final String n = name;
        final String m = mail;

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);

        mAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "createUserWithEmail:success");
                    final FirebaseUser user = mAuth.getCurrentUser();
                    user.sendEmailVerification();
                    user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        @Override
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if(task.isSuccessful()){
                                String idToken = task.getResult().getToken();
                                //Enviar al backend
                                registerUserRest(idToken, m, n, formattedDate, user.getUid());
                        }
                        }
                    });
                    vista.showLogin();
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(context, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    /**
     * Realiza la petición de registro en el servidor rest
     * @param idToken Token de firebase para autenticación
     * @param email Email del nuevo usuario
     * @param name Nombre del nuevo usuario
     * @param regDate Fecha de registro, es decir, la actual
     * @param uid Identificador único del usuario en firebase que sirve complementariamente para la autenticacion en el servidor rest
     */
    @Override
    public void registerUserRest(String idToken, String email, String name, String regDate, String uid) {
        //(http://api.fastrecipes.com/user/registro/idToken) add json encoded name, mail, regDate, uid
        User u = new User(email, name, regDate, uid);
        mService.registerUser(idToken, u).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResultUser>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResultUser result) {
                           // vista.doLogin();

                    }
                });


        /*Call<Boolean> registerCall = mService.registerUser(idToken, u);
        registerCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                if (!response.isSuccessful()) {
                    String error;
                    if (response.errorBody()
                            .contentType()
                            .subtype()
                            .equals("application/json")) {
                        Log.d("LoginActivity", "APIERROR");
                    } else {
                        error = response.message();
                    }

                    return;
                }


            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });*/
    }


    /**
     * Comprobación de validez del email
     * @param email Email a comprobar
     * @return Devuelve true o false en función de la validez del email
     */
    private boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}
