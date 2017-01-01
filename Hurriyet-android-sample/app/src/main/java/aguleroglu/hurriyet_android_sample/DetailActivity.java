package aguleroglu.hurriyet_android_sample;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by anil_ on 1.01.2017.
 */

public class DetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        Intent intent = getIntent();
        long id = intent.getLongExtra("ARTICLE_ID",0);

        new ArticleDetailData(this,String.valueOf(id)).execute();
    }

    class ArticleDetailData extends AsyncTask<String, String, String> {

        Activity _activity;
        String _articleId;
        public ArticleDetailData(Activity activity,String articleId){
            _activity = activity;
            _articleId = articleId;
        }

        @Override
        protected String doInBackground(String... arg0) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL("https://hurriyet-js-api.herokuapp.com/article?id="+_articleId);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
        protected void onPostExecute(String result){

            try {

                JSONObject article = new JSONObject(result);

                ImageView img = (ImageView)findViewById(R.id.article_detail_img);
                TextView txt = (TextView)findViewById(R.id.article_detail_title);
                String text = article.getString("StripedText");
                txt.setText(text);

                JSONArray files = article.getJSONArray("Files");
                String imageUrl = "";

                if(files!=null && files.length()!=0){
                    imageUrl = files.getJSONObject(0).getString("FileUrl");
                }

                if(imageUrl!="") {
                    UrlImageViewHelper.setUrlDrawable(img, imageUrl);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
