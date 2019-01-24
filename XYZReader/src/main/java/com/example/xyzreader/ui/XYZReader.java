package com.example.xyzreader.ui;

import android.app.Application;

public class XYZReader extends Application {

    private static XYZReader mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static XYZReader getContext() {
        return mContext;
    }
}
