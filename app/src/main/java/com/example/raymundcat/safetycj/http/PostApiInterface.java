package com.example.raymundcat.safetycj.http;

import com.example.raymundcat.safetycj.models.EventLocations;
import com.example.raymundcat.safetycj.models.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Marbeen on 5/21/2016.
 */
public interface PostApiInterface {

//    @POST("/api-debug/user-exists/{userID}")
//    Call<User> listRepos(@Path("user") String user);
//    public void getFeed(@Path("userID") String userID, Callback response);    // For get
//    Call<ResponseBody> listRepos(@Path("userID") String user);

//    @GET("api-debug/locations")
//    Call<ResponseBody> getLocations();

    @GET("api-debug/locations")
    Call<EventLocations> getLocations();

    @Multipart
    @POST ("/api-debug/users")
    Call<ResponseBody> createUser(
            @Part("name") String name,
            @Part("facebookId") String facebookId,
            @Part("birthday") String birthday,
            @Part("email") String email
    );
//    @Headers({ "Content-Type: multipart/form-data"})
//    Call<ResponseBody> createUser(@Body User user);
//    Call<ResponseBody> createUser(
//            @Body String name,
//            @Body String facebookId,
//            @Body String birthday
//            )
}
