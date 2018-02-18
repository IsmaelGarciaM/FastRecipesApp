package com.ismael.fastrecipes.utils;

import android.util.Log;

import com.ismael.fastrecipes.interfaces.FastRecipesApi;
import com.ismael.fastrecipes.model.Recipe;
import com.ismael.fastrecipes.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Ismael on 12/02/2018.
 */

public class FastRecipesService {
    private FastRecipesApi fastRecipesApi;

    public FastRecipesService() {
        //Conexión con servicio rest
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FastRecipesApi.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Conexión con la api
        fastRecipesApi = retrofit.create(FastRecipesApi.class);
    }

    public FastRecipesApi getAPI() {
        return fastRecipesApi;
    }

    public Observable<ResultUser> getUser(String token) {
        return fastRecipesApi.userLoginObservable(token);//WeatherAPI.API_KEY);
    }

    public Observable<ResultUser> getUserByComment(int idComment) {
        return fastRecipesApi.getUserObservable(idComment);//WeatherAPI.API_KEY);
    }

    public Observable<Result> getRecipe(int id){
        return fastRecipesApi.getRecipeObservable(id);

    }


    public Observable<ResultComment> getUserComments(int idUser){
        return fastRecipesApi.getCommentsObservable(idUser);
    }

    public Observable<ResultUser> getUsersFav(int idRecipe){
        return fastRecipesApi.getUsersFavObservable(idRecipe);
    }

    public Observable<Result> setFavouriteRec(int idUser, int idRecipe) {
        return fastRecipesApi.setFavRecipe(idUser, idRecipe);
    }



   /*public Recipe getRecipe(int id) {
        Recipe[] tmp = new Recipe[1];
         Call<Recipe> call = fastRecipesApi.getRecipe(id);
         call.enqueue(new Callback<Recipe>() {
             @Override
             public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                 if (!response.isSuccessful()) {
                     String error;
                     if (response.errorBody()
                             .contentType()
                             .subtype()
                             .equals("application/json")) {

                         Log.d("getRecipe", "Response not successful");
                     } else {
                         error = response.message();
                     }

                     tmp[0] = null;
                 }
                 else {
                     tmp[0] = response.body();
                 }
             }

             @Override
             public void onFailure(Call<Recipe> call, Throwable t) {
                 tmp[0] = null;

             }
         });
        //WeatherAPI.API_KEY);

        return tmp;
    return null;}*/
}
