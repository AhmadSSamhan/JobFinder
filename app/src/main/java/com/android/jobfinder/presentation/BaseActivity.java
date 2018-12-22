package com.android.jobfinder.presentation;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

/**
 * Created by A.Samhan on 2018-12-19.
 */
public class BaseActivity extends AppCompatActivity {
    private Context mContext = this;
    private Toolbar toolbar;

    public void setContext(Context context) {
        this.mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContext(this);
    }

    public void createToolBarWithBackBtn(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // toolbar.setNavigationIcon(R.drawable.ic_arrow_back_light_blue);
    }

    public void setToolbarTitle(String title) {
        title = TextUtils.isEmpty(title) ? "" : title;
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onNavigateUp() {
        finish();
        return super.onNavigateUp();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
