package com.ismael.fastrecipes.presenter;

import android.content.Context;
import android.util.Log;

import com.ismael.fastrecipes.FastRecipesApplication;
import com.ismael.fastrecipes.interfaces.ProfilePresenter;
import com.ismael.fastrecipes.model.User;
import com.ismael.fastrecipes.utils.FastRecipesService;
import com.ismael.fastrecipes.utils.Result;
import com.ismael.fastrecipes.utils.ResultUser;

import java.util.ArrayList;

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
    public void getUser(int idComment) {

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
}
