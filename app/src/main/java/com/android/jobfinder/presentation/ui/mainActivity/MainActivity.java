package com.android.jobfinder.presentation.ui.mainActivity;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.jobfinder.R;
import com.android.jobfinder.adapter.PaginationScrollListener;
import com.android.jobfinder.adapter.SearchResultAdapter;
import com.android.jobfinder.dialog.FilterDialog;
import com.android.jobfinder.helper.ConstantApp;
import com.android.jobfinder.model.GitHubJobSearchResult;
import com.android.jobfinder.model.JobSearchGov;
import com.android.jobfinder.my_interface.SearchComponentInfo;
import com.android.jobfinder.presentation.BaseActivity;
import com.android.jobfinder.presentation.presenter.GitHubPresenter.GitHubContract;
import com.android.jobfinder.presentation.presenter.GitHubPresenter.GitHubPresenterImpl;
import com.android.jobfinder.presentation.presenter.GitHubPresenter.GitHubSearchResultIntractorImpl;
import com.android.jobfinder.presentation.presenter.jobSearchGovPresenter.GetJobSearchGovIntractorImpl;
import com.android.jobfinder.presentation.presenter.jobSearchGovPresenter.JobSearchGovContract;
import com.android.jobfinder.presentation.presenter.jobSearchGovPresenter.JobSearchPresenterImpl;
import com.android.jobfinder.utils.CustomViewNoData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.jobfinder.helper.ConstantApp.PAGE_START;
import static com.android.jobfinder.helper.ConstantApp.TOTAL_PAGES;

/**
 * This activity to display all data coming from API (Provider)
 * 1- Use Swipe refresh to reload list of data.
 * 2- Use 2 provider and merge 2 list in 1 list by interface.
 * 3- Use custom view if no data from APi or something happens.
 * 4- Use Floating button to Filter list by types selected.
 * 5-Use adapter to display all data from API.
 * 6- Use Animation to open/close activity
 */
public class MainActivity extends BaseActivity implements GitHubContract.MainView, JobSearchGovContract.MainView, FilterDialog.CallBackFilter, SwipeRefreshLayout.OnRefreshListener, CustomViewNoData.CallBackClickTryAgainButton {

    @BindView(R.id.recycle_view_search_result)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_load_data)
    ProgressBar mBarProgressBar;
    @BindView(R.id.fab_filter)
    FloatingActionButton mFloatingFilterButton;
    @BindView(R.id.layout_main)
    RelativeLayout mLayoutRoot;
    @BindView(R.id.swipe_refresh_main)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.custom_view_no_data)
    CustomViewNoData mCustomViewNoData;
    private GitHubContract.presenter mPresenter;
    private JobSearchGovContract.presenter mPresenterGov;
    private List<SearchComponentInfo> mSearchComponentInfo = new ArrayList<>();
    private SearchResultAdapter mAdapter;
    private boolean isListOfJobSearchGovReady;
    private boolean isListOfJobSearchResultReady;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = PAGE_START;
    private List<String> mStringListOfJobTitle;
    private List<String> mStringListOfLocation;
    private boolean isLoadListOfPosition;
    private boolean isLoadListOfLocation;
    private String mStringJobTitle;
    private String mStringJobLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setToolbarTitle(getString(R.string.txt_jobs_you_search));
        initPresenter();
        getExtraIntent();
        initSwipeRefresh();
        mCustomViewNoData.setCallBackIfNoData(this);
    }

    /**
     * initializing the provider to use
     */
    private void initPresenter() {
        mPresenter = new GitHubPresenterImpl(this, new GitHubSearchResultIntractorImpl());
        mPresenterGov = new JobSearchPresenterImpl(this, new GetJobSearchGovIntractorImpl());
    }

    /**
     * initializing SwipeRefresh
     */
    private void initSwipeRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.deep_purple_100), ContextCompat.getColor(getContext(), R.color.colorPrimary));
    }

    /**
     * Get Title and location from Search Activity if exist
     */
    private void getExtraIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mStringJobTitle = bundle.getString(ConstantApp.JOB_TITLE);
            mStringJobLocation = bundle.getString(ConstantApp.JOB_LOCATION);
        }
        /*
         * Submit title and location (is exit) to provider to load data
         */
        submitDataToProvider(mStringJobTitle, mStringJobLocation, currentPage);
    }


    /**
     * Show Progress bar before load data and display list
     * Hide FloatingFilter if Api data not completed
     */
    @Override
    public void showProgress() {
        mBarProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mFloatingFilterButton.setVisibility(View.INVISIBLE);
    }

    /**
     * - Hide Progress bar after load data and display on list
     * - Check if list of provider is Empty
     */
    @Override
    public void hideProgress() {
        if (isLoadDataCompleted()) {
            mBarProgressBar.setVisibility(View.INVISIBLE);
            mSwipeRefreshLayout.setRefreshing(false);
            mFloatingFilterButton.setVisibility(View.VISIBLE);
            if (mSearchComponentInfo.isEmpty()) {
                mCustomViewNoData.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.INVISIBLE);
                return;
            }
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Get Data from SearchGov provider
     * - Add this data to list of SearchComponentInfo(interface)
     * - Update recycle view and adapter
     * - Set flag isListOfJobSearchGovReady = true, To alarm adapter if load data success
     */
    @Override
    public void setDataJobSearchGovToActivity(List<JobSearchGov> jobSearchGovs) {
        mSearchComponentInfo.addAll(jobSearchGovs);
        isListOfJobSearchGovReady = true;
        setDataToRecycleView();
    }

    /**
     * Get Data from GitHub provider
     * - Add this data to list of SearchComponentInfo(interface)
     * - Update recycle view and adapter
     * - Set flag isListOfJobSearchResultReady = true, To alarm adapter if load data success
     */
    @Override
    public void setGitHubSearchResultToActivity(List<GitHubJobSearchResult> gitHubJobSearchResult) {
        mSearchComponentInfo.addAll(gitHubJobSearchResult);
        isListOfJobSearchResultReady = true;
        setDataToRecycleView();
    }

    /**
     * If no data return from provider
     * Display CustomView(No data found)
     */
    @Override
    public void onResponseEmpty(String strNoData) {
        mCustomViewNoData.setVisibility(View.VISIBLE);
        mBarProgressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * If no data return from provider
     * Display CustomView(No data found)
     */
    @Override
    public void onResponseFailure(Throwable throwable) {
        mCustomViewNoData.setVisibility(View.VISIBLE);
        mBarProgressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * Init Recycle view
     * Init Adapter
     * Set All Data to recycle view
     * Use load more to load more data
     * Get list of Position and use it filter autoComplete
     * Get list of location and use it filter autoComplete
     */
    private void setDataToRecycleView() {
        if (!isLoadDataCompleted()) {
            return;
        }
        if (mSearchComponentInfo.isEmpty()) {
            mCustomViewNoData.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            return;
        }
        mAdapter = new SearchResultAdapter(this, mSearchComponentInfo);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getContext(), 1);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new PaginationScrollListener(mGridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                if (mSearchComponentInfo.size() <= 10) {
                    return;
                }
                isLoading = true;
                currentPage += 1;
                submitDataToProvider(mStringJobTitle, mStringJobLocation, currentPage);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        if (!isLoadListOfPosition && !isLoadListOfLocation) {
            getListOfPosition();
            getListOfLocation();
            isLoadListOfPosition = false;
            isLoadListOfLocation = false;
        }
    }

    /**
     * Submit title and location (is exit) to provider to load data
     */
    private void submitDataToProvider(String key, String location, int currentPage) {
        mPresenter.requestGitHubSearchDataFromServer(key, location, String.valueOf(currentPage));
        mPresenterGov.requestJobSearchGovDataFromServer(key, location, String.valueOf(currentPage));
    }

    /**
     * Check if 2 provider loaded successfully
     *
     * @return boolean
     */
    private boolean isLoadDataCompleted() {
        return isListOfJobSearchGovReady && isListOfJobSearchResultReady;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Get List of Position and use it on filter
     */
    private void getListOfPosition() {
        isLoadListOfPosition = true;
        mStringListOfJobTitle = new ArrayList<>();
        if (mSearchComponentInfo.isEmpty()) {
            return;
        }
        for (SearchComponentInfo searchComponentInfo : mSearchComponentInfo) {
            if (!TextUtils.isEmpty(searchComponentInfo.getJobTitle())) {
                mStringListOfJobTitle.add(searchComponentInfo.getJobTitle());
            }
        }
    }

    /**
     * Get List of Location and use it on filter
     */
    private void getListOfLocation() {
        isLoadListOfLocation = true;
        mStringListOfLocation = new ArrayList<>();
        if (mSearchComponentInfo.isEmpty()) {
            return;
        }
        for (SearchComponentInfo searchComponentInfo : mSearchComponentInfo) {
            if (!TextUtils.isEmpty(searchComponentInfo.getLocation())) {
                mStringListOfLocation.add(searchComponentInfo.getLocation());
            }
            if (!searchComponentInfo.getListOfLocation().isEmpty()) {
                mStringListOfLocation.addAll(searchComponentInfo.getListOfLocation());
            }
        }
    }

    /**
     * Filter data
     */
    @OnClick(R.id.fab_filter)
    public void onClickFilterButton() {
        new FilterDialog(getContext())
                .setListOfPosition(mStringListOfJobTitle)
                .setListOfLocation(mStringListOfLocation)
                .setCallBack(this)
                .show();
    }

    /**
     * Return Callback from Filter
     * Submit filter to specific provider and update list (RecycleView)
     * <p>
     * Use Flag isListOfJobSearchResultReady  to show only the required list using GitHub provider
     * and set isListOfJobSearchGovReady to false
     * <p>
     * Use Flag isListOfJobSearchGovReady  to show only the required list using SearchGov provider
     * and set isListOfJobSearchResultReady to false
     *
     * @param typeProviderSelected return selected provider
     * @param selectedPosition     return position
     * @param selectedLocation     return location
     */
    @Override
    public void onCallBackFilterData(String typeProviderSelected, String selectedPosition, String selectedLocation) {
        currentPage = 1;
        mSearchComponentInfo = new ArrayList<>();
        if (ConstantApp.PROVIDER_SEARCH_GOV.equalsIgnoreCase(typeProviderSelected)) {
            //isListOfJobSearchResultReady equal true to hide progress bar and show the result
            isListOfJobSearchResultReady = true;
            mPresenterGov.requestJobSearchGovDataFromServer(selectedPosition, selectedLocation, String.valueOf(currentPage));
            return;
        }
        if (ConstantApp.PROVIDER_GITHUB.equalsIgnoreCase(typeProviderSelected)) {
            //isListOfJobSearchGovReady equal true to hide progress bar and show the result
            isListOfJobSearchGovReady = true;
            mPresenter.requestGitHubSearchDataFromServer(selectedPosition, String.valueOf(currentPage), selectedLocation);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
        mPresenterGov.onDestroy();
    }

    //Swipe refresh to update list
    @Override
    public void onRefresh() {
        submitDataToProvider(mStringJobTitle, mStringJobLocation, currentPage);
    }

    //If No Data Found
    @Override
    public void onClickTryAgainButtonIfNoData() {
        onBackPressed();
    }
}
