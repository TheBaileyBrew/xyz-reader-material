package com.example.xyzreader.database.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "allarticles")
public class Article implements Parcelable {

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "article_id")
    private int articleID;

    @ColumnInfo(name = "server_id")
    private String serverID;

    @ColumnInfo(name = "article_title")
    private String articleTitle;

    @ColumnInfo(name = "article_author")
    private String articleAuthor;

    @ColumnInfo(name = "article_body")
    private String articleBody;

    @ColumnInfo(name = "article_thumbnail")
    private String articleThumbUrl;

    @ColumnInfo(name = "article_photo")
    private String articlePhotoUrl;

    @ColumnInfo(name = "article_aspect_ratio")
    private float articleAspectRatio;

    @ColumnInfo(name = "article_date")
    private String articlePublishDate;

    public Article() {}

    private Article(Parcel in) {
        articleID = in.readInt();
        serverID = in.readString();
        articleTitle = in.readString();
        articleAuthor = in.readString();
        articleBody = in.readString();
        articleThumbUrl = in.readString();
        articlePhotoUrl = in.readString();
        articleAspectRatio = in.readFloat();
        articlePublishDate = in.readString();
    }

    public int getArticleID() {
        return articleID;
    }

    public void setArticleID(int articleID) {
        this.articleID = articleID;
    }

    public String getServerID() {
        return serverID;
    }

    public void setServerID(String serverID) {
        this.serverID = serverID;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleAuthor() {
        return articleAuthor;
    }

    public void setArticleAuthor(String articleAuthor) {
        this.articleAuthor = articleAuthor;
    }

    public String getArticleBody() {
        return articleBody;
    }

    public void setArticleBody(String articleBody) {
        this.articleBody = articleBody;
    }

    public String getArticleThumbUrl() {
        return articleThumbUrl;
    }

    public void setArticleThumbUrl(String articleThumbUrl) {
        this.articleThumbUrl = articleThumbUrl;
    }

    public String getArticlePhotoUrl() {
        return articlePhotoUrl;
    }

    public void setArticlePhotoUrl(String articlePhotoUrl) {
        this.articlePhotoUrl = articlePhotoUrl;
    }

    public float getArticleAspectRatio() {
        return articleAspectRatio;
    }

    public void setArticleAspectRatio(float articleAspectRatio) {
        this.articleAspectRatio = articleAspectRatio;
    }

    public String getArticlePublishDate() {
        return articlePublishDate;
    }

    public void setArticlePublishDate(String articlePublishDate) {
        this.articlePublishDate = articlePublishDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(articleID);
        parcel.writeString(serverID);
        parcel.writeString(articleTitle);
        parcel.writeString(articleAuthor);
        parcel.writeString(articleBody);
        parcel.writeString(articleThumbUrl);
        parcel.writeString(articlePhotoUrl);
        parcel.writeFloat(articleAspectRatio);
        parcel.writeString(articlePublishDate);
    }
}
