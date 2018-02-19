package com.ismael.fastrecipes.utils;

import android.util.Log;

import com.ismael.fastrecipes.FastRecipesApplication;
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
    public Observable<Result> getRecipe(int id){
        return fastRecipesApi.getRecipeObservable(id);//FastRecipesApplication.getToken(),
    }


    /**
     * Realiza la llamada a la api para obtener los comentarios realizados por un usuario
     * @param idUser Id del usuario
     * @return Devuelve un Observable con un array de ResultComment, array con los comentarios
     */
    public Observable<ResultComment> getUserComments(int idUser){
        return fastRecipesApi.getCommentsObservable(idUser);
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
     * @param idUser Id del usuario que marca o desmarca
     * @param idRecipe Id de la receta marcada o desmarcada
     * @return Devuelve la receta marcada o desmarcada
     */
    public Observable<Result> setFavouriteRec(int idUser, int idRecipe, boolean fav) {
        return fastRecipesApi.setFavRecipe(idUser, idRecipe, fav);
    }


    /**
     * Realiza la llamada a la api para registrar un usuario
     * @param token Token de firebase para autenticación
     * @return
     */
    public Observable<ResultUser> registerUser(String token, User u) {
        return fastRecipesApi.register(token, u);
    }

    public Observable<Result> getRecipesFiltered(Recipe rModel) {
        return fastRecipesApi.getFiltRecipes(rModel);
    }

    public Observable<Result> addRecipe(Recipe addR) {
        return fastRecipesApi.addRecipe(addR);
    }

    public Observable<Result> modifyRecipe(Recipe addR) {
        return fastRecipesApi.modifyRecipe(addR);
    }

    public Observable<Integer> removeRecipe(int idRecipe) {
        return fastRecipesApi.removeRecipe(idRecipe);
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
