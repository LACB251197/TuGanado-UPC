package com.upc.yourlivestock.interfaces;

import com.upc.yourlivestock.models.LivestockRequest;
import com.upc.yourlivestock.models.LivestockResponse;
import com.upc.yourlivestock.models.SimpleResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface LivestockApi {

    @POST("v1/livestock")
    public Call<SimpleResponse> createLivestock (@Body LivestockRequest livestockRequest);

    @GET("v1/livestock")
    public Call<LivestockResponse> getLivestock ();
}
