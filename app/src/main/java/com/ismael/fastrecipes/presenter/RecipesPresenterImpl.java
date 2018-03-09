package com.ismael.fastrecipes.presenter;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.ismael.fastrecipes.R;
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
import java.util.concurrent.TimeUnit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * RecipesPresenterImpl.java - Clase que controla todas las conexiones para la obtención, modificación, borrado y añadido de recetas. Local o externo
 * Created by Ismael on 24/01/2018.
 */

public class RecipesPresenterImpl implements RecipesPresenter{
    private Context context;
    private RecipesPresenter.View view;
    private FastRecipesService mService;
    private DatabaseReference mCommentsReference;
    DatabaseReference firebaseDatabaseInstance;
    private StorageReference mStorageRef;



    public RecipesPresenterImpl(RecipesPresenter.View vista, int id){
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
        if(isOnline()) {
            mService.getMyRecipes(idUser).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).timeout(10, TimeUnit.SECONDS)
                    .subscribe(new Subscriber<Result>() {
                        @Override
                        public void onCompleted() {
                            if (recs.size() > 0) {
                                view.setListData(recs);
                            } else {
                                view.cancelSearch();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            view.showNetworkError(context.getResources().getString(R.string.serverError));
                        }

                        @Override
                        public void onNext(Result result) {
                            if (result.getRecipes() != null && result.getCode() && result.getRecipes().size() > 0) {
                                recs.addAll(result.getRecipes());
                            }

                        }

                    });
        }else
            view.showNetworkError(context.getResources().getString(R.string.nointernet));
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
       if(isOnline()){
        mService.getFavsRecipes(idUser).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).timeout(10, TimeUnit.SECONDS)
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
                        view.showNetworkError(context.getResources().getString(R.string.serverError));
                    }

                    @Override
                    public void onNext(Result result) {
                        if (result.getRecipes() != null && result.getCode() && result.getRecipes().size() > 0){
                            recs.addAll(result.getRecipes());
                        }

                    }

                });
       }else
           view.showNetworkError(context.getResources().getString(R.string.nointernet));
    }
    /*
    Métodos para la gestion de recetas filtradas
     */


    @Override
    public void addRecipe(final Recipe addR, final Uri mImageUri) {
        final Recipe[] r = new Recipe[1];
        if(isOnline()) {
            mService.addRecipe(addR).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).timeout(10, TimeUnit.SECONDS)
                    .subscribe(new Subscriber<Result>() {
                        @Override
                        public void onCompleted() {
                            if (r[0].getImage().equals("add") && mImageUri != null)
                                loadImage(r[0], mImageUri);
                            else {
                                Bundle b = new Bundle();
                                b.putParcelable("recipe", r[0]);
                                view.showRecipeInfo(b);
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            view.showNetworkError("Error de conexión con el servidor");
                        }

                        @Override
                        public void onNext(Result result) {
                            r[0] = result.getRecipes().get(0);
                        }

                    });
        }else
            view.showNetworkError(context.getResources().getString(R.string.nointernet));
    }


    @Override
    public void modifyRecipe(final Recipe addR,  final Uri mImageUri) {
        final Recipe[] r = new Recipe[1];
        if(isOnline()) {
            mService.modifyRecipe(addR).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).timeout(10, TimeUnit.SECONDS)
                    .subscribe(new Subscriber<Result>() {
                        @Override
                        public void onCompleted() {
                            Log.d("SUBSCRIBER ADD INFO", "RECIPE ADDED");
                            //view.showSocialFragment();
                            if (r[0].getImage().equals("add") && mImageUri != null)
                                loadImage(r[0], mImageUri);
                            else {
                                Bundle b = new Bundle();
                                b.putParcelable("recipe", r[0]);
                                view.showRecipeInfo(b);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            view.showNetworkError("Error de conexión con el servidor");
                        }

                        @Override
                        public void onNext(Result result) {
                            r[0] = result.getRecipes().get(0);
                        }

                    });
        }
    }




    @Override
    public void getRecipe(int id, int idu) {
        final Recipe[] r = new Recipe[1];
        String idS = String.valueOf(idu)+"-"+String.valueOf(id);
        if(isOnline()) {
            mService.getRecipe(idS).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).timeout(10, TimeUnit.SECONDS)
                    .subscribe(new Subscriber<Result>() {
                        @Override
                        public void onCompleted() {
                            if (r[0] != null) {
                                Bundle b = new Bundle();
                                b.putParcelable("recipe", r[0]);
                                view.showRecipeInfo(b);
                            }
                        }
                        @Override
                        public void onError(Throwable e) {
                            view.showNetworkError(context.getResources().getString(R.string.serverError));
                        }

                        @Override
                        public void onNext(Result result) {
                            if (result.getCode() && result.getRecipes() != null)
                                r[0] = result.getRecipes().get(0);
                        }

                    });
        }else
            view.showNetworkError(context.getResources().getString(R.string.nointernet));
    }





    @Override
    public void setFavourite(int idUser, int idRecipe, int fav) {
        final Recipe[] r = new Recipe[1];

        String setfav = String.valueOf(idUser)+"-"+String.valueOf(idRecipe)+"-"+String.valueOf(fav);
        if(isOnline()) {
            mService.setFavouriteRec(setfav).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).timeout(10, TimeUnit.SECONDS)
                    .subscribe(new Subscriber<Result>() {
                        @Override
                        public void onCompleted() {
                            if(r[0].getFav() == 0)
                                view.showNetworkError(context.getResources().getString(R.string.favdeleted));

                            else
                                view.showNetworkError(context.getResources().getString(R.string.favadded));

                            view.setFavState(r[0]);

                        }

                        @Override
                        public void onError(Throwable e) {
                            view.showNetworkError(context.getResources().getString(R.string.serverError));
                        }

                        @Override
                        public void onNext(Result result) {
                            r[0] = result.getRecipes().get(0);
                        }

                    });
        }else
            view.showNetworkError(context.getResources().getString(R.string.nointernet));
    }



    @Override
    public void getFilteredRecipes(Recipe rModel) {
        final ArrayList<Recipe> recs = new ArrayList<>();
        if(isOnline()) {
            mService.getRecipesFiltered(rModel).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).timeout(10, TimeUnit.SECONDS)
                    .subscribe(new Subscriber<Result>() {
                        @Override
                        public void onCompleted() {
                            if (recs.size() > 0) {
                                view.setListData(recs);
                            } else {
                                view.cancelSearch();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            view.showNetworkError("Error de conexión con el servidor");
                        }

                        @Override
                        public void onNext(Result result) {
                            if (result.getRecipes() != null && result.getCode() && result.getRecipes().size() > 0) {
                                recs.addAll(result.getRecipes());
                            }

                        }

                    });
        }else
            view.showNetworkError(context.getResources().getString(R.string.nointernet));
    }

    @Override
    public void sendComment(Comment cTmp) {

        mCommentsReference = firebaseDatabaseInstance.child(String.valueOf(cTmp.getIdRecipe()));
        mCommentsReference.push().setValue(cTmp).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                view.showNetworkError(context.getResources().getString(R.string.publishedComment));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                view.showNetworkError(context.getResources().getString(R.string.serverError));
            }
        });
    }

    @Override
    public void setRating(int id, int id1, int newRating) {
        final Recipe[] r = new Recipe[1];
        String rating = String.valueOf(id)+"-"+String.valueOf(id1)+"-"+String.valueOf(newRating);
        if(isOnline()){
        mService.setRating(rating).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).timeout(10, TimeUnit.SECONDS)
                .subscribe(new Subscriber<Result>() {
                    boolean cont = false;
                    @Override
                    public void onCompleted() {
                        Bundle b = new Bundle();
                        b.putParcelable("r", r[0]);
                        if(cont) {
                            view.showRecipeInfo(b);
                            view.showNetworkError("Valoración actualizada.");
                        }
                        else
                            view.showNetworkError("Valoración NO actualizada.");
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showNetworkError("Error de conexión con el servidor.");
                    }

                    @Override
                    public void onNext(Result result) {
                        if(result.getCode() && result.getRecipes() != null){
                            r[0] = result.getRecipes().get(0);
                            cont = true;
                        }
                    }

                });
        }else
            view.showNetworkError(context.getResources().getString(R.string.nointernet));
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
        if(isOnline()){
        mService.removeRecipe(idRecipe).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).timeout(10, TimeUnit.SECONDS)
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {
                        if(state[0] == 0){
                            view.cancelSearch();
                        }
                        else if(state[0] == 1){
                            view.showNetworkError("La receta no se ha borrado.");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Result result) {
                        if(result.getCode() && result.getStatus() == 200)
                            state[0] = 0;
                        else{
                            state[0] = 1;
                        }
                    }

                });
        }else
            view.showNetworkError(context.getResources().getString(R.string.nointernet));
    }

    @Override
    public void loadImage(final Recipe rTmp, Uri mImageUri){
        // Intent i = new Intent(Intent.ACTION_PICK, android.provider.)
        mStorageRef = FirebaseStorage.getInstance().getReference(Const.FIREBASE_IMAGE_RECIPE+"/"+String.valueOf(rTmp.getIdr()));
        mStorageRef.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mStorageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        rTmp.setImage(task.getResult().toString());
                        modifyRecipe(rTmp, null);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String msg = "Error al cargar la foto.";
                view.showNetworkError( msg);
            }
        });
    }

    @Override
    public void getRecipeOfDay() {
        final ArrayList<Recipe> r = new ArrayList<>();
        if(isOnline()) {
            mService.getRecipeOfDay().subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).timeout(10, TimeUnit.SECONDS)
                    .subscribe(new Subscriber<Result>() {
                        @Override
                        public void onCompleted() {
                            if (r.get(0) != null) {
                                view.setListData(r);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            view.showNetworkError("Ha ocurrido un problema con la conexión");
                        }

                        @Override
                        public void onNext(Result result) {
                            if (result.getRecipes() != null)
                                r.add(result.getRecipes().get(0));
                        }

                    });
        }
    }

    /**
     * Comprueba la conexión de red del dispositivo
     * @return Devuelve true o false en función de la disponibilidad de la conexión del dispositivo
     */
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
}
