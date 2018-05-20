package com.repogithub.restapicall;

import com.repogithub.model.GetRepo;

import java.util.LinkedHashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface RepoService {

    @GET("users/{username}/repos?")
    Call<List<GetRepo>> getUsername(@Path("username") String username,
                                    @QueryMap(encoded = true) LinkedHashMap<String,String> params);
}
