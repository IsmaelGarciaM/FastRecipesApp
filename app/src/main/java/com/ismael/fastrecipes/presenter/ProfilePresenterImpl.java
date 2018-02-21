package com.ismael.fastrecipes.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.ismael.fastrecipes.FastRecipesApplication;
import com.ismael.fastrecipes.exceptions.DataEntryException;
import com.ismael.fastrecipes.interfaces.ProfilePresenter;
import com.ismael.fastrecipes.model.Errors;
import com.ismael.fastrecipes.model.Recipe;
import com.ismael.fastrecipes.model.User;
import com.ismael.fastrecipes.utils.FastRecipesService;
import com.ismael.fastrecipes.utils.Result;
import com.ismael.fastrecipes.utils.ResultUser;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Ismael on 17/02/2018.
 */

public class ProfilePresenterImpl implements ProfilePresenter {

    private Context context;
    private ProfilePresenter.View view;
    private FastRecipesService mService;

    public ProfilePresenterImpl(ProfilePresenter.View vista){
        this.view = vista;
        this.context = vista.getContext();
        mService = FastRecipesApplication.getFastRecipesService();
    }
    @Override
    public void getUserByComment(int idComment) {

        final ArrayList<User> u = new ArrayList<>();
        //Observable<Recipe> call = mService.getRecipe(id);
        mService.getUserByComment(idComment).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResultUser>() {
                    @Override
                    public void onCompleted() {
                        view.setUserData(u);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("SUBSCRIBER FAILED USER", e.getMessage());
                    }

                    @Override
                    public void onNext(ResultUser resultUser) {
                        u.addAll(resultUser.getUsers());
                    }
                });
    }

    @Override
    public void getUser(int idUser) {

        final ArrayList<User> u = new ArrayList<>();
        //Observable<Recipe> call = mService.getRecipe(id);
        mService.getUser(idUser).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResultUser>() {
                    @Override
                    public void onCompleted() {
                        view.setUserData(u);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("SUBSCRIBER FAILED USER", e.getMessage());
                    }

                    @Override
                    public void onNext(ResultUser resultUser) {
                        u.addAll(resultUser.getUsers());
                    }
                });
    }
    @Override
    public void getUsersFav(int idRecipe) {
        final ArrayList<User> u = new ArrayList<>();
        //Observable<Recipe> call = mService.getRecipe(id);
        mService.getUsersFav(idRecipe).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResultUser>() {
                    @Override
                    public void onCompleted() {
                        view.setUserData(u);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("SUBSCRIBER FAILED USER", e.getMessage());
                    }

                    @Override
                    public void onNext(ResultUser resultUser) {
                        u.addAll(resultUser.getUsers());
                    }
                });
    }

    @Override
    public void getUserRecipes(int idUser) {
        final ArrayList<Recipe> r = new ArrayList<>();
        mService.getUserRecipes(idUser).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {
                        view.setUserRecipesData(r);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("SUBSCRIBER FAILED USER", e.getMessage());
                    }

                    @Override
                    public void onNext(Result result) {
                        r.addAll(result.getRecipes());
                    }
                });
    }



    @Override
    public void editProfile(User userData) {
        final ArrayList<User> u = new ArrayList<>();
        //Observable<Recipe> call = mService.getRecipe(id);
        mService.updateProfile(userData).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResultUser>() {
                    @Override
                    public void onCompleted() {
                        view.setUserData(u);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("SUBSCRIBER FAILED USER", e.getMessage());
                    }

                    @Override
                    public void onNext(ResultUser resultUser) {
                        u.addAll(resultUser.getUsers());
                    }
                });
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

    /**
     * Comprueba que el nombre no esté vacío
     * @param name Nombre a comprobar
     */
    @Override
    public void validateName(String name) {
        if(TextUtils.isEmpty(name))
            throw new DataEntryException(Errors.EMPTYFIELD_EXCEPTION, context.getApplicationContext());
    }
}
