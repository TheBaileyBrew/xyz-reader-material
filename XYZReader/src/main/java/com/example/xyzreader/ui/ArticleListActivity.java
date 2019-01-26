package com.example.xyzreader.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.xyzreader.R;
import com.example.xyzreader.database.ArticleRepository;
import com.example.xyzreader.database.ArticleViewModel;
import com.example.xyzreader.database.models.Article;
import com.example.xyzreader.loaders.ArticleLoader;
import com.example.xyzreader.ui.adapters.CollapsingToolbarListener;
import com.example.xyzreader.ui.adapters.RecyclerAdapter;
import com.example.xyzreader.ui.behavior.OnClickInterface;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.view.View.VISIBLE;

/**
 * An activity representing a list of Articles. This activity has different presentations for
 * handset and tablet-size devices. On handsets, the activity presents a list of items, which when
 * touched, lead to a {@link ArticleDetailActivity} representing item details. On tablets, the
 * activity presents a grid of items as cards.
 */
public class ArticleListActivity extends AppCompatActivity{

    private static final String TAG = ArticleListActivity.class.toString();
    private Toolbar mToolbar;
    private LiveData<List<Article>> allArticles;
    private List<Article> listArticles;
    private ImageView mToolbarImage;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter adapter;
    private AppBarLayout appBarLayout;
    private ArticleRepository articleRepository;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbarImage = findViewById(R.id.toolbar_image);
        appBarLayout = findViewById(R.id.app_toolbar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        setupToolbar();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        setupViewModelAndRecycler();


        if (savedInstanceState == null) {
            refresh();
        }
    }

    private void setupViewModelAndRecycler() {
        try {
            listArticles = new ArticleLoader().execute().get();
            Log.e(TAG, "setupViewModelAndRecycler: list size:" + listArticles.size() );
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*
        ArticleViewModel viewModel = ViewModelProviders.of(this).get(ArticleViewModel.class);
        viewModel.getArticles().observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articleSet) {
                if(articleSet.size()>0) {
                    adapter.updateArticles(articleSet);
                }
            }
        });
        */
        adapter = new RecyclerAdapter(this, listArticles, new OnClickInterface() {
            @Override
            public void onClick(int position, int articleID) {
                ArticleDetailFragment.newInstance(position);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL, false);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        appBarLayout.setExpanded(true);
        appBarLayout.addOnOffsetChangedListener(new CollapsingToolbarListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                switch (state) {
                    case EXPANDED:
                        mToolbarImage.setVisibility(VISIBLE);
                        break;
                    case COLLAPSED:
                        mToolbarImage.setVisibility(View.INVISIBLE);
                        break;
                    case IDLE:
                        mToolbarImage.setVisibility(VISIBLE);
                        break;
                }
            }
        });
    }

    private void refresh() {

        //get refreshed list
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    private boolean mIsRefreshing = false;



    private void updateRefreshingUI() {
        mSwipeRefreshLayout.setRefreshing(mIsRefreshing);
    }

}
