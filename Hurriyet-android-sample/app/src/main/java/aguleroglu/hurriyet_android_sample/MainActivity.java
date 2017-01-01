package aguleroglu.hurriyet_android_sample;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView lstArticles = (ListView)findViewById(R.id.lstArticles);

        lstArticles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), DetailActivity.class);
                intent.putExtra("ARTICLE_ID",id);
                startActivity(intent);
            }
        });

        new ArticlesData(this).execute();

    }

    class ArticlesData extends AsyncTask<String, String, String> {

        Activity _activity;
        public ArticlesData(Activity activity){
            _activity = activity;
        }

        @Override
        protected String doInBackground(String... arg0) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL("https://hurriyet-js-api.herokuapp.com/articles?top=10");
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
                JSONArray articles = new JSONArray(result);

                ArticleAdapter adatpter = new ArticleAdapter(articles,_activity);

                ListView lstArticles = (ListView)findViewById(R.id.lstArticles);

                lstArticles.setAdapter(adatpter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
