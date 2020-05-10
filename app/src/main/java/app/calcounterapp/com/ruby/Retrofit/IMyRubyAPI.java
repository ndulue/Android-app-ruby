package app.calcounterapp.com.ruby.Retrofit;

import androidx.room.Delete;
import app.calcounterapp.com.ruby.Model.FavouriteModel;
import app.calcounterapp.com.ruby.Model.LoginUserModel;
import app.calcounterapp.com.ruby.Model.ProductModel;
import app.calcounterapp.com.ruby.Model.RegisterUserModel;
import app.calcounterapp.com.ruby.Model.UserModel;
import io.reactivex.Observable;
import retrofit2.http.DELETE;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IMyRubyAPI {

    @GET("user")
    Observable<LoginUserModel> getUser(@Query("email") String email,
                                       @Query("password") String password);

    @POST("user")
    @FormUrlEncoded
    Observable<RegisterUserModel> postUser(@Query("name") String name,
                                           @Query("email") String email,
                                           @Query("password") String password);

    @GET("product")
    Observable<ProductModel> getProduct(@Query("api_key") int api_key);

    @GET("favourite")
    Observable<FavouriteModel> getFav(@Query("api_key") int api_key,
                                      @Query("email") String email,
                                      @Query("sku") String sku);

    @GET("favourite")
    Observable<FavouriteModel> getAllFav(@Query("api_key") int api_key,
                                      @Query("email") String email);

    @POST("favourite")
    @FormUrlEncoded
    Observable<FavouriteModel> postFav(@Query("api_key") int api_key,
                                       @Query("email") String email,
                                       @Query("image") String image,
                                       @Query("price") int price,
                                       @Query("sku") String sku,
                                       @Query("title") String title,
                                       @Query("manufacturer") String manufacturer);

    @DELETE("favourite")
    @FormUrlEncoded
    Observable<FavouriteModel> deleteFav(@Query("api_key") int api_key,
                                         @Query("email") String email,
                                         @Query("sku") String sku);


}
