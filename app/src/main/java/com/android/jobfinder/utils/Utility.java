package com.android.jobfinder.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.android.jobfinder.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by A.Samhan.
 */

public class Utility {


    // Check is connected to internet or not
    public static boolean isOnlineWithInternet(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }


    //Double check to url if success or not
    public static String checkUrl(Context context, String urlPath) {
        if (urlPath == null || TextUtils.isEmpty(urlPath)) {
            return urlPath;
        }
        if (!urlPath.startsWith("http://") && !urlPath.startsWith("https://")) {
            urlPath = "http://" + urlPath;
        }
        return urlPath;
    }

    //Use Circle Image on adapter with fit size of image special on different devices
    public static void setImageCircle(final Context context, final String imagePath, final ImageView imgItem) {
        if (imagePath != null && !imagePath.isEmpty()) {
            imgItem.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    imgItem.getViewTreeObserver().removeOnPreDrawListener(this);
                    int width = imgItem.getMeasuredWidth();
                    int height = imgItem.getMeasuredHeight();
                    if (context != null) {
                        setGlideCircleImage(context, imagePath, width, height, imgItem);
                    }
                    return true;
                }
            });
        }
    }

    //Use Glide lib to load images and Show images in a circular way
    public static void setGlideCircleImage(final Context context, String imgPath, int width, int height, final ImageView imageView) {
        Glide.with(context)
                .asBitmap()
                .load(imgPath)
                .apply(new RequestOptions()
                        .override(width, height)
                        .placeholder(R.drawable.ic_load_image) // Display default image if not exist
                        .error(R.drawable.ic_broken_image_indigo_300_24dp) // If something wrong
                        .centerCrop())
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    //Convert dynamic format date to specific format
    public static String getDateFormatConverter(final String inputFormat, String inputYourFormat, final String outputFormat) {
        String strFormatConverter = "";
        try {
            strFormatConverter = new SimpleDateFormat(outputFormat, Locale.getDefault()).format(new SimpleDateFormat(inputFormat, Locale.getDefault()).parse(inputYourFormat));
        } catch (Exception e) {
            e.getMessage();
            strFormatConverter = "";
        }
        return strFormatConverter;
    }

}
