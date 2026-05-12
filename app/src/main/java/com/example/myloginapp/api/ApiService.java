package com.example.myloginapp.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    @FormUrlEncoded
    @POST("api.php")
    Call<ApiResponse> signup(
        @Field("action") String action,
        @Field("name") String name,
        @Field("email") String email,
        @Field("dob") String dob,
        @Field("password") String password
    );

    @FormUrlEncoded
    @POST("api.php")
    Call<ApiResponse> login(
        @Field("action") String action,
        @Field("email") String email,
        @Field("password") String password
    );
}
