package com.example.merosathi.service;

import com.example.merosathi.model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    @FormUrlEncoded
    @POST("api/login")
    Call<User> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("api/register")
    Call<User> register(@Field("name") String name, @Field("username") String username, @Field("email") String email, @Field("contact") String contact, @Field("dob") String dateOfBirth);

    @FormUrlEncoded
    @POST("api/change-password")
    Call<User> changePassword(@Header("Authorization") String token, @Field("old_password") String oldPassword, @Field("new_password") String newPassword, @Field("confirm_password") String confirmPassword);
}
