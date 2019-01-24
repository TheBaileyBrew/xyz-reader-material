package com.example.xyzreader.ui.adapters;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xyzreader.R;
import com.example.xyzreader.database.ArticleRepository;
import com.example.xyzreader.database.models.Article;
import com.example.xyzreader.ui.ImageLoaderHelper;
import com.example.xyzreader.ui.XYZReader;
import com.example.xyzreader.ui.behavior.DynamicHeightImageView;
import com.example.xyzreader.ui.behavior.RecyclerDiffCallback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static String TAG = RecyclerAdapter.class.getSimpleName();

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss");
    // Use default locale format
    private SimpleDateFormat outputFormat = new SimpleDateFormat();
    // Most time functions can only handle 1902 - 2037
    private GregorianCalendar START_OF_EPOCH = new GregorianCalendar(2,1,1);

    private List<Article> mArticles;
    private Context mContext;

    public RecyclerAdapter(Context context, List<Article> allArticles) {
        this.mArticles = allArticles;
        mContext = context;

    }

    class ViewHolderRight extends RecyclerView.ViewHolder {
        public DynamicHeightImageView thumbnailView;
        public TextView titleView;
        public TextView subtitleView;

        public ViewHolderRight(View view) {
            super(view);

            thumbnailView = view.findViewById(R.id.thumbnail);
            thumbnailView.setImageHeight();
            titleView = (TextView) view.findViewById(R.id.article_title);
            subtitleView = (TextView) view.findViewById(R.id.article_subtitle);

        }
    }

    class ViewHolderLeft extends RecyclerView.ViewHolder {
        public DynamicHeightImageView thumbnailView;
        public TextView titleView;
        public TextView subtitleView;

        public ViewHolderLeft(View view) {
            super(view);
            thumbnailView = view.findViewById(R.id.thumbnail);
            thumbnailView.setImageHeight();
            titleView = (TextView) view.findViewById(R.id.article_title);
            subtitleView = (TextView) view.findViewById(R.id.article_subtitle);
        }
    }

    @Override
    public int getItemCount() {
        if (mArticles == null) {
            return 0;
        } else {
            Log.e(TAG, "getItemCount: size" + mArticles.size() );
            return mArticles.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 * 2;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
            default:
                ViewHolderLeft viewHolderLeft = (ViewHolderLeft)holder;
                viewHolderLeft.titleView.setText(mArticles.get(position).getArticleTitle());
                Log.e(TAG, "onBindViewHolder: title: " + mArticles.get(position).getArticleTitle() );
                Date publishedDate = parsePublishedDate(position);
                if (!publishedDate.before(START_OF_EPOCH.getTime())) {
                    viewHolderLeft.subtitleView.setText(Html.fromHtml(
                            DateUtils.getRelativeTimeSpanString(
                                    publishedDate.getTime(),
                                    System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                                    DateUtils.FORMAT_ABBREV_ALL).toString()
                                    + "<br/>" + " by "
                                    + mArticles.get(position).getArticleAuthor()));
                } else {
                    viewHolderLeft.subtitleView.setText(Html.fromHtml(
                            outputFormat.format(publishedDate)
                                    + "<br/>" + " by "
                                    + mArticles.get(position).getArticleAuthor()));
                }
                Picasso.get()
                        .load(mArticles.get(position).getArticlePhotoUrl())
                        .into(viewHolderLeft.thumbnailView);
                //holder.thumbnailView.setAspectRation(mArticles.get(position).getArticleAspectRatio());
                Log.e(TAG, "onBindViewHolder: subTitle: " + viewHolderLeft.subtitleView.getText().toString() );
            break;
            case 2:
                ViewHolderRight viewHolderRight = (ViewHolderRight)holder;
                viewHolderRight.titleView.setText(mArticles.get(position).getArticleTitle());
                Log.e(TAG, "onBindViewHolder: title: " + mArticles.get(position).getArticleTitle() );
                Date publishDate = parsePublishedDate(position);
                if (!publishDate.before(START_OF_EPOCH.getTime())) {
                    viewHolderRight.subtitleView.setText(Html.fromHtml(
                            DateUtils.getRelativeTimeSpanString(
                                    publishDate.getTime(),
                                    System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                                    DateUtils.FORMAT_ABBREV_ALL).toString()
                                    + "<br/>" + " by "
                                    + mArticles.get(position).getArticleAuthor()));
                } else {
                    viewHolderRight.subtitleView.setText(Html.fromHtml(
                            outputFormat.format(publishDate)
                                    + "<br/>" + " by "
                                    + mArticles.get(position).getArticleAuthor()));
                }
                Picasso.get()
                        .load(mArticles.get(position).getArticlePhotoUrl())
                        .into(viewHolderRight.thumbnailView);
                //holder.thumbnailView.setAspectRation(mArticles.get(position).getArticleAspectRatio());
                Log.e(TAG, "onBindViewHolder: subTitle: " + viewHolderRight.subtitleView.getText().toString() );
        }


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch(viewType) {
            case 0:
            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_article, parent, false);
                final ViewHolderLeft vhl = new ViewHolderLeft(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Figure out the intent call to open new activity based on item selected
                        //mContext.startActivity(new Intent(Intent.ACTION_VIEW,
                        //        ItemsContract.Items.buildItemUri(getItemId(vh.getAdapterPosition()))));
                    }
                });
                return vhl;
            case 2:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_article_alternate, parent, false);
                final ViewHolderRight vhr = new ViewHolderRight(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Figure out the intent call to open new activity based on item selected
                        //mContext.startActivity(new Intent(Intent.ACTION_VIEW,
                        //        ItemsContract.Items.buildItemUri(getItemId(vh.getAdapterPosition()))));
                    }
                });
                return vhr;
        }
    }

    public void updateArticles(List<Article> articles) {
        final RecyclerDiffCallback diffCallback = new RecyclerDiffCallback(this.mArticles, articles);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        if (this.mArticles != null) {
            this.mArticles.clear();
            this.mArticles.addAll(articles);
        }

        if (articles == null) {
            assert this.mArticles != null;
            this.mArticles.clear();
            notifyDataSetChanged();
        }
        diffResult.dispatchUpdatesTo(this);
    }



    private Date parsePublishedDate(int position) {
        try {
            String date = mArticles.get(position).getArticlePublishDate();
            return dateFormat.parse(date);
        } catch (ParseException ex) {
            Log.e(TAG, ex.getMessage());
            Log.i(TAG, "passing today's date");
            return new Date();
        }
    }




}