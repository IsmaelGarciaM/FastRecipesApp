package com.ismael.fastrecipes.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.ismael.fastrecipes.FastRecipesApplication;
import com.ismael.fastrecipes.R;
import com.ismael.fastrecipes.interfaces.FastRecipesApi;
import com.ismael.fastrecipes.interfaces.LoginPresenter;
import com.ismael.fastrecipes.exceptions.DataEntryException;
import com.ismael.fastrecipes.model.Errors;
import com.ismael.fastrecipes.model.User;
import com.ismael.fastrecipes.utils.FastRecipesService;
import com.ismael.fastrecipes.utils.ResultUser;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

/**
 * Presentador para controlar los datos y las conexiones de la vista de LoginActivity. Conectados mediante la interfez LoginPresenter
 * @author Ismael Garcia
 * @see LoginPresenter
 */

public class LoginPresenterImpl implements LoginPresenter {

    LoginPresenter.View vista;
    Context context;
    private FirebaseAuth mAuth;
    private FastRecipesService mService;

    /**
     * Constructor del presentador
     * @param view Vista a la que controla
     * @param c Contexto de la aplicación
     */
    public LoginPresenterImpl(LoginPresenter.View view, Context c){
        this.vista = view;
        this.context = c;
        mAuth = FirebaseAuth.getInstance();
        mService = FastRecipesApplication.getFastRecipesService();

    }

    /**
     * Realiza la comprobación de que el email no es nulo y llama a otra función para comprobar su validez
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
     * Realiza la comprobacón de contraseña llamando. 8 o mas caracteres, mayusculas y minusculas y algún símbolo
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
/*
    @Override
    public void logIn() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        vista.updateUI(currentUser);
    }
*/

    /**
     * Realiza la coneión y logueo en el servidor de Firebase
     * @param email Email de usuario
     * @param pass Contraseña de usuario
     */
    @Override
    public void logIn(String email, String pass) {

        if(email.equals("") && pass.equals("")) {
                if (context.getSharedPreferences("fastrecipessp", Context.MODE_PRIVATE).getBoolean("remember_user", false)) {
                    /*

                    email = context.getSharedPreferences("fastrecipessp", Context.MODE_PRIVATE).getString("um", "");
                    pass = context.getSharedPreferences("fastrecipessp", Context.MODE_PRIVATE).getString("up", "");
                    */
                    if(mAuth.getCurrentUser() != null){
                        mAuth.getCurrentUser().getIdToken(false).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                             @Override
                             public void onComplete(@NonNull Task<GetTokenResult> task) {
                                 userLogin(task.getResult().getToken());
                             }
                        }).addOnFailureListener(new OnFailureListener() {
                             @Override
                             public void onFailure(@NonNull Exception e) {
                                 vista.showLoginError("");
                             }
                        });



                    }
                    else {
                        /*mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    //if(user.isEmailVerified())
                                    vista.updateUI(user);

                                    //else
                                    //vista.updateUI(null);
                                } else {
                                    Log.d(TAG, "signInWithEmail:failure", task.getException());
                                    vista.showProgress(false);
                                    vista.showLoginError(context.getResources().getString(R.string.login_error));
                                }
                            }
                        });*/
                        vista.showLoginError("");

                    }
                } else {
                    vista.showLoginError("");
                }
        }
        else{
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        //if(user.isEmailVerified())
                        vista.updateUI(user);

                        //else
                        //vista.updateUI(null);
                    } else {
                        Log.d(TAG, "signInWithEmail:failure", task.getException());
                        vista.showProgress(false);
                        vista.showLoginError(context.getResources().getString(R.string.login_error));
                    }
                }
            });
        }
    }


    /**
     * Obtiene el token de Firebase del usuario para la autenticación el el servidor rest
     * @param currentUser Usuario de Firebase una vez logueado
     */
    public void logInRest(final FirebaseUser currentUser) {
        currentUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if(task.isSuccessful()){
                    String idToken = task.getResult().getToken();
                    //Enviar al backend
                    Log.d(TAG, idToken );
                    userLogin(idToken);
                }
            }
        });
    }

    /**
     * Realiza la conexión y login con el servidor rest
     * @param token Token de firebase para autenticación
     */
    private void userLogin(String token){
        final Bundle b = new Bundle();
        Log.d("token", token);

        mService.getUser(token).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResultUser>() {
                    Boolean cont = false;
                    @Override
                    public void onCompleted() {
                        if(cont) {
                            vista.showProgress(false);
                            vista.showHome(b);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        vista.showProgress(false);
                        Log.d("SUBSCRIBER FAILED", e.getMessage());
                        vista.showLoginError(e.getMessage());
                    }

                    @Override
                    public void onNext(ResultUser user) {
                        if(user.getCode()) {
                            b.putParcelable("user", user.getUsers().get(0));
                            cont = true;
                        } else{
                            vista.showProgress(false);
                            vista.showLoginError(user.getMessage());
                            Log.d("SUBSCRIBER FAILED NEXT", user.getMessage());

                        }
                    }
                });
    }

    /**
     * Envía un correo para el cambio de contraseña
     * @param email
     */
    @Override
    public void forgetPass(String email) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                String msg;
                if (task.isSuccessful()) {
                    msg = "Email enviado.";
                }
                else {
                    msg = "Email incorrecto.";
                }
                vista.showLoginError(msg);

            }
        });
    }

    @Override
    public void closeFirebaseSession() {
        if(mAuth.getCurrentUser() != null){
            mAuth.signOut();
        }
    }


    /**
     * Comrpueba que el email es válido
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

   /* Callback<User> calb = new Callback<User>() {
        @Override
        public void onResponse(Call<User> call, Response<User> response) {
            vista.showProgress(false);

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

                vista.showLoginError();
                return;
            }

            // Guardar cliente en preferencias
            //SessionPrefs.get(FastRecipesApplication.getContext()).saveClient(response.body());
            Bundle b = new Bundle();
            b.putParcelable("user", response.body());
            vista.showHome(b);
        }

        @Override
        public void onFailure(Call<User> call, Throwable t) {
            vista.showProgress(false);
            vista.showLoginError();
        }
    };*/


}
