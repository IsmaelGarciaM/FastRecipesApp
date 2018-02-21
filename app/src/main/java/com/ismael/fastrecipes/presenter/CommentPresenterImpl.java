package com.ismael.fastrecipes.presenter;

import android.content.Context;
import android.util.Log;

import com.ismael.fastrecipes.FastRecipesApplication;
import com.ismael.fastrecipes.interfaces.CommentPresenter;
import com.ismael.fastrecipes.model.Comment;
import com.ismael.fastrecipes.model.Recipe;
import com.ismael.fastrecipes.utils.FastRecipesService;
import com.ismael.fastrecipes.utils.Result;
import com.ismael.fastrecipes.utils.ResultComment;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Ismael on 23/01/2018.
 */

public class CommentPresenterImpl implements CommentPresenter {

    CommentPresenter.View view;
    Context context;
    private FastRecipesService mService;


    public CommentPresenterImpl(CommentPresenter.View vista){
        this.view = vista;
        this.context = vista.getContext();
        mService = FastRecipesApplication.getFastRecipesService();

    }

    @Override
    public void deleteComment(int idComment) {
        final int[] state = new int[1];
        mService.removeComment(idComment).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {
                        if(state[0] == 0){
                            //Cambiar vista a eliminado
                        }
                        else if(state[0] == 1){
                            Log.d("SUBSCRIBER DELETE", "EL COMENTARIO NO SE HA BORRADO.");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("SUBSCRIBER FAILED", e.getMessage());
                    }

                    @Override
                    public void onNext(Result result) {
                        if(result.getCode() == true && result.getStatus() == 200)
                            state[0] = 0;
                        else{
                            state[0] = 1;
                        }
                    }

                });
    }

    @Override
    public void showRecipeComments(int idRecipe) {
        final Comment[] r = new Comment[1];
        //Observable<Recipe> call = mService.getRecipe(id);
       /* mService.getComments(idRecipe).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResultComment>() {
                    @Override
                    public void onCompleted() {
                        view.setCommentsData(r[0]);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("SUBSCRIBER FAILED", e.getMessage());
                    }

                    @Override
                    public void onNext(ResultComment result) {
                        r[0] = result.getComments().get(0);
                    }

                });*/
    }

    @Override
    public void showMyComments(int idUser) {
        final ArrayList<Comment> c = new ArrayList<>();

        mService.getUserComments(idUser).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResultComment>() {
                    @Override
                    public void onCompleted() {
                        view.setCommentsData(c);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("SUBSCRIBER FAILED", e.getMessage());
                    }

                    @Override
                    public void onNext(ResultComment result) {

                        if(result.getComments().size() > 0){
                            c.addAll(result.getComments());
                        }
                    }

                });
    }
}
