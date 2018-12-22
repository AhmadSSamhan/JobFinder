package com.android.jobfinder.presentation.presenter.jobSearchGovPresenter;

import com.android.jobfinder.model.JobSearchGov;

import java.util.List;

/**
 * Handel all presenter SearchGov
 */
public class JobSearchPresenterImpl implements JobSearchGovContract.presenter, JobSearchGovContract.GetJobSearchGovIntractor.OnFinishedListener {

    private JobSearchGovContract.MainView mMainView;
    private JobSearchGovContract.GetJobSearchGovIntractor mGetSearchResultIntractor;

    public JobSearchPresenterImpl(JobSearchGovContract.MainView mainView, JobSearchGovContract.GetJobSearchGovIntractor getJobSearchGovIntractor) {
        this.mMainView = mainView;
        this.mGetSearchResultIntractor = getJobSearchGovIntractor;
    }

    @Override
    public void onDestroy() {
        mMainView = null;
    }

    @Override
    public void requestJobSearchGovDataFromServer(String searchQueryKey, String location, String currentPage) {
        if (mMainView != null) {
            mMainView.showProgress();
        }
        mGetSearchResultIntractor.getJobSearchGovResponse(searchQueryKey, currentPage, location, this);
    }

    @Override
    public void onFinished(List<JobSearchGov> jobSearchGovs) {
        if (mMainView != null) {
            mMainView.setDataJobSearchGovToActivity(jobSearchGovs);
            mMainView.hideProgress();
        }
    }

    @Override
    public void onSomethingWrongOnSearchResponse(String s) {
        if (mMainView != null) {
            mMainView.onResponseEmpty(s);
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
