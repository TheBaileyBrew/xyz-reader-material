package com.example.xyzreader.remote;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import static com.example.xyzreader.remote.TheFallOfConstantinople.PARAM_EXCEPTION;
import static com.example.xyzreader.remote.TheFallOfConstantinople.PARAM_RESULT;
import static com.example.xyzreader.remote.TheFallOfConstantinople.RESULT_CODE_OK;

public class ArticleResultRetriever<T> extends ResultReceiver {

    private ResultReceiverCallBack mReciever;

    public ArticleResultRetriever(Handler handler) {
        super(handler);
    }

    public void setReceiver(ResultReceiverCallBack<T> receiver) {
        mReciever = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReciever != null) {
            if (resultCode == RESULT_CODE_OK) {
                mReciever.onSuccess(resultData.getSerializable(PARAM_RESULT));
            } else {
                mReciever.onError((Exception) resultData.getSerializable(PARAM_EXCEPTION));
            }
        }
    }

    public interface ResultReceiverCallBack<T>{
        public void onSuccess(T data);
        public void onError(Exception exception);
    }
}
