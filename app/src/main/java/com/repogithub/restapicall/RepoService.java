package com.repogithub.restapicall;

import com.repogithub.model.GetRepo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RepoService {

    @GET("users/{username}/repos")
    Call<List<GetRepo>> getUsername(@Path("username") String username);
}
