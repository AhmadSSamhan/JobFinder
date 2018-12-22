package com.android.jobfinder.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jobfinder.R;
import com.android.jobfinder.helper.ConstantApp;
import com.android.jobfinder.my_interface.SearchComponentInfo;
import com.android.jobfinder.presentation.ui.detailActivity.DetailActivity;
import com.android.jobfinder.utils.Utility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This adapter to display view item , coming from API
 */
public class SearchResultViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.img_item_logo)
    ImageView mImgLogo;
    @BindView(R.id.tv_title)
    TextView mTvTitleJob;
    @BindView(R.id.tv_company_name)
    TextView mTvTitleJobCompanyName;
    @BindView(R.id.tv_location_company)
    TextView mTvTitleCompanyLocation;
    @BindView(R.id.tv_date)
    TextView mTvDate;
    private Context mContext;
    private List<SearchComponentInfo> mList;

    public SearchResultViewHolder(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, DetailActivity.class);
                i.putExtra(ConstantApp.JOB_DETAILS, mList.get(getAdapterPosition()).getDetail());
                i.putExtra(ConstantApp.JOB_TITLE, mList.get(getAdapterPosition()).getJobTitle());
                mContext.startActivity(i);
            }
        });
    }

    public void setData(Context context, List<SearchComponentInfo> list, int position) {
        this.mContext = context;
        this.mList = list;
        bindData(position);
    }

    private void bindData(int position) {
        SearchComponentInfo componentInfo = mList.get(position);
        mTvTitleJob.setText(componentInfo.getJobTitle());
        mTvTitleJobCompanyName.setText(componentInfo.getCompanyName());
        Utility.setImageCircle(mContext, componentInfo.getCompanyLogo(),mImgLogo);
        if (TextUtils.isEmpty(componentInfo.getLocation())) {
            mTvTitleCompanyLocation.setText(getStringBasedOnLocation(componentInfo.getListOfLocation()));
        } else {
            mTvTitleCompanyLocation.setText(componentInfo.getLocation());
        }
        mTvDate.setText(componentInfo.getPostDate());
        String postDate = getPostDate(componentInfo.getPostDate());
        mTvDate.setText(postDate);
        if (TextUtils.isEmpty(componentInfo.getCompanyLogo())){
            mImgLogo.setImageResource(R.drawable.ic_load_image);
        }
    }

    private String getStringBasedOnLocation(List<String> listOfLocation) {
        StringBuilder stringBuilderOfLocation = new StringBuilder();
        for (String txtLocation : listOfLocation) {
            if (!TextUtils.isEmpty(txtLocation)) {
                stringBuilderOfLocation.append(txtLocation);
            }
        }
        return stringBuilderOfLocation.toString();
    }

    private String getPostDate(String postDate) {
        String dateFormat = Utility.getDateFormatConverter(ConstantApp.FORMAT_DATE_FROM_SEARCH_API_TYPE_ONE, postDate, ConstantApp.FORMAT_DATE_TO_USE);
        return TextUtils.isEmpty(dateFormat) ? Utility.getDateFormatConverter(ConstantApp.FORMAT_DATE_FROM_SEARCH_API_TYPE_TWO, postDate, ConstantApp.FORMAT_DATE_TO_USE) : dateFormat;
    }
}
