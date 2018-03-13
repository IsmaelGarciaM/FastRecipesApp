package com.ismael.fastrecipes.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ismael.fastrecipes.FastRecipesApplication;
import com.ismael.fastrecipes.interfaces.FastRecipesApi;
import com.ismael.fastrecipes.model.Comment;
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
 * FastRecipesService - Clase que implementa las llamadas mediante Retrofit a la base de datos
 * @author Ismael Garcia
 */

public class FastRecipesService {
    //Instancia de la api
    private FastRecipesApi fastRecipesApi;

    /**
     * Constructor del servicio
     */
    public FastRecipesService() {

        Gson gson = new GsonBuilder()
                .setLenient().create();
        //Conexión con servicio rest
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FastRecipesApi.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //Conexión con la api
        fastRecipesApi = retrofit.create(FastRecipesApi.class);
    }

    public FastRecipesApi getAPI() {
        return fastRecipesApi;
    }

    /**
     * Realiza la llamada a la api para el login con el token de autenticación
     * @param token
     * @return Devuelve un Observable con un array de ResultUser (en realidad sólo contendrá un usuario)
     */
    public Observable<ResultUser> getUser(String token) {
        return fastRecipesApi.userLoginObservable(token);
    }

    /**
     * Realiza la llamada a la api para obtener la información de un usuario mediante un comentario
     * @param idComment Id del comentario realizado por el usuario
     * @return Devuelve un Observable con un array de ResultUser (en realidad sólo contendrá un usuario)
     */
    public Observable<ResultUser> getUserByComment(int idComment) {
        return fastRecipesApi.getUserObservable(idComment);
    }

    /**
     * Realiza la llamada a la api para obtener la información de una receta
     * @param id Id de la receta a obtener
     * @return Devuelve un objeto Observable con un array de Result, que es la clase que recoge los datos del json de la respuesta del servidor
     */
    public Observable<Result> getRecipe(String id){
        return fastRecipesApi.getRecipeObservable(id);//FastRecipesApplication.getToken(),
    }



    /**
     * Realiza la llamada a la api para obtener los usuarios que han marcado como favorita una receta
     * @param idRecipe Id de la receta
     * @return Devuelve un Observable con un array de ResultUser, array con los usuario
     */
    public Observable<ResultUser> getUsersFav(int idRecipe){
        return fastRecipesApi.getUsersFavObservable(idRecipe);
    }

    /**
     * Añade o borra una receta como favorita de un usuario
     * @param idfav String que contiene los datos de receta y usuario para cambiar la asgnación de favorito
     * @return Devuelve la receta marcada o desmarcada
     */
    public Observable<Result> setFavouriteRec(String idfav) {
        return fastRecipesApi.setFavRecipeObservable(idfav);
    }


    /**
     * Realiza la llamada a la api para registrar un usuario
     * @param token Token de firebase para autenticación
     * @return
     */
    public Observable<ResultUser> registerUser(String token, User u) {
        return fastRecipesApi.registerObservable(token, u);
    }

    public Observable<Result> getRecipesFiltered(Recipe rModel) {
        return fastRecipesApi.getFiltRecipesObservable(rModel);
    }

    public Observable<Result> addRecipe(Recipe addR) {
        return fastRecipesApi.addRecipeObservable(addR);
    }

    public Observable<Result> modifyRecipe(Recipe addR) {
        return fastRecipesApi.modifyRecipeObservable(addR);
    }

    public Observable<Result> removeRecipe(int idRecipe) {
        return fastRecipesApi.removeRecipeObservable(idRecipe);
    }

    public Observable<Result> sendComment(int idUser, Comment com) {
        return null;//fastRecipesApi.sendCommentObservable(idUser, com);
    }

    public Observable<Result> removeComment(int idComment) {
        return null;//fastRecipesApi.removeCommentObservable(idComment);
    }

    public Observable<ResultUser> getUser(int idUser ) {
        return fastRecipesApi.getUserByIdObservable(idUser);
    }

    public Observable<Result> getUserRecipes(int idUser) {
        return fastRecipesApi.getUserRecipesObservable(idUser);
    }

    public Observable<ResultUser> updateProfile(User userData) {
        return fastRecipesApi.updateUserObservable(userData);

    }

    public Observable<Result> setRating(String idRating) {
        return fastRecipesApi.setRatingObservable(idRating);

    }

    public Observable<Result> getMyRecipes(int idUser) {
        return fastRecipesApi.getMyRecipesObservable(idUser);
    }

    public Observable<Result> getFavsRecipes(int idUser) {
        return fastRecipesApi.getFavRecipesObservable(idUser);
    }

    public Observable<Result> getRecipeOfDay() {
        return fastRecipesApi.getRecipeOfDayObservable();
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
