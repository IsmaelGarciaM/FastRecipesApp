package com.ismael.fastrecipes.interfaces;

import android.util.Log;

import com.ismael.fastrecipes.FastRecipesApplication;
import com.ismael.fastrecipes.model.Recipe;
import com.ismael.fastrecipes.model.User;
import com.ismael.fastrecipes.utils.Result;
import com.ismael.fastrecipes.utils.ResultComment;
import com.ismael.fastrecipes.utils.ResultUser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Ismael on 06/02/2018.
 */

public interface FastRecipesApi {

    public static final String BASE_URL = "http://10.0.2.2/apifastrecipes/api/public/";

/*
    @POST("user/login/{token}")
    Call<User> login(@Path("token") String token);*/

    @POST("user/registro/{token}")
    Observable<ResultUser> register(@Path("token") String token, @Body User user);

    @GET("recipe/{id}")
    Observable<Result> getRecipeObservable(@Path("id") int id);

    @GET("comments/{idUser}")
    Observable<ResultComment> getCommentsObservable(@Path("idUser") int idUser);

    @GET("user/comment/{idComment}")
    Observable<ResultUser> getUserObservable(@Path("idComment") int idComment);

    @POST("user/login/{token}")
    Observable<ResultUser> userLoginObservable(@Path("token") String token);

    @GET("user/recipefavs/{idRecipe}")
    Observable<ResultUser> getUsersFavObservable(@Path("idRecipe") int idComment);

    @POST("recipe/setfav/{idUser}/{idRecipe}/{fav}")
    Observable<Result> setFavRecipe(int idUser, int idRecipe, boolean fav);

    @GET("recipe/")
    Observable<Result> getFiltRecipes(@Body Recipe rModel);

    @POST("recipe/")
    Observable<Result> addRecipe(Recipe addR);

    @PUT("recipe/")
    Observable<Result> modifyRecipe(Recipe addR);

    @DELETE("recipe/{id}")
    Observable<Integer> removeRecipe(int idRecipe);




/*
    @GET("recipe/{id}")
    Observable<Recipe> getRecipeObservable(@Path("id") int id);

*/

/*
    @POST("recipe/{id}")
    Call<Recipe> getRecipe(@Path("token") int id);*/
}
