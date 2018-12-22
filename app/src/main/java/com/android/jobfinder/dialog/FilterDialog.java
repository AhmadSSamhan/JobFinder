package com.android.jobfinder.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.android.jobfinder.R;
import com.android.jobfinder.helper.ConstantApp;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Display filter dialog
 * Return data selected by Callback to activity
 */
public class FilterDialog extends BottomSheetDialog {

    //Getting the instance of AutoCompleteTextView
    @BindView(R.id.auto_complete_position)
    AutoCompleteTextView mAutoCompletePosition;
    @BindView(R.id.auto_complete_location)
    AutoCompleteTextView mAutoCompleteLocation;

    @BindView(R.id.btn_github_provider)
    Button mButtonGithubProvider;
    @BindView(R.id.btn_search_gov_provider)
    Button mButtonJobGovProvider;
    private List<String> mListPosition;
    private List<String> mListLocation;
    private CallBackFilter mCallBackFilter;
    private String mStrOfPosition = "";
    private String mStrOfLocation = "";
    private String mTypeProviderSelected = "";

    public FilterDialog(@NonNull Context context) {
        super(context);
    }

    public FilterDialog setListOfPosition(List<String> listOfPosition) {
        this.mListPosition = listOfPosition;
        return this;
    }

    public FilterDialog setListOfLocation(List<String> listLocation) {
        this.mListLocation = listLocation;
        return this;
    }

    public FilterDialog setCallBack(CallBackFilter callBack) {
        this.mCallBackFilter = callBack;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_content_filter);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        ButterKnife.bind(this);
        setAutoCompleteWithList(mAutoCompletePosition, mListPosition);
        setAutoCompleteWithList(mAutoCompleteLocation, mListLocation);

        mAutoCompletePosition.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get selected word
                mStrOfPosition = (String) parent.getItemAtPosition(position);
            }
        });
        mAutoCompleteLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get selected word
                mStrOfLocation = (String) parent.getItemAtPosition(position);
            }
        });
    }

    @OnClick(R.id.btn_submit_filter)
    public void onClickBtnFilter() {
        if (mCallBackFilter != null) {
            //Get selected word
            mStrOfPosition = TextUtils.isEmpty(mStrOfPosition) ? mAutoCompletePosition.getText().toString() : mStrOfPosition;
            mStrOfLocation = TextUtils.isEmpty(mStrOfLocation) ? mAutoCompleteLocation.getText().toString() : mStrOfLocation;
            if (TextUtils.isEmpty(mTypeProviderSelected)) {
                Toast.makeText(getContext(), R.string.txt_select_provider, Toast.LENGTH_LONG).show();
                return;
            }
            mCallBackFilter.onCallBackFilterData(mTypeProviderSelected, mStrOfPosition, mStrOfLocation);
        }
        hide();
    }

    @OnClick(R.id.btn_github_provider)
    public void onClickGitHubButton() {
        mTypeProviderSelected = ConstantApp.PROVIDER_GITHUB;
        activeBtnSelected(mButtonGithubProvider);
        unActiveBtnSelected(mButtonJobGovProvider);
    }

    @OnClick(R.id.btn_search_gov_provider)
    public void onClickJobGovButton() {
        mTypeProviderSelected = ConstantApp.PROVIDER_SEARCH_GOV;
        activeBtnSelected(mButtonJobGovProvider);
        unActiveBtnSelected(mButtonGithubProvider);
    }

    private void activeBtnSelected(Button button) {
        button.setBackgroundResource(R.drawable.gradient_background_btn_provider_selected);
        button.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
    }

    private void unActiveBtnSelected(Button button) {
        button.setBackgroundResource(R.drawable.gradient_background_btn_provider_default);
        button.setTextColor(ContextCompat.getColor(getContext(), R.color.grey_500));
    }

    private void setAutoCompleteWithList(AutoCompleteTextView autoCompleteWithList, List<String> list) {
        //Creating the instance of ArrayAdapter containing list of position/location names
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_item, list);
        //setting the adapter data into the AutoCompleteTextView
        autoCompleteWithList.setAdapter(adapter);
        //will start working from first character
        autoCompleteWithList.setThreshold(1);
    }

    public interface CallBackFilter {
        void onCallBackFilterData(String typeProviderSelected, String selectedPosition, String selectedLocation);
    }

}
