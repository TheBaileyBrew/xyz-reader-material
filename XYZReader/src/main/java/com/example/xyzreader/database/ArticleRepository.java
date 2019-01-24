package com.example.xyzreader.database;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.example.xyzreader.database.models.Article;

import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.LiveData;

public class ArticleRepository {
    private static final String TAG =ArticleRepository.class.getSimpleName();

    private ArticleDao mArticleDao;
    private LiveData<List<Article>> mAllArticles;
    private List<Article> mArticles;

    public ArticleRepository(Application application) {
        ArticleDatabase db = ArticleDatabase.getDatabase(application);
        mArticleDao = db.articleDao();
    }

    public LiveData<List<Article>> getAllArticles() {
        mAllArticles = mArticleDao.getArticles();
        return mAllArticles;
    }

    public List<Article> getArticles() {
        mArticles = mArticleDao.getListArticles();
        return mArticles;
    }

    public Article getSingleArticle(int id) {
        try {
            return new checkForDatabaseRecordAsyncTask(mArticleDao).execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException ie) {
            Log.e(TAG, "getSingleArticle: ", ie);
            return null;
        }
    }

    public void insertArticle(Article article) {
        new populateDatabaseArticlesAsyncTask(mArticleDao).execute(article);
    }

    public void removeArticle(Article article) {
        new removeDatabaseArticleAsyncTask(mArticleDao).execute(article);
    }


    //Add to Favorite
    private static class populateDatabaseArticlesAsyncTask extends AsyncTask<Article, Void, Void> {
        private ArticleDao mArticleDao;

        populateDatabaseArticlesAsyncTask(ArticleDao mArticleDao) {
            this.mArticleDao = mArticleDao;
        }


        @Override
        protected Void doInBackground(Article... articles) {
            Article currentArticle = articles[0];
            mArticleDao.insertArticle(currentArticle);
            return null;
        }
    }

    //Remove Favorite
    private static class removeDatabaseArticleAsyncTask extends AsyncTask<Article, Void, Void> {
        private ArticleDao mArticleDao;

        removeDatabaseArticleAsyncTask(ArticleDao mArticleDao) {
            this.mArticleDao = mArticleDao;
        }


        @Override
        protected Void doInBackground(Article... articles) {
            Article currentArticle = articles[0];
            Log.e(TAG, "doInBackground: movie to delete is: " + currentArticle.getArticleTitle() );
            mArticleDao.deleteArticle(currentArticle);
            Log.e(TAG, "doInBackground: movie deleted");
            return null;
        }
    }

    private static class checkForDatabaseRecordAsyncTask extends AsyncTask<Integer, Void, Article> {
        private ArticleDao mArticleDao;

        checkForDatabaseRecordAsyncTask(ArticleDao articleDao) {
            this.mArticleDao = articleDao;
        }

        @Override
        protected Article doInBackground(Integer... ints) {
            int currentInt = ints[0];
            mArticleDao.getArticleDetails(currentInt);
            return mArticleDao.getArticleDetails(currentInt);
        }
    }
}
