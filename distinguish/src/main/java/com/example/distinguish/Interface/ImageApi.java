package com.example.distinguish.Interface;

import android.app.DownloadManager;

import com.example.distinguish.db.HttpResult;

import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ImageApi {
    @FormUrlEncoded()
    @POST("recover")
    Call<HttpResult> getCall(@Field("img") String img, @Header("Authorization") String authorization);
}
