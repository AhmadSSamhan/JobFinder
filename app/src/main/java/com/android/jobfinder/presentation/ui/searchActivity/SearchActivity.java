package com.android.jobfinder.presentation.ui.searchActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.widget.Button;
import android.widget.EditText;

import com.android.jobfinder.BuildConfig;
import com.android.jobfinder.R;
import com.android.jobfinder.helper.ConstantApp;
import com.android.jobfinder.presentation.BaseActivity;
import com.android.jobfinder.presentation.ui.mainActivity.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * This Activity allow the users to search the jobs by :
 * - Enter Job title
 * - Enter Job location
 */
public class SearchActivity extends BaseActivity {

    @BindView(R.id.btn_find_jobs)
    Button mButtonFindJobs;
    @BindView(R.id.ed_search_by_title)
    EditText mEdSearchByTitle;
    @BindView(R.id.ed_search_by_location)
    EditText mEdSearchByLocation;
    @BindView(R.id.cons_layout_search)
    ConstraintLayout mLayoutRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_find_jobs)
    public void onClickBtnFindJobs() {
        runFadeInAnimationAndStartActivity();
    }

    /**
     * Submit search words if exist to MainActivity
     * Use animation to open MainActivity
     */
    private void runFadeInAnimationAndStartActivity() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra(ConstantApp.JOB_TITLE, mEdSearchByTitle.getText().toString());
        intent.putExtra(ConstantApp.JOB_LOCATION, mEdSearchByLocation.getText().toString());
        startActivity(intent);
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * Clear search words if found
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEdSearchByTitle.getText().clear();
        mEdSearchByLocation.getText().clear();
    }
}
