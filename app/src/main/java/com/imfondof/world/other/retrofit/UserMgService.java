package com.imfondof.world.other.retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Imfondof on 2020/1/6 9:46
 * description:
 */
public interface UserMgService {
//    @GET("login")
//    Call<UserInfoModel> login(@Query("username") String username, @Query("password") String pwd);

//    post方式
    @POST("user/login")
    @FormUrlEncoded
    Call<UserInfoModel> login(@Field("username") String username, @Field("password") String password);
}
