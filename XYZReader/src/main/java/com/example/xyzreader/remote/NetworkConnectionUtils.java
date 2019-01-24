package com.example.xyzreader.remote;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Objects;

public class NetworkConnectionUtils {

    public static boolean checkNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        boolean hasNetwork = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            hasNetwork = true;
        }
        return hasNetwork;
    }
}
