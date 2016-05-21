package com.example.raymundcat.safetycj.http;

import com.example.raymundcat.safetycj.models.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Marbeen on 5/21/2016.
 */
public interface PostApiInterface {

//    @POST("/api-debug/user-exists/{userID}")
//    Call<User> listRepos(@Path("user") String user);
//    public void getFeed(@Path("userID") String userID, Callback response);    // For get
//    Call<ResponseBody> listRepos(@Path("userID") String user);
    @POST("api-debug/users")
    Call<User> createUser(@Body User user);
}
