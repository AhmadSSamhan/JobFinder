package com.android.jobfinder.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.jobfinder.R;
import com.android.jobfinder.my_interface.SearchComponentInfo;
import com.android.jobfinder.presentation.ui.detailActivity.DetailActivity;

import java.util.List;

/**
 * Handel Search result fetching from APi using custom view
 * Use type LOADING_ITEM load more to show progressBar
 * Use type of ITEM_SEARCH_RESULT to add item in list
 */
public class SearchResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<SearchComponentInfo> mList;
    // View Types
    private static final int ITEM_SEARCH_RESULT = 0;
    private static final int LOADING_ITEM = 1;
    private boolean isLoadingAdded = false;

    public SearchResultAdapter(Context context, List<SearchComponentInfo> searchComponentInfos) {
        this.mContext = context;
        this.mList = searchComponentInfos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder myViewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ITEM_SEARCH_RESULT:
                View viewItem = inflater.inflate(R.layout.adapter_search_row, parent, false);
                myViewHolder = new SearchResultViewHolder(viewItem);
                break;
            case LOADING_ITEM:
                View viewLoading = inflater.inflate(R.layout.row_progress_load_more, parent, false);
                myViewHolder = new LoadMoreViewHolder(viewLoading);
                break;
        }
        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ITEM_SEARCH_RESULT:
                ((SearchResultViewHolder) holder).setData(mContext, mList, position);
                break;
            case LOADING_ITEM:
                ((LoadMoreViewHolder) holder).setData(mContext);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * To insert special view 'Load More search' inside the list
     *
     * @param position : get current position
     */
    @Override
    public int getItemViewType(int position) {
        return (position == mList.size() - 1 && isLoadingAdded) ? LOADING_ITEM : ITEM_SEARCH_RESULT;
    }

}
