package com.ismael.fastrecipes.interfaces;

import android.util.Log;

import com.ismael.fastrecipes.FastRecipesApplication;
import com.ismael.fastrecipes.model.Comment;
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
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Ismael on 06/02/2018.
 */

public interface FastRecipesApi {

    public static final String BASE_URL = "http://fastrecipesapp.com/apifastrecipes/api/public/";

    //RECETAS
    @GET("recipe/{id}")
    Observable<Result> getRecipeObservable(@Path("id") int id);

    @POST("recipe/")
    Observable<Result> getFiltRecipesObservable(@Body Recipe rModel);

    @POST("recipe/")
    Observable<Result> addRecipeObservable(@Body Recipe addR);

    @PUT("recipe/")
    Observable<Result> modifyRecipeObservable(@Body Recipe addR);

    @DELETE("recipe/{id}")
    Observable<Result> removeRecipeObservable(@Path("id") int idRecipe);

    //FAVS
    @POST("recipe/setfav/{idu}/{idr}/{how}")
    Observable<Result> setFavRecipeObservable(@Path("idu") int idUser, @Path("idr") int idRecipe,@Path("how") int fav);

    //COMENTARIOS
    @GET("comments/{id}")
    Observable<ResultComment> getCommentsObservable(@Path("idUser") int idUser);

    @POST("comments/{id}")
    Observable<Result> sendCommentObservable(@Path("id") int idUser, @Body Comment com);

    @DELETE("comments/{id}")
    Observable<Result> removeCommentObservable(@Path("id") int idComment);

    //USUARIOS
    @GET("user/comment/{id}")
    Observable<ResultUser> getUserObservable(@Path("id") int idComment);

    @POST("user/login/{token}")
    Observable<ResultUser> userLoginObservable(@Path("token") String token);

    @GET("user/recipefavs/{id}")
    Observable<ResultUser> getUsersFavObservable(@Path("idRecipe") int idRecipe);

    @POST("user/registro/{token}")
    Observable<ResultUser> registerObservable(@Path("token") String token, @Body User user);

    @GET("user/{id}")
    Observable<ResultUser> getUserByIdObservable(@Path("id") int idUser);

    @GET("user/recipes/{id}")
    Observable<Result> getUserRecipesObservable(@Path("id") int idUser);

    @PUT("/user")
    Observable<ResultUser> updateUserObservable(@Body User userData);
}
