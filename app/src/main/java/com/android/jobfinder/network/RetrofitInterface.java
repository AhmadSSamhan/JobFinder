package com.android.jobfinder.network;


import com.android.jobfinder.model.JobSearchGov;
import com.android.jobfinder.model.GitHubJobSearchResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface RetrofitInterface {

    /**
     * Github provider
     * Use Page to load specific page
     * description , A search term, such as "ruby" or "java". This parameter is aliased to search.
     * location — A city name, zip code, or other location search term.
     */
    @Headers("Content-Type: application/json")
    @GET("positions.json?")
    Call<List<GitHubJobSearchResult>> getGitHubJobSearchResponseCall(@Query("page") String pageCount, @Query("description") String searchKey, @Query("location") String location);

    /**
     * SearchGov provider
     * Url, to use Dynamic provider
     * query , A search term, such as "ruby" or "java",attempts to extract as much “signal” as possible from the input text.
     * size ,Specifies how many results are returned (up to 100 at a time).
     */
    @Headers("Content-Type: application/json")
    @GET
    Call<List<JobSearchGov>> getJobSearchGovResponseCall(@Url String url, @Query("query") String searchKey, @Query("size") String pageSize);
}
