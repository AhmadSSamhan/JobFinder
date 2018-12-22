package com.android.jobfinder.presentation.presenter.GitHubPresenter;


import com.android.jobfinder.model.GitHubJobSearchResult;

import java.util.List;

/**
 * This interface to handel GitHub Api and interact with activity
 */
public interface GitHubContract {
    /**
     * Call when user interact with the view and other when view OnDestroy()
     */
    interface presenter {

        void onDestroy();

        void requestGitHubSearchDataFromServer(String searchQueryKey, String location, String currentPage);

    }

    /**
     * showProgress() and hideProgress() would be used for displaying and hiding the progressBar
     * while the setGitHubSearchResultToActivity and onResponseFailure is fetched from the GitHubSearchResultIntractorImpl class
     **/
    interface MainView {

        void showProgress();

        void hideProgress();

        void setGitHubSearchResultToActivity(List<GitHubJobSearchResult> gitHubJobSearchResult);

        void onResponseEmpty(String strNoData);

        void onResponseFailure(Throwable throwable);

    }

    /**
     * Intractors are classes built for fetching data from web services.
     **/
    interface GetSearchResultIntractor {

        interface OnFinishedListener {
            void onFinished(List<GitHubJobSearchResult> gitHubJobSearchResult);

            void onSomethingWrongOnSearchResponse(String s);

            void onFailure(Throwable t);
        }

        void getGitHubSearchResultResponse(String searchQueryKey, String location, String currentPage, OnFinishedListener onFinishedListener);
    }
}
