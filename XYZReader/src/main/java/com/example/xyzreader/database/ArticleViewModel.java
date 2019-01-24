package com.example.xyzreader.database;

import android.app.Application;

import com.example.xyzreader.database.models.Article;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ArticleViewModel extends AndroidViewModel {
    private final static String TAG = ArticleViewModel.class.getSimpleName();

    private ArticleRepository articleRepository;
    private LiveData<List<Article>> mAllArticles;

    public ArticleViewModel(@NonNull Application application) {
        super(application);
        articleRepository = new ArticleRepository(application);
        mAllArticles = articleRepository.getAllArticles();
    }

    public LiveData<List<Article>> getArticles() {
        return mAllArticles;
    }

}
