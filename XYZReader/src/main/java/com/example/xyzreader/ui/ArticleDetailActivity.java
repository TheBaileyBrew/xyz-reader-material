package com.example.xyzreader.ui;

import androidx.annotation.Nullable;
import androidx.loader.content.Loader;

import android.app.LoaderManager;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.xyzreader.R;
import com.example.xyzreader.database.models.Article;
import com.example.xyzreader.loaders.ArticleLoader;
import com.example.xyzreader.ui.adapters.CollapsingToolbarListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static android.view.View.VISIBLE;

/**
 * An activity representing a single Article detail screen, letting you swipe between articles.
 */
public class ArticleDetailActivity extends AppCompatActivity {

    private Cursor mCursor;
    private long mStartId;

    private long mSelectedItemId;
    private int mSelectedItemUpButtonFloor = Integer.MAX_VALUE;
    private int mTopInset;

    private ViewPager mPager;
    private List<Article> mAllArticles;
    private MyPagerAdapter mPagerAdapter;
    private Toolbar mToolbar;
    private AppBarLayout appBarLayout;
    private ImageView mToolbarImage;

    private FloatingActionButton floatingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_detail_activity);

        getLoaderManager().initLoader(0, null, (LoaderManager.LoaderCallbacks<Object>) this);
        setupToolbarDetails();
        floatingButton = findViewById(R.id.floating_favorite_button);

        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);

        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }

            @Override
            public void onPageSelected(int position) {
                if (mCursor != null) {
                    mCursor.moveToPosition(position);
                }
                //mSelectedItemId = mCursor.getLong(ArticleLoader.Query._ID);
            }
        });



        if (savedInstanceState == null) {

        }
    }

    private void setupToolbarDetails() {
        mToolbar = findViewById(R.id.article_toolbar);
        appBarLayout = findViewById(R.id.article_app_toolbar);
        mToolbarImage = findViewById(R.id.article_toolbar_image);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
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







    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            mCursor.moveToPosition(position);
            return ArticleDetailFragment.newInstance(mAllArticles.get(position).getArticleID());
        }

        @Override
        public int getCount() {
            return (mAllArticles != null) ? mAllArticles.size() : 0;
        }
    }
}
