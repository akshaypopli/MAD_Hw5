package com.example.homework05;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        final String source = getIntent().getExtras().getString(MainActivity.NEWS_KEY);
        Log.d("data get " , source);
        setTitle(source);
        String url = "https://newsapi.org/v2/top-headlines?sources="+ source+ "&apiKey=dd7c77b2dba94fe4956539f81c86f967";
        new GetDataAsync().execute(url);

    }

    public class GetDataAsync extends AsyncTask<String, Void, ArrayList<News>> {

        @Override
        protected ArrayList<News> doInBackground(String... strings) {
            HttpURLConnection connection = null;
            ArrayList<News> result = new ArrayList<>();
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection)url.openConnection();
                connection.connect();

                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    JSONObject root = new JSONObject(json);
                    Log.d("allnews", root.toString());
                    JSONArray articles = root.getJSONArray("articles");
                    if(articles.length() == 0){
                        Toast.makeText(getApplicationContext(), "No News Found", Toast.LENGTH_SHORT).show();
                    }else{
                        for(int i=0; i< articles.length();i++){
                            JSONObject articlesJSON = articles.getJSONObject(i);
                            News news = new News();
                            news.author = articlesJSON.getString("author");
                            news.title = articlesJSON.getString("title");
                            news.url = articlesJSON.getString("url");
                            news.publishedAt= articlesJSON.getString("publishedAt");
                            news.urlToImage = articlesJSON.getString("urlToImage");
                            result.add(news);
                        }
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(final ArrayList<News> sources) {

//            progressBar.setVisibility(View.INVISIBLE);
//
//            String newsNames[] = new String[sources.size()];
//
//            for (int j = 0; j < sources.size(); j++) {
//                newsNames[j] = sources.get(j).name;
//            }
//
//            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.activity_list_view, R.id.label, newsNames);
//            lv_news_names.setAdapter(adapter);
//
//            lv_news_names.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    Log.d("Selected", String.valueOf(sources.get(i)));
//                    Intent intent = new Intent(getBaseContext(), NewsActivity.class);
////                    intent.putExtra(NEWS_KEY, (Serializable) sources.get(i));
//                    intent.putExtra(NEWS_KEY, sources.get(i).id.toString());
//                    startActivity(intent);
//                }
//            });

        }
    }
}
