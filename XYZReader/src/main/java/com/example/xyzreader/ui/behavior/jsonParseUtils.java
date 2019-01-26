package com.example.xyzreader.ui.behavior;

import android.text.TextUtils;
import android.util.Log;

import com.example.xyzreader.database.ArticleRepository;
import com.example.xyzreader.database.models.Article;
import com.example.xyzreader.remote.Config;
import com.example.xyzreader.ui.XYZReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static com.example.xyzreader.remote.TheFallOfConstantinople.ARTICLE_ASPECT_RATIO;
import static com.example.xyzreader.remote.TheFallOfConstantinople.ARTICLE_AUTHOR;
import static com.example.xyzreader.remote.TheFallOfConstantinople.ARTICLE_BODY;
import static com.example.xyzreader.remote.TheFallOfConstantinople.ARTICLE_ID;
import static com.example.xyzreader.remote.TheFallOfConstantinople.ARTICLE_PHOTO_URL;
import static com.example.xyzreader.remote.TheFallOfConstantinople.ARTICLE_PUBLISHED_DATE;
import static com.example.xyzreader.remote.TheFallOfConstantinople.ARTICLE_THUMB;
import static com.example.xyzreader.remote.TheFallOfConstantinople.ARTICLE_TITLE;

public class jsonParseUtils {
    private final static String TAG = jsonParseUtils.class.getSimpleName();

    private static ArticleRepository repo = new ArticleRepository(XYZReader.getContext());

    private jsonParseUtils() {}

    public static String makeHttpsRequest(URL url) throws IOException {
        String jsonResponse = "";
        //Check for null URL
        if (url == null) {
            return jsonResponse;
        }
        HttpsURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            Log.e(TAG, "Full url is:" + String.valueOf(url));
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setReadTimeout(12000);
            urlConnection.setConnectTimeout(20000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            //If successful request
            if(urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse= readFromStream(inputStream);
            } else {
                Log.e(TAG, "makeHttpsRequest: Error Code: " +urlConnection.getResponseCode());
            }
        } catch (IOException ioe) {
            Log.e(TAG, "makeHttpsRequest: Cound not retrieve JSON result", ioe);
        } finally {
            if (urlConnection !=null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    public static List<Article> extractArticleData(String jsonData) {
        int articleID;
        String serverID;
        String articleTitle;
        String articleAuthor;
        String articleBody;
        String articleThumbUrl;
        String articlePhotoUrl;
        Float articleAspectRatio;
        String articlePublishDate;

        //Check for NULL jsonData
        if (TextUtils.isEmpty(jsonData)) {
            return null;
        }

        ArrayList<Article> allArticles = new ArrayList<>();
        try {
            JSONArray baseJsonArray = new JSONArray(jsonData);
            for(int a = 0; a <baseJsonArray.length(); a++){
                JSONObject currentArticleJsonObject = baseJsonArray.getJSONObject(a);
                articleID = currentArticleJsonObject.optInt(ARTICLE_ID);
                serverID = String.valueOf(Config.BASE_URL);
                articleTitle = currentArticleJsonObject.optString(ARTICLE_TITLE);
                articleAuthor = currentArticleJsonObject.optString(ARTICLE_AUTHOR);
                articleBody = currentArticleJsonObject.optString(ARTICLE_BODY);
                articleThumbUrl = currentArticleJsonObject.optString(ARTICLE_THUMB);
                articlePhotoUrl = currentArticleJsonObject.optString(ARTICLE_PHOTO_URL);
                articleAspectRatio = Float.valueOf(currentArticleJsonObject.optString(ARTICLE_ASPECT_RATIO));
                articlePublishDate = currentArticleJsonObject.optString(ARTICLE_PUBLISHED_DATE);

                Article article = new Article();
                article.setArticleID(articleID);
                article.setServerID(serverID);
                article.setArticleTitle(articleTitle);
                article.setArticleAuthor(articleAuthor);
                article.setArticleBody(articleBody);
                article.setArticleThumbUrl(articleThumbUrl);
                article.setArticlePhotoUrl(articlePhotoUrl);
                article.setArticleAspectRatio(articleAspectRatio);
                article.setArticlePublishDate(articlePublishDate);
                allArticles.add(article);

            }
        } catch (JSONException je) {
            Log.e(TAG, "extractArticleData: problem extracting data", je);
        }
        return allArticles;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
