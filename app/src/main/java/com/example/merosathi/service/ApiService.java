package com.example.merosathi.service;

import com.example.merosathi.model.Article;
import com.example.merosathi.model.ArticleList;
import com.example.merosathi.model.BabyGrowth;
import com.example.merosathi.model.BabyGrowthList;
import com.example.merosathi.model.BodyChange;
import com.example.merosathi.model.BodyChangeList;
import com.example.merosathi.model.MainModel;
import com.example.merosathi.model.Notification;
import com.example.merosathi.model.NotificationResponse;
import com.example.merosathi.model.SingleNotificationResponse;
import com.example.merosathi.model.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @GET("api/weekly-baby-growth")
    Call<BabyGrowthList> babyGrowthIndex(@Header("Authorization") String token);

    @GET("api/weekly-baby-growth/{id}")
    Call<BabyGrowth> babyGrowthShow(@Header("Authorization") String token, @Path("id") Integer id);

    @GET("api/articles")
    Call<ArticleList> articleIndex(@Header("Authorization") String token);

    @GET("api/articles/{id}")
    Call<Article> articleShow(@Header("Authorization") String token, @Path("id") Integer id);

    @GET("api/body-changes")
    Call<BodyChangeList> bodyChangeIndex(@Header("Authorization") String token);

    @GET("api/body-changes/{id}")
    Call<BodyChange> bodyChangeShow(@Header("Authorization") String token, @Path("id") Integer id);

    @GET("api/main")
    Call<MainModel> main(@Header("Authorization") String token, @Query("date") String date);

    @GET("api/profile")
    Call<User> profile(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("api/profile/edit")
    Call<User> editProfile(@Header("Authorization") String token, @Field("name") String name, @Field("contact") String contact, @Field("email") String email, @Field("dob") String dob);

    @GET("api/notifications")
    Call<NotificationResponse> notifications(@Header("Authorization") String token);

    @GET("api/notifications/{id}")
    Call<SingleNotificationResponse> singleNotification(@Header("Authorization") String token, @Path("id") Integer id);

    @FormUrlEncoded
    @POST("api/calculate")
    Call<HashMap<String, String>> calculateDay(
            @Header("Authorization") String token,
            @FieldMap Map<String, Object> fields
    );

    @FormUrlEncoded
    @POST("api/liked")
    Call<String> like(@Header("Authorization") String token, @Field("liked") Boolean liked, @Field("article_id") Integer articleId, @Field("weekly_baby_growth_id") Integer babyGrowthId, @Field("body_change_id") Integer bodyChangeId);
}
