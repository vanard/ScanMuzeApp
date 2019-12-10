package com.vanard.muze.network;

import com.vanard.muze.model.museum.DataMuseum;
import com.vanard.muze.model.rajaapi.AuthApi;
import com.vanard.muze.model.rajaapi.ResponseApi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {
    @GET("searchGET")
    Call<DataMuseum> getSearchMuseumByName(@Query("nama") String museumName);

    @GET("searchGET")
    Call<DataMuseum> getDataMuseum();

    @GET("poe")
    Call<AuthApi> getAuthToken();

    @GET("MeP7c5ne{token}/m")
    Call<AuthApi> getValidToken(@Path("token") String token);

    @GET("MeP7c5ne{token}/m/wilayah/provinsi")
    Call<ResponseApi> getProvince(@Path("token") String token);

    @GET("MeP7c5ne{token}/m/wilayah/kabupaten")
    Call<ResponseApi> getDistrict(@Path("token") String token,
                                  @Query("idpropinsi") String idProv);

    @GET("MeP7c5ne{token}/m/wilayah/kecamatan")
    Call<ResponseApi> getSubDistrict(@Path("token") String token,
                                     @Query("idkabupaten") String idKabs);
}
