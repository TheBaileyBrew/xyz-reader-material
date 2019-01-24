package com.example.xyzreader.loaders;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.xyzreader.database.ArticleRepository;
import com.example.xyzreader.database.models.Article;
import com.example.xyzreader.remote.Config;
import com.example.xyzreader.ui.XYZReader;
import com.example.xyzreader.ui.adapters.RecyclerAdapter;
import com.example.xyzreader.ui.behavior.jsonParseUtils;

import java.net.URL;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

/**
 * Helper for loading a list of articles or a single article.
 */
public class ArticleLoader extends AsyncTask<String, Void, List<Article>> {
    private final static String TAG = ArticleLoader.class.getSimpleName();



    public ArticleLoader()  {

    }


    @Override
    protected List<Article> doInBackground(String... strings) {
        URL articleUrlRequest = Config.BASE_URL;
        try {
            String jsonArticleReturn = jsonParseUtils.makeHttpsRequest(articleUrlRequest);
            return jsonParseUtils.extractArticleData(jsonArticleReturn);
        } catch (Exception e) {
            Log.e(TAG, "doInBackground: can't make http request", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Article> articles) {
        super.onPostExecute(articles);
    }
}
