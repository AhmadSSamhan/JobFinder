package com.android.jobfinder.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.jobfinder.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * This class use to :
 * - Display special view if no data coming from api.
 * - If the user Search/Filter on something and not return any data
 * - Use CallBack to take appropriate action in activity
 */
public class CustomViewNoData extends LinearLayout {
    @BindView(R.id.ly_main_content_socket)
    LinearLayout mMainLayout;
    @BindView(R.id.img_no_data)
    ImageView mImgLogo;
    @BindView(R.id.tv_title_socket_timeout)
    TextView mTitle;
    @BindView(R.id.tv_desc_socket_timeout)
    TextView mDesc;
    @BindView(R.id.btn_socket_timeout)
    Button mBtnTryAgain;
    @BindView(R.id.progress_load_data)
    ProgressBar mProgressLoadData;

    private CallBackClickTryAgainButton mCallBackTryAgain;

    public CustomViewNoData(Context context) {
        super(context);
        inflate(context);
    }

    public CustomViewNoData(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context);
    }

    public CustomViewNoData(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context);
    }

    private void inflate(Context context) {
        inflate(context, R.layout.content_no_data, this);
        ButterKnife.bind(this);
    }

    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            mTitle.setText(title);
        }
    }

    public void setDescription(String desc) {
        if (!TextUtils.isEmpty(desc)) {
            mDesc.setText(desc);
        }
    }

    @OnClick(R.id.btn_socket_timeout)
    void onClickTryAgain() {
        if (mCallBackTryAgain != null) {
            mCallBackTryAgain.onClickTryAgainButtonIfNoData();
        }
    }

    //Use CallBack to take appropriate action in Activity
    public void setCallBackIfNoData(CallBackClickTryAgainButton callBackNoData) {
        this.mCallBackTryAgain = callBackNoData;
    }

    public interface CallBackClickTryAgainButton {
        void onClickTryAgainButtonIfNoData();
    }
}
