package aguleroglu.hurriyet_android_sample;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by anil_ on 1.01.2017.
 */

public class ArticleAdapter extends BaseAdapter {

    JSONArray _articles;
    Activity _activity;

    public ArticleAdapter(JSONArray articles, Activity activity){
        _articles = articles;
        _activity = activity;
    }

    @Override
    public int getCount() {
        return _articles.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return _articles.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public long getItemId(int position) {
        try {
            JSONObject article = _articles.getJSONObject(position);

            long id = article.getLong("Id");

            return id;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)_activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.article_cell, null);

        TextView txtTitle = (TextView)vi.findViewById(R.id.article_list_title);
        ImageView img = (ImageView)vi.findViewById(R.id.artcile_list_img);
        try {
            JSONObject article = _articles.getJSONObject(position);
            txtTitle.setText(article.getString("Title"));
            String imageUrl = "";

            JSONArray files = article.getJSONArray("Files");

            if(files!=null && files.length()!=0){
                imageUrl = files.getJSONObject(0).getString("FileUrl");
            }

            if(imageUrl!="") {
                UrlImageViewHelper.setUrlDrawable(img, imageUrl);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return vi;
    }
}
