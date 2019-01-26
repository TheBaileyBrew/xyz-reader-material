package com.example.xyzreader.ui;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.loader.content.Loader;

import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.xyzreader.R;
import com.example.xyzreader.database.ArticleRepository;
import com.example.xyzreader.database.models.Article;
import com.example.xyzreader.loaders.ArticleLoader;
import com.squareup.picasso.Picasso;

/**
 * A fragment representing a single Article detail screen. This fragment is
 * either contained in a {@link ArticleListActivity} in two-pane mode (on
 * tablets) or a {@link ArticleDetailActivity} on handsets.
 */
public class ArticleDetailFragment extends Fragment {
    private static final String TAG = "ArticleDetailFragment";

    public static final String ARG_ITEM_ID = "item_id";
    private static final float PARALLAX_FACTOR = 1.25f;

    private Cursor mCursor;
    private long mItemId;
    private View mRootView;
    private List<Article> mAllArticles;
    private int mMutedColor = 0xFF333333;
    private ColorDrawable mStatusBarColorDrawable;
    private int position;

    private ImageView mPhotoView;
    private int mScrollY;
    private boolean mIsCard = false;
    private int mStatusBarFullOpacityBottom;

    private ArticleRepository repository;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss");
    // Use default locale format
    private SimpleDateFormat outputFormat = new SimpleDateFormat();
    // Most time functions can only handle 1902 - 2037
    private GregorianCalendar START_OF_EPOCH = new GregorianCalendar(2,1,1);

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticleDetailFragment() {
    }

    public static ArticleDetailFragment newInstance(long itemId) {
        Bundle arguments = new Bundle();
        arguments.putLong(ARG_ITEM_ID, itemId);
        Log.e(TAG, "newInstance: ITEM_ID " +itemId );
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAllArticles = ArticleDetailActivity.getArticles();
        Log.e(TAG, "onCreate: article size: " + mAllArticles.size() );

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItemId = getArguments().getLong(ARG_ITEM_ID);
            position = (int) mItemId;
        }

        mIsCard = getResources().getBoolean(R.bool.detail_is_card);
        mStatusBarFullOpacityBottom = getResources().getDimensionPixelSize(
                R.dimen.detail_card_top_margin);
        setHasOptionsMenu(true);
    }

    public ArticleDetailActivity getActivityCast() {
        return (ArticleDetailActivity) getActivity();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // In support library r8, calling initLoader for a fragment in a FragmentPagerAdapter in
        // the fragment's onCreate may cause the same LoaderManager to be dealt to multiple
        // fragments because their mIndex is -1 (haven't been added to the activity yet). Thus,
        // we do this in onActivityCreated.
        //getLoaderManager().initLoader(0, null, getActivityCast().getApplication());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_article_detail, container, false);
        mPhotoView = getActivityCast().findViewById(R.id.article_toolbar_image);
        mStatusBarColorDrawable = new ColorDrawable(0);

        bindViews();
        return mRootView;
    }



    static float progress(float v, float min, float max) {
        return constrain((v - min) / (max - min), 0, 1);
    }

    static float constrain(float val, float min, float max) {
        if (val < min) {
            return min;
        } else if (val > max) {
            return max;
        } else {
            return val;
        }
    }

    private Date parsePublishedDate() {
        try {
            String date = mAllArticles.get(position).getArticlePublishDate();
            return dateFormat.parse(date);
        } catch (ParseException ex) {
            Log.e(TAG, ex.getMessage());
            Log.i(TAG, "passing today's date");
            return new Date();
        }
    }

    private void bindViews() {
        if (mRootView == null) {
            return;
        }

        Toolbar titleView = getActivityCast().findViewById(R.id.article_toolbar);
        TextView bodyView = (TextView) mRootView.findViewById(R.id.article_body);

        bodyView.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "Rosario-Regular.ttf"));

        if (mAllArticles != null) {
            mRootView.setAlpha(0);
            mRootView.setVisibility(View.VISIBLE);
            mRootView.animate().alpha(1);
            getActivityCast().getSupportActionBar().setTitle(mAllArticles.get(position).getArticleTitle());

            Date publishedDate = parsePublishedDate();
            if (!publishedDate.before(START_OF_EPOCH.getTime())) {
                getActivityCast().getSupportActionBar().setSubtitle(Html.fromHtml(
                        DateUtils.getRelativeTimeSpanString(
                                publishedDate.getTime(),
                                System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                                DateUtils.FORMAT_ABBREV_ALL).toString()
                                + " by <font color='#ffffff'>"
                                + mAllArticles.get(position).getArticleAuthor()
                                + "</font>"));

            } else {
                // If date is before 1902, just show the string
                getActivityCast().getSupportActionBar().setSubtitle(Html.fromHtml(
                        outputFormat.format(publishedDate) + " by <font color='#ffffff'>"
                        + mAllArticles.get(position).getArticleAuthor()
                                + "</font>"));

            }
            bodyView.setText(Html.fromHtml(mAllArticles.get(position).getArticleBody().replaceAll("(\r\n|\n)", "<br />")));

            Picasso.get()
                    .load(mAllArticles.get(position).getArticlePhotoUrl())
                    .into(mPhotoView);

        } else {
            mRootView.setVisibility(View.GONE);
            getActivityCast().getSupportActionBar().setTitle("N/A");
            getActivityCast().getSupportActionBar().setSubtitle("N/A");
            bodyView.setText("N/A");
        }
    }

}
