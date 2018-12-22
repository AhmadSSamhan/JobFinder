package com.android.jobfinder.presentation.presenter.GitHubPresenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.jobfinder.model.GitHubJobSearchResult;
import com.android.jobfinder.network.RetrofitInstance;
import com.android.jobfinder.network.RetrofitInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Get Data from GitHub API
 */
public class GitHubSearchResultIntractorImpl implements GitHubContract.GetSearchResultIntractor {

    @Override
    public void getGitHubSearchResultResponse(String searchQueryKey,String location, String currentPage, final OnFinishedListener onFinishedListener) {

        /* Create handle for the RetrofitInstance interface*/
        RetrofitInterface apiService = RetrofitInstance.getClient().create(RetrofitInterface.class);

        /* Call the method with parameter in the interface to get the Search Response data (GitHub provider)*/
        Call<List<GitHubJobSearchResult>> call = apiService.getGitHubJobSearchResponseCall(currentPage, searchQueryKey,location);

        /*Log the URL called*/
        Log.wtf("URL GitHub", call.request().url() + "");

        call.enqueue(new Callback<List<GitHubJobSearchResult>>() {
            @Override
            public void onResponse(@NonNull Call<List<GitHubJobSearchResult>> call, @NonNull Response<List<GitHubJobSearchResult>> response) {

                try {
                    if (response.isSuccessful() && response.body() == null) {
                        //TODO change string try again
                        onFinishedListener.onSomethingWrongOnSearchResponse("TEXT_TRY_AGAIN");
                        return;
                    }
                    if (response.isSuccessful() && response.body() != null) {
                        onFinishedListener.onFinished(response.body());
                    }
                } catch (Exception e) {
                    e.getCause();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<GitHubJobSearchResult>> call, @NonNull Throwable t) {
                onFinishedListener.onFailure(t);
            }
        });

    }
}
