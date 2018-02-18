package com.ismael.fastrecipes.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.ismael.fastrecipes.FastRecipesApplication;
import com.ismael.fastrecipes.interfaces.FastRecipesApi;
import com.ismael.fastrecipes.interfaces.LoginPresenter;
import com.ismael.fastrecipes.exceptions.DataEntryException;
import com.ismael.fastrecipes.model.Errors;
import com.ismael.fastrecipes.model.User;
import com.ismael.fastrecipes.utils.FastRecipesService;
import com.ismael.fastrecipes.utils.ResultUser;

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
/*
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ismael on 3/05/17.
 */

public class LoginPresenterImpl implements LoginPresenter {

    LoginPresenter.View vista;
    Context context;
    private FirebaseAuth mAuth;

    private FastRecipesService mService;

    public LoginPresenterImpl(LoginPresenter.View view, Context c){
        this.vista = view;
        this.context = c;
        mAuth = FirebaseAuth.getInstance();
        mService = FastRecipesApplication.getFastRecipesService();

    }

    @Override
    public void validateMail(String email) {
        if(TextUtils.isEmpty(email))
            throw new DataEntryException(Errors.EMPTYFIELD_EXCEPTION, context.getApplicationContext());

        if(!isEmailValid(email)){
            throw new DataEntryException(Errors.INVALIDEMAIL_EXCEPTION, context);
        }
    }

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

    @Override
    public void logIn() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        vista.updateUI(currentUser);
    }

    @Override
    public void logIn(String email, String pass) {

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
                    vista.showLoginError();
                }
            }
        });

    }

    public void logInRest(final FirebaseUser currentUser) {
        currentUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if(task.isSuccessful()){
                    String idToken = task.getResult().getToken();
                    //Enviar al backend
                    //(http://api.fastrecipes.com/v1/user/login/idToken) en el token ya va el uid
                    //userLogin(idToken);
                    vista.showHome(null);

                }
            }
        });
    }


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

    private void userLogin(String token){
        final Bundle b = new Bundle();

        mService.getUser(token).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResultUser>() {
                    @Override
                    public void onCompleted() {
                        vista.showProgress(false);
                        vista.showHome(b);
                    }

                    @Override
                    public void onError(Throwable e) {
                        vista.showProgress(false);
                        vista.showLoginError();
                    }

                    @Override
                    public void onNext(ResultUser user) {
                        b.putParcelable("user", user.getUsers().get(0));

                    }
                });
    }
}
