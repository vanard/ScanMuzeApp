package com.vanard.muze.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit;
    public static final String BASE_URL = "http://jendela.data.kemdikbud.go.id/api/index.php/CcariMuseum/";
    public static final String BASE_URL_RAJAAPI = "https://x.rajaapi.com/";

    public static Retrofit getRetrofitInstance(String url) {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static void clearClient() {
        retrofit = null;
    }
}
