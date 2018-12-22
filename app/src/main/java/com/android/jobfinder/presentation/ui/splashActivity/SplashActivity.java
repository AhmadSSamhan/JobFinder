package com.android.jobfinder.presentation.ui.splashActivity;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import com.android.jobfinder.R;
import com.android.jobfinder.presentation.BaseActivity;
import com.android.jobfinder.presentation.ui.searchActivity.SearchActivity;

import static com.android.jobfinder.helper.ConstantApp.SPLASH_TIME_OUT;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splach);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(getContext(), SearchActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
