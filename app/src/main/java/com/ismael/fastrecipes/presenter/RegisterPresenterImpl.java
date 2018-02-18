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
import com.ismael.fastrecipes.interfaces.FastRecipesApi;
import com.ismael.fastrecipes.interfaces.RegisterPresenter;
import com.ismael.fastrecipes.exceptions.DataEntryException;
//import com.ismael.fastrecipes.interfaces.ProFinderApi;
import com.ismael.fastrecipes.model.Errors;
import com.ismael.fastrecipes.model.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

//import retrofit2.Retrofit;

/**
 * Created by ismael on 30/05/17.
 */

public class RegisterPresenterImpl implements RegisterPresenter {


    RegisterPresenter.View vista;
    Context context;
    FirebaseAuth mAuth;
    private Retrofit mRestAdapter;
    private FastRecipesApi fastRecipesApi;


    public RegisterPresenterImpl(RegisterPresenter.View view, Context c){
        this.vista = view;
        this.context = c;
        mAuth = FirebaseAuth.getInstance();
        //Conexión con servicio rest
        mRestAdapter = new Retrofit.Builder().baseUrl(FastRecipesApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        //Conexión con la api
        fastRecipesApi = mRestAdapter.create(FastRecipesApi.class);
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
    public void validateData(String name) {
        if(TextUtils.isEmpty(name))
            throw new DataEntryException(Errors.EMPTYFIELD_EXCEPTION, context.getApplicationContext());
    }


    @Override
    public void registerUser(String name, String mail, String password) {
        final String n = name;
        final String dateStr = "04/05/2010";
        final String m = mail;
        /*SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
        Date dateObj = null;
        try {
            dateObj = curFormater.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat postFormater = new SimpleDateFormat("MMMM dd, yyyy");

        String newDateStr = postFormater.format(dateObj);*/
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
                                registerUserRest(idToken, m, n, dateStr, user.getUid());
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

    @Override
    public void registerUserRest(String idToken, String email, String name, String regDate, String uid) {
        //(http://api.fastrecipes.com/user/registro/idToken) add json encoded name, mail, regDate, uid
        User u = new User(email, name, regDate, uid );
        Call<Boolean> registerCall = fastRecipesApi.register(idToken, u);
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
    }/**/
}
