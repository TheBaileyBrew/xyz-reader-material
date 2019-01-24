package com.example.xyzreader.database;

import com.example.xyzreader.database.models.Article;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ArticleDao {

    @Query("SELECT * FROM allarticles ORDER BY article_title")
    LiveData<List<Article>> getArticles();

    @Query("SELECT * FROM allarticles ORDER BY article_title")
    List<Article> getListArticles();

    @Query("SELECT * FROM allarticles WHERE article_id = :articleID")
    Article getArticleDetails(int articleID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertArticle(Article article);

    @Delete
    void deleteArticle(Article article);
}
