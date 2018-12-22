package com.android.jobfinder.presentation.presenter.jobSearchGovPresenter;


import com.android.jobfinder.model.JobSearchGov;

import java.util.List;

/**
 * This interface to handel SearchGov Api and interact with activity
 */
public interface JobSearchGovContract {
    /**
     * Call when user interact with the view and other when view OnDestroy()
     */
    interface presenter {

        void onDestroy();

        void requestJobSearchGovDataFromServer(String searchQueryKey, String location, String currentPage);

    }

    /**
     * showProgress() and hideProgress() would be used for displaying and hiding the progressBar
     * while the setSearchGovResultToActivity and onResponseFailure is fetched from the SearchGovSearchResultIntractorImpl class
     **/
    interface MainView {

        void showProgress();

        void hideProgress();

        void setDataJobSearchGovToActivity(List<JobSearchGov> jobSearchGovs);

        void onResponseEmpty(String strNoData);

        void onResponseFailure(Throwable throwable);

    }

    /**
     * Intractors are classes built for fetching data from web services.
     **/
    interface GetJobSearchGovIntractor {

        interface OnFinishedListener {
            void onFinished(List<JobSearchGov> jobSearchResult);

            void onSomethingWrongOnSearchResponse(String s);

            void onFailure(Throwable t);
        }

        void getJobSearchGovResponse(String searchQueryKey, String currentPage, String location, OnFinishedListener onFinishedListener);
    }
}
