package com.android.jobfinder.presentation.ui.detailActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.jobfinder.R;
import com.android.jobfinder.helper.ConstantApp;
import com.android.jobfinder.presentation.BaseActivity;
import com.android.jobfinder.utils.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This Activity to display url detail submitted from Main activity
 * By specific job.
 * Display data by web view.
 */
public class DetailActivity extends BaseActivity {
    private Context mContext = this;
    private String mBookUrl;
    @BindView(R.id.web_view)
    WebView mWebView;
    private String mItemTitle;
    private ProgressDialog prDialog;
    private MenuItem mMenuItemShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_web_view);
        ButterKnife.bind(this);
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        invalidateOptionsMenu();
        getExtraData();
        setWebViewType();
        setToolbarTitle(mItemTitle);
        setProgressPar();
    }

    //Get Title and Url from Extra data coming from selected job
    private void getExtraData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            try {
                mBookUrl = bundle.getString(ConstantApp.JOB_DETAILS);
                mBookUrl = Utility.checkUrl(mContext, mBookUrl);
                mItemTitle = bundle.getString(ConstantApp.JOB_TITLE);
            } catch (Exception e) {
                e.getCause();
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //Adjust all appropriate settings until the content is displayed as desired
    @SuppressLint("SetJavaScriptEnabled")
    private void setWebViewType() {
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setEnableSmoothTransition(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.setWebViewClient(new MyBrowser());
        mWebView.loadUrl(mBookUrl);
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
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mWebView.clearHistory();
        mWebView.destroy();
        super.onDestroy();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refresh_page, menu);
        mMenuItemShare = menu.findItem(R.id.action_refresh_page);
        mMenuItemShare.setVisible(true);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh_page:
                String url = mWebView.getUrl();
                mWebView.setInitialScale(1);
                LoadWebViewUrl(url);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void LoadWebViewUrl(String url) {
        if (Utility.isOnlineWithInternet(mContext)) {
            mWebView.loadUrl(url);
        } else {
            mMenuItemShare.setVisible(true);
            Toast.makeText(mContext, getString(R.string.txt_no_internet), Toast.LENGTH_LONG).show();
        }
    }

    private void setProgressPar() {
        prDialog = new ProgressDialog(mContext);
        prDialog.setMessage(getString(R.string.txt_wait_to_load));
        prDialog.setCancelable(true);
    }

    class MyBrowser extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            prDialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (prDialog != null) {
                prDialog.dismiss();
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            ifNoDataWebView();
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            ifNoDataWebView();

        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            ifNoDataWebView();
        }

    }

    private void ifNoDataWebView() {
        if (prDialog != null) {
            prDialog.dismiss();
        }
    }
}
