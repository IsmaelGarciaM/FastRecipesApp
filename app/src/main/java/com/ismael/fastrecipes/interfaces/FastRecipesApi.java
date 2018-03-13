package com.ismael.fastrecipes.interfaces;

import com.ismael.fastrecipes.model.Recipe;
import com.ismael.fastrecipes.model.User;
import com.ismael.fastrecipes.utils.Result;
import com.ismael.fastrecipes.utils.ResultUser;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * FastRecipesApi -> Interfaz para la gestión de las llamadas a la API
 * @author Ismael Garcia
 * */

public interface FastRecipesApi {

    public static final String BASE_URL = "https://ismagm9481493.ipage.com/wb_fastrecipesapp.com/apifastrecipes/api/public/";

    //RECETAS
    @GET("recipe/id/{id}")
    Observable<Result> getRecipeObservable(@Path("id") String id);

    @POST("search")
    Observable<Result> getFiltRecipesObservable(@Body Recipe rModel);

    @POST("recipe")
    Observable<Result> addRecipeObservable(@Body Recipe addR);

    @DELETE("recipe/{id}")
    Observable<Result> removeRecipeObservable(@Path("id") int idRecipe);


    @POST("recipe/update")
    Observable<Result> modifyRecipeObservable(@Body Recipe addR);

    //FAVS
    @POST("recipe/fav/{id}")
    Observable<Result> setFavRecipeObservable(@Path("id") String id);

    //USUARIOS
    @GET("user/comment/{id}")
    Observable<ResultUser> getUserObservable(@Path("id") int idComment);

    @GET("user/login/{token}")
    Observable<ResultUser> userLoginObservable(@Path("token") String token);

    @GET("user/recipefavs/{id}")
    Observable<ResultUser> getUsersFavObservable(@Path("id") int idRecipe);

    @POST("user/registro/{token}")
    Observable<ResultUser> registerObservable(@Path("token") String token, @Body User user);

    @GET("user/{id}")
    Observable<ResultUser> getUserByIdObservable(@Path("id") int idUser);

    @GET("user/recipes/{id}")
    Observable<Result> getUserRecipesObservable(@Path("id") int idUser);

    @POST("user/update")
    Observable<ResultUser> updateUserObservable(@Body User userData);

    //RATING
    @POST("rating/{id}")
    Observable<Result> setRatingObservable(@Path("id")String rate);

    @GET("recipe/user/{id}")
    Observable<Result> getMyRecipesObservable(@Path("id") int idUser);

    @GET("favs/{id}")
    Observable<Result> getFavRecipesObservable(@Path("id") int idUser);

    @GET("recipeoftheday")
    Observable<Result> getRecipeOfDayObservable();
}
