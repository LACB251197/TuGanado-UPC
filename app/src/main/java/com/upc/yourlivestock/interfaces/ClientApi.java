package com.upc.yourlivestock.interfaces;

import com.upc.yourlivestock.models.AuthRequest;
import com.upc.yourlivestock.models.AuthResponse;
import com.upc.yourlivestock.models.ClientRequest;
import com.upc.yourlivestock.models.SimpleResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ClientApi {

    @POST("v1/client/auth")
    public Call<AuthResponse> authentication (@Body AuthRequest authRequest);

    @POST("v1/client")
    public Call<SimpleResponse> createClient(@Body ClientRequest simpleResponse);

}
