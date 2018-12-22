package com.android.jobfinder.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.jobfinder.my_interface.SearchComponentInfo;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GitHubJobSearchResult implements Parcelable, SearchComponentInfo {

    private String id;

    private String title;

    private String company_logo;

    private String location;

    private String description;

    private String company;

    private String how_to_apply;

    private String created_at;

    private String type;

    private String url;

    private String company_url;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.company_logo);
        dest.writeString(this.location);
        dest.writeString(this.description);
        dest.writeString(this.company);
        dest.writeString(this.how_to_apply);
        dest.writeString(this.created_at);
        dest.writeString(this.type);
        dest.writeString(this.url);
        dest.writeString(this.company_url);
    }

    public GitHubJobSearchResult() {
    }

    protected GitHubJobSearchResult(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.company_logo = in.readString();
        this.location = in.readString();
        this.description = in.readString();
        this.company = in.readString();
        this.how_to_apply = in.readString();
        this.created_at = in.readString();
        this.type = in.readString();
        this.url = in.readString();
        this.company_url = in.readString();
    }

    public static final Parcelable.Creator<GitHubJobSearchResult> CREATOR = new Parcelable.Creator<GitHubJobSearchResult>() {
        @Override
        public GitHubJobSearchResult createFromParcel(Parcel source) {
            return new GitHubJobSearchResult(source);
        }

        @Override
        public GitHubJobSearchResult[] newArray(int size) {
            return new GitHubJobSearchResult[size];
        }
    };

    @Override
    public String getCompanyLogo() {
        return getCompany_logo();
    }

    @Override
    public String getCompanyName() {
        return getCompany();
    }

    @Override
    public String getJobTitle() {
        return getTitle();
    }

    @Override
    public List<String> getListOfLocation() {
        return new ArrayList<>();
    }

    @Override
    public String getPostDate() {
        return getCreated_at();
    }

    @Override
    public String getDetail() {
        return getUrl();
    }
}
