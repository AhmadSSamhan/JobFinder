package com.android.jobfinder.my_interface;

import java.util.List;

/**
 * This interface use to merge 2 provider on one data
 */
public interface SearchComponentInfo {
    String getCompanyLogo();

    String getCompanyName();

    String getJobTitle();

    String getLocation();

    List<String> getListOfLocation();

    String getPostDate();

    String getDetail();
}
