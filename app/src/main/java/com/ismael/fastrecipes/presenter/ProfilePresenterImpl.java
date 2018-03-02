package com.ismael.fastrecipes.presenter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ismael.fastrecipes.FastRecipesApplication;
import com.ismael.fastrecipes.exceptions.DataEntryException;
import com.ismael.fastrecipes.interfaces.ProfilePresenter;
import com.ismael.fastrecipes.model.Errors;
import com.ismael.fastrecipes.model.Recipe;
import com.ismael.fastrecipes.model.User;
import com.ismael.fastrecipes.utils.Const;
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
    private StorageReference mStorageRef;


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
                    boolean cont = false;
                    @Override
                    public void onCompleted() {
                        if(cont)
                            view.setUserData(u.get(0));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("SUBSCRIBER FAILED USER", e.getMessage());
                    }

                    @Override
                    public void onNext(ResultUser resultUser) {
                        if(resultUser.getCode() && resultUser.getUsers() != null) {
                            u.addAll(resultUser.getUsers());
                            cont = true;
                        }
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
                        if(u.size()>0)
                            view.setUserListData(u);
                        else
                            view.cancelSearch();
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
                    boolean cont = false;
                    @Override
                    public void onCompleted() {
                        if (cont)
                            view.setUserRecipesData(r);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("SUBSCRIBER FAILED USER", e.getMessage());
                    }

                    @Override
                    public void onNext(Result result) {
                        if(result.getCode() && result.getRecipes() != null && result.getRecipes().size() > 0) {
                            r.addAll(result.getRecipes());
                            cont = true;
                        }

                    }
                });
    }



    @Override
    public void editProfile(User userData, final boolean imageChanged, final Uri mImageUri) {
        final ArrayList<User> u = new ArrayList<>();
        //Observable<Recipe> call = mService.getRecipe(id);
        mService.updateProfile(userData).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResultUser>() {
                    boolean cont = false;
                    @Override
                    public void onCompleted() {
                        if (cont){
                            if(imageChanged)
                                loadImage(u.get(0), mImageUri);
                            else
                                view.setUserData(u.get(0));
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("SUBSCRIBER FAILED USER", e.getMessage());
                    }

                    @Override
                    public void onNext(ResultUser resultUser) {
                        if(resultUser.getUsers() != null && resultUser.getUsers().size()>0) {
                            u.addAll(resultUser.getUsers());
                            cont = true;
                        }
                    }
                });
    }



    String loadImage(final User u, Uri mImageUri){
        // Intent i = new Intent(Intent.ACTION_PICK, android.provider.)
        final String[] newImage = new String[1];
        mStorageRef = FirebaseStorage.getInstance().getReference(Const.FIREBASE_IMAGE_USER+"/"+String.valueOf(u.getId()));
        mStorageRef.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mStorageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        u.setImage(task.getResult().toString());
                        editProfile(u, false, null);
                        view.setUserData(u);
                        view.updateCurrentUser(u);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ERROR FIREASE STORAGE", e.getMessage());
            }
        });

        return "";
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
