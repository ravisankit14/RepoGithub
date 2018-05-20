package com.repogithub.restapicall;

import com.repogithub.restapicall.model.ModelUsername;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("users/{username}")
    Call<ModelUsername> getUsername(@Path("username") String username);
}
