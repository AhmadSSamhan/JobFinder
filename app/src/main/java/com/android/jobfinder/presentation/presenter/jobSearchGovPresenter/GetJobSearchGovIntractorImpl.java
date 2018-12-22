package com.android.jobfinder.presentation.presenter.jobSearchGovPresenter;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.android.jobfinder.helper.ConstantApp;
import com.android.jobfinder.model.JobSearchGov;
import com.android.jobfinder.network.RetrofitInstance;
import com.android.jobfinder.network.RetrofitInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.android.jobfinder.helper.ConstantApp.API_PER_PAGE;

/**
 * Get Data from SearchGov API
 */
public class GetJobSearchGovIntractorImpl implements JobSearchGovContract.GetJobSearchGovIntractor {
    @Override
    public void getJobSearchGovResponse(String searchQueryKey, String currentPage, String location, final OnFinishedListener onFinishedListener) {
        /* Create handle for the RetrofitInstance interface*/
        RetrofitInterface apiService = RetrofitInstance.getClient().create(RetrofitInterface.class);
        /* Call the method with parameter in the interface to get the Search Response data(SearchGov provider)*/
        String searchQuery = searchQueryKey;
        if (!TextUtils.isEmpty(location)) {
            //Filter by location use this function
            searchQuery = searchQueryKey + "+in+" + location;
        }
        Call<List<JobSearchGov>> call = apiService.getJobSearchGovResponseCall(ConstantApp.BASE_URL_SEARCH_GOV, searchQuery, API_PER_PAGE);

        /*Log the URL called*/
        Log.wtf("URL SearchGov", call.request().url() + "");

        call.enqueue(new Callback<List<JobSearchGov>>() {
            @Override
            public void onResponse(@NonNull Call<List<JobSearchGov>> call, @NonNull Response<List<JobSearchGov>> response) {

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
            public void onFailure(@NonNull Call<List<JobSearchGov>> call, @NonNull Throwable t) {
                onFinishedListener.onFailure(t);
            }
        });
    }
}
