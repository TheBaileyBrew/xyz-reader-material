package com.example.xyzreader.remote;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.example.xyzreader.database.ArticleRepository;
import com.example.xyzreader.database.models.Article;
import com.example.xyzreader.ui.XYZReader;
import com.example.xyzreader.ui.behavior.jsonParseUtils;

import java.net.URL;
import java.util.List;

import javax.xml.transform.Result;

public class MyIntentService extends IntentService {
    private final static String TAG = MyIntentService.class.getSimpleName();

    private List<Article> allArticles;
    ArticleRepository repo;

    private enum Actions {
        LOAD, GET, REMOVE
    }
    private enum PARAM {
        RESULT_RECIEVER
    }

    public MyIntentService(String name) {
        super(name);
    }

    public MyIntentService() {
        super(MyIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ResultReceiver resultReceiver = intent.getParcelableExtra(PARAM.RESULT_RECIEVER.name());
        repo = new ArticleRepository(XYZReader.getContext());


        if (intent != null) {
            final String action = intent.getAction();
            if (Actions.LOAD.name().equals(action)) {
                handleLoadDatabase(resultReceiver);
            } else if (Actions.GET.name().equals(action)) {
                handleGetDatabase(resultReceiver);
            } else if (Actions.REMOVE.name().equals(action)) {
                handleRemoveDatabase(resultReceiver);
            }
        }
    }

    private void handleLoadDatabase(ResultReceiver resultReceiver) {
        Bundle bundle = new Bundle();
        int code;

        //Start Download from JSON


        URL articleUrlRequest = Config.BASE_URL;
        try {
            String jsonArticleReturn = jsonParseUtils.makeHttpsRequest(articleUrlRequest);
            allArticles = jsonParseUtils.extractArticleData(jsonArticleReturn);
        } catch (Exception e) {
            Log.e(TAG, "doInBackground: can't make http request", e);
        }

        //Add data to ROOM
        if (allArticles == null) {
            //do something if no return
            Log.e(TAG, "onHandleIntent: all articles is null");
        } else {
            for (int a = 0; a <= allArticles.size(); a++)
                repo.insertArticle(allArticles.get(a));
        }

        //Log that all articles have been loaded into ROOM DB
        Log.e(TAG, "onHandleIntent: Room loaded: " + allArticles.size());
    }
}
