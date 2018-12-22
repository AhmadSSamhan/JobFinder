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
public class JobSearchGov implements Parcelable, SearchComponentInfo {

    private String id;

    private String end_date;

    private List<String> locations;

    private String minimum;

    private String maximum;

    private String position_title;

    private String start_date;

    private String url;

    private String rate_interval_code;

    private String organization_name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.end_date);
        dest.writeStringList(this.locations);
        dest.writeString(this.minimum);
        dest.writeString(this.maximum);
        dest.writeString(this.position_title);
        dest.writeString(this.start_date);
        dest.writeString(this.url);
        dest.writeString(this.rate_interval_code);
        dest.writeString(this.organization_name);
    }

    public JobSearchGov() {
    }

    protected JobSearchGov(Parcel in) {
        this.id = in.readString();
        this.end_date = in.readString();
        this.locations = in.createStringArrayList();
        this.minimum = in.readString();
        this.maximum = in.readString();
        this.position_title = in.readString();
        this.start_date = in.readString();
        this.url = in.readString();
        this.rate_interval_code = in.readString();
        this.organization_name = in.readString();
    }

    public static final Parcelable.Creator<JobSearchGov> CREATOR = new Parcelable.Creator<JobSearchGov>() {
        @Override
        public JobSearchGov createFromParcel(Parcel source) {
            return new JobSearchGov(source);
        }

        @Override
        public JobSearchGov[] newArray(int size) {
            return new JobSearchGov[size];
        }
    };

    @Override
    public String getCompanyLogo() {
        return "";
    }

    @Override
    public String getCompanyName() {
        return getOrganization_name();
    }

    @Override
    public String getJobTitle() {
        return getPosition_title();
    }

    @Override
    public String getLocation() {
        return "";
    }

    @Override
    public List<String> getListOfLocation() {
        return checkListOfLocation();
    }

    private List<String> checkListOfLocation() {
        if (getLocations() == null) {
            return new ArrayList<>();
        }
        return getLocations();
    }

    @Override
    public String getPostDate() {
        return getStart_date();
    }

    @Override
    public String getDetail() {
        return getUrl();
    }
}
