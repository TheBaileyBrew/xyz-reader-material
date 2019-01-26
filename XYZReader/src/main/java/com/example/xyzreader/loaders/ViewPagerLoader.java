package com.example.xyzreader.loaders;

import android.os.AsyncTask;

import com.example.xyzreader.database.ArticleRepository;
import com.example.xyzreader.database.models.Article;
import com.example.xyzreader.ui.XYZReader;

import java.util.List;

public class ViewPagerLoader extends AsyncTask<String, Void, List<Article>> {
    @Override
    protected List<Article> doInBackground(String... strings) {
        ArticleRepository repo = new ArticleRepository(XYZReader.getContext());
        return repo.getArticles();
    }

    @Override
    protected void onPostExecute(List<Article> articles) {
        super.onPostExecute(articles);
    }
}
