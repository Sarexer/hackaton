package ru.cppinfo.igo.service;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestService {

    @FormUrlEncoded
    @POST("/new")
    Call<Object> newTable(@Field("message") String message, @Field("ID") int ID, @Field("hashtag") String hashtag);

    @GET("/reg")
    Call<Object> reg(@Query("email") String email, @Query("FIO") String FIO, @Query("bdate") String bdate, @Query("pass") String pass, @Query("interests") String interests);

    @GET("/entry")
    Call<Object> entr(@Query("email") String email, @Query("pass") String pass, @Query("tokenID") String tokenID);

    @GET("/getIvents")
    Call<Object> getIvents(@Query("id") String id);



    //home
    /*Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://100.64.42.29:3347")
            .addConverterFactory(GsonConverterFactory.create())
            .build();*/
    //сервер
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://185.43.4.38:3348")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
