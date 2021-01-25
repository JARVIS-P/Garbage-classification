package com.example.distinguish.Interface;

import com.example.distinguish.db.HttpResult;
import com.example.distinguish.db.HttpResult1;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface StringApi {
    @GET("/recover_word")
    Call<HttpResult1> getCall(@Query("city") String city,@Query("name") String name, @Header("Authorization") String authorization);
}
