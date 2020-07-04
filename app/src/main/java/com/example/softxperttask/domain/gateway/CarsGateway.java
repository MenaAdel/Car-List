package com.example.softxperttask.domain.gateway;

import com.example.softxperttask.entites.CarResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CarsGateway {
    @GET("cars")
    Single<CarResponse> getCars(@Query("page") int page);
}
