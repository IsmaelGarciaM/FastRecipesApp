package com.ismael.fastrecipes.presenter;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ismael.fastrecipes.FastRecipesApplication;
import com.ismael.fastrecipes.db.DatabaseContract;
import com.ismael.fastrecipes.exceptions.DataEntryException;
import com.ismael.fastrecipes.interfaces.RecipesPresenter;
import com.ismael.fastrecipes.model.Comment;
import com.ismael.fastrecipes.model.Errors;
import com.ismael.fastrecipes.model.Recipe;
import com.ismael.fastrecipes.provider.FastRecipesContract;
import com.ismael.fastrecipes.utils.Const;
import com.ismael.fastrecipes.utils.FastRecipesService;
import com.ismael.fastrecipes.utils.Result;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ismael.fastrecipes.provider.FastRecipesProvider.FAVRECIPES;
import static com.ismael.fastrecipes.provider.FastRecipesProvider.FAVRECIPE_ID;
import static com.ismael.fastrecipes.provider.FastRecipesProvider.RECIPE;
import static com.ismael.fastrecipes.provider.FastRecipesProvider.RECIPE_ID;

/**
 * RecipesPresenterImpl.java - Clase que controla todas las conexiones para la obtención, modificación, borrado y añadido de recetas. Local o externo
 * Created by Ismael on 24/01/2018.
 */

public class RecipesPresenterImpl implements RecipesPresenter{
    private Context context;
    private RecipesPresenter.View view;
    private FastRecipesService mService;
    Result r = new Result();
    private DatabaseReference mCommentsReference;
    DatabaseReference firebaseDatabaseInstance;
    GenericTypeIndicator<List<Comment>> t ;
    private StorageReference mStorageRef;



    public RecipesPresenterImpl(RecipesPresenter.View vista, int id){
        t = new GenericTypeIndicator<List<Comment>>() {};
        this.view = vista;
        this.context = vista.getContext();
        mService = FastRecipesApplication.getFastRecipesService();
        firebaseDatabaseInstance = FirebaseDatabase.getInstance().getReference(Const.FIREBASE_CHILD_COMMENTS).child(String.valueOf(id));
        firebaseDatabaseInstance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Comment> tmp = new ArrayList<>();

                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    for(DataSnapshot d : data.getChildren()) {
                        tmp.add(d.getValue(Comment.class));

                    }
                }
                view.addNewComment(tmp);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    /*
        Métodos para la gestión de 'MyRecipes'
     */

    /**
     * Obtiene el listado de recetas propias
     */
    @Override
    public void getMyRecipesList(int idUser) {
        final ArrayList<Recipe> recs = new ArrayList<>();
        //Observable<Recipe> call = mService.getRecipe(id);
        mService.getMyRecipes(idUser).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {
                        if (recs.size()>0) {
                            view.setListData(recs);
                        }
                        else{
                            view.cancelSearch();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d("SUBSCRIBER FAILED",e.getMessage());
                    }

                    @Override
                    public void onNext(Result result) {
                        if (result.getRecipes() != null && result.getCode() && result.getRecipes().size() > 0){
                            recs.addAll(result.getRecipes());
                        }

                    }

                });
    }

     /*
        Métodos para la gestión de 'FavRecipes'
     */

    /**
     * Obtiene el listado de recetas FAVORITAS
     */
    @Override
    public void getFavRecipes(int idUser){
       final ArrayList<Recipe> recs = new ArrayList<>();
        mService.getFavsRecipes(idUser).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {
                        if (recs.size()>0) {
                            view.setListData(recs);
                        }
                        else{
                            view.cancelSearch();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d("SUBSCRIBER FAILED",e.getMessage());
                    }

                    @Override
                    public void onNext(Result result) {
                        if (result.getRecipes() != null && result.getCode() && result.getRecipes().size() > 0){
                            recs.addAll(result.getRecipes());
                        }

                    }

                });
    }
    /*
    Métodos para la gestion de recetas filtradas
     */


    @Override
    public void addRecipe(final Recipe addR, final Uri mImageUri) {
        final Recipe[] r = new Recipe[1];
        mService.addRecipe(addR).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {
                        Log.d("SUBSCRIBER ADD INFO", "RECIPE ADDED");
                        //view.showSocialFragment();
                        if(r[0].getImage().startsWith("gs"))
                            loadImage(r[0].getIdr(), mImageUri);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("SUBSCRIBER FAILED", e.getMessage());
                    }

                    @Override
                    public void onNext(Result result) {
                        r[0] = result.getRecipes().get(0);
                    }

                });
    }


    @Override
    public void modifyRecipe(final Recipe addR,  final Uri mImageUri) {
        final Recipe[] r = new Recipe[1];
        mService.modifyRecipe(addR).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {
                        Log.d("SUBSCRIBER ADD INFO", "RECIPE ADDED");
                        //view.showSocialFragment();
                        if(r[0].getImage().startsWith("gs") && mImageUri != null)
                            loadImage(r[0].getIdr(), mImageUri);

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.d("SUBSCRIBER FAILED", e.getMessage());
                    }

                    @Override
                    public void onNext(Result result) {
                        r[0] = result.getRecipes().get(0);
                    }

                });
    }




    @Override
    public void getRecipe(int id, int idu) {

        final Recipe[] r = new Recipe[1];
        String idS = String.valueOf(idu)+"-"+String.valueOf(id);
       // final ArrayList<Comment> c = new ArrayList<>();
        //Observable<Recipe> call = mService.getRecipe(id);
        mService.getRecipe(idS).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {
                        if(r[0] != null) {
                            Bundle b = new Bundle();
                            b.putParcelable("recipe", r[0]);
                            view.showRecipeInfo(b);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("SUBSCRIBER FAILED", e.getMessage());
                    }

                    @Override
                    public void onNext(Result result) {
                        if(result.getRecipes() != null)
                            r[0] = result.getRecipes().get(0);
                    }

                });
    }





    @Override
    public void setFavourite(int idUser, int idRecipe, int fav) {
        final Recipe[] r = new Recipe[1];
        String setfav = String.valueOf(idUser)+"-"+String.valueOf(idRecipe)+"-"+String.valueOf(fav);
        mService.setFavouriteRec(setfav).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {
                       view.setFavState(r[0]);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("SUBSCRIBER FAILED", e.getMessage());
                    }

                    @Override
                    public void onNext(Result result) {
                        r[0] = result.getRecipes().get(0);
                    }

                });
    }

    @Override
    public void getFilteredRecipes(Recipe rModel) {
        final ArrayList<Recipe> recs = new ArrayList<>();
        //Observable<Recipe> call = mService.getRecipe(id);
        mService.getRecipesFiltered(rModel).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {
                        if (recs.size()>0) {
                            view.setListData(recs);
                        }
                        else{
                            view.cancelSearch();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d("SUBSCRIBER FAILED",e.getMessage());
                    }

                    @Override
                    public void onNext(Result result) {
                        if (result.getRecipes() != null && result.getCode() && result.getRecipes().size() > 0){
                            recs.addAll(result.getRecipes());
                        }

                    }

                });
    }

    @Override
    public void sendComment(String comment, int id, int id1, String name) {


        Comment com = new Comment(id1, name, id, comment, "");
        mCommentsReference = firebaseDatabaseInstance.child(String.valueOf(id));
        mCommentsReference.push().setValue(com);
    }

    @Override
    public void setRating(int id, int id1, int newRating) {
        final Recipe[] r = new Recipe[1];
        //final ArrayList<Comment> c = new ArrayList<>();
        String rating = String.valueOf(id)+"-"+String.valueOf(id1)+"-"+String.valueOf(newRating);
        mService.setRating(rating).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result>() {
                    boolean cont = false;
                    @Override
                    public void onCompleted() {
                        Bundle b = new Bundle();
                        b.putParcelable("r", r[0]);
                        if(cont) {
                            view.showRecipeInfo(b);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        if(e.getCause().equals("connect timed out")){
                           // view.showError();
                        }
                        Log.d("SUBSCRIBER FAILED", e.getMessage());
                    }

                    @Override
                    public void onNext(Result result) {
                        if(result.getCode() && result.getRecipes() != null){
                            r[0] = result.getRecipes().get(0);
                            cont = true;
                        }
                    }

                });
    }

    @Override
    public void validateField(String field) {
        if(field.equals("Tiempo")) {
            throw  new DataEntryException(Errors.EMPTYTIME_EXCEPTION, context);
        }
        if(field.equals("Dificultad"))
            throw  new DataEntryException(Errors.EMPTYDIF_EXCEPTION, context);

        if(field.equals("") && field.length() < 3)
            throw  new DataEntryException(Errors.EMPTYING_EXCEPTION, context);
    }

    @Override
    public void validateName(String name) {
        if(name.equals(""))
            throw  new DataEntryException(Errors.EMPTYNAME_EXCEPTION, context);
    }

    @Override
    public void validateEla(String ela) {
        if(ela.equals(""))
            throw  new DataEntryException(Errors.EMPTYELABORATION_EXCEPTION, context);
    }

    @Override
    public void deleteRecipe(final int idRecipe) {
        final int[] state = new int[1];
        //Observable<Recipe> call = mService.getRecipe(id);
        mService.removeRecipe(idRecipe).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {
                        if(state[0] == 0){
                            Log.d("BORRADA", "HAZ ALGO EN LA UI COÑO");
                        }
                        else if(state[0] == 1){
                            Log.d("SUBSCRIBER DELETE", "LA RECETA NO SE HA BORRADO.");
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
    public void loadImage(int idRecipe, Uri mImageUri){
        // Intent i = new Intent(Intent.ACTION_PICK, android.provider.)
        mStorageRef = FirebaseStorage.getInstance().getReference(Const.FIREBASE_IMAGE_RECIPE+"/"+String.valueOf(idRecipe));
        mStorageRef.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getDownloadUrl();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ERROR FIREASE STORAGE", e.getMessage());
            }
        });
    }

}
