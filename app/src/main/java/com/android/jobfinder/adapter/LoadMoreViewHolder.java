package com.android.jobfinder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.android.jobfinder.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LoadMoreViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.progressBar)
    ProgressBar mProgressLoadMore;
    private Context mContext;

    public LoadMoreViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setData(Context context) {
        this.mContext = context;
        bindData();
    }

    private void bindData() {
        mProgressLoadMore.setIndeterminate(true);
    }
}
