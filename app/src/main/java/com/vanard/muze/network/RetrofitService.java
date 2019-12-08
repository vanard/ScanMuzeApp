package com.vanard.muze.network;

import com.vanard.muze.model.DataMuseum;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {
    @GET("searchGET")
    Call<DataMuseum> getDataMuseum(@Query("nama") String museumName);
}
