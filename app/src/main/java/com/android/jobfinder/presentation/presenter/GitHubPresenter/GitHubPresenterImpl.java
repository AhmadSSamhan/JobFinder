package com.android.jobfinder.presentation.presenter.GitHubPresenter;


import com.android.jobfinder.model.GitHubJobSearchResult;

import java.util.List;

/**
 * Handel all presenter GitHub
 */
public class GitHubPresenterImpl implements GitHubContract.presenter, GitHubContract.GetSearchResultIntractor.OnFinishedListener {

    private GitHubContract.MainView mMainView;
    private GitHubContract.GetSearchResultIntractor mGetSearchResultIntractor;

    public GitHubPresenterImpl(GitHubContract.MainView mainView, GitHubContract.GetSearchResultIntractor getSearchResultIntractor) {
        this.mMainView = mainView;
        this.mGetSearchResultIntractor = getSearchResultIntractor;
    }

    @Override
    public void onDestroy() {
        mMainView = null;
    }

    @Override
    public void requestGitHubSearchDataFromServer(String searchQueryKey, String location,String currentPage) {
        if (mMainView != null) {
            mMainView.showProgress();
        }
        mGetSearchResultIntractor.getGitHubSearchResultResponse(searchQueryKey,location,currentPage, this);
    }

    @Override
    public void onFinished(List<GitHubJobSearchResult> gitHubJobSearchResult) {
        if (mMainView != null) {
            mMainView.setGitHubSearchResultToActivity(gitHubJobSearchResult);
            mMainView.hideProgress();
        }
    }

    @Override
    public void onSomethingWrongOnSearchResponse(String strNoData) {
        if (mMainView != null) {
            mMainView.onResponseEmpty(strNoData);
            mMainView.hideProgress();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        if (mMainView != null) {
            mMainView.onResponseFailure(t);
            mMainView.hideProgress();
        }
    }
}
