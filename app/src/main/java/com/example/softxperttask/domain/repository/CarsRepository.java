package com.example.softxperttask.domain.repository;

import com.example.softxperttask.domain.client.APIClient;
import com.example.softxperttask.domain.gateway.CarsGateway;
import com.example.softxperttask.entites.CarResponse;

import io.reactivex.Single;

public class CarsRepository {
    private final CarsGateway endpoint;

    public CarsRepository() {
        endpoint = APIClient.getAPIClientInstant().create(CarsGateway.class);
    }

    public Single<CarResponse> getCars(int page){
        return endpoint.getCars(page);
    }
}
