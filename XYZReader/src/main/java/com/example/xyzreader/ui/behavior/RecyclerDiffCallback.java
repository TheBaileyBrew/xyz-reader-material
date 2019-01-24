package com.example.xyzreader.ui.behavior;

import com.example.xyzreader.database.models.Article;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

public class RecyclerDiffCallback extends DiffUtil.Callback {

    private final List<Article> mOriginalArticles;
    private final List<Article> mNewArticles;

    public RecyclerDiffCallback(List<Article> original, List<Article> newArticles) {
        this.mOriginalArticles = original;
        this.mNewArticles = newArticles;
    }

    @Override
    public int getOldListSize() {
        return mOriginalArticles == null ? 0 : mOriginalArticles.size();
    }

    @Override
    public int getNewListSize() {
        return mNewArticles == null ? 0 : mNewArticles.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOriginalArticles.get(oldItemPosition).getArticleID()
                == mNewArticles.get(newItemPosition).getArticleID();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final Article oldArticle = mOriginalArticles.get(oldItemPosition);
        final Article newArticle = mNewArticles.get(newItemPosition);
        return oldArticle.getArticleTitle() == newArticle.getArticleTitle();
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }

}
