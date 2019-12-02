package com.example.qrapp;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RetrofitInterface {

    @FormUrlEncoded
    @POST("/attendees/{dni}")
    Call<userData> post (@Path("dni") String dni);

}
