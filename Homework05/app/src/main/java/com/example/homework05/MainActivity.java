package com.example.homework05;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //filename: MainActivity
    //Group Number: groups1 3
    //Members : Akshay Popli and Neel Solanki

    ProgressBar progressBar;
    ListView lv_news_names;
    static String NEWS_KEY = "NEWS";
    int REQ_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Main Activity");

        progressBar = findViewById(R.id.progressBar);
        lv_news_names = findViewById(R.id.lv_news_names);

        //progressBar.setVisibility(View.VISIBLE);

        if(isConnected()){
            new GetDataAsync().execute("https://newsapi.org/v2/sources?apiKey=dd7c77b2dba94fe4956539f81c86f967");
        }else {
            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    public class GetDataAsync extends AsyncTask<String, Void, ArrayList<Source>> {

        @Override
        protected ArrayList<Source> doInBackground(String... strings) {
            HttpURLConnection connection = null;
            ArrayList<Source> result = new ArrayList<>();
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection)url.openConnection();
                connection.connect();

                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    JSONObject root = new JSONObject(json);
                    JSONArray sources = root.getJSONArray("sources");
                    if(sources.length() == 0){
                        Toast.makeText(getApplicationContext(), "No News Found", Toast.LENGTH_SHORT).show();
                    }else{
                        for(int i=0; i< sources.length();i++){
                            JSONObject sourceJSON = sources.getJSONObject(i);
                            Source source = new Source();
                            source.id = sourceJSON.getString("id");
                            source.name = sourceJSON.getString("name");

                            result.add(source);
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
        protected void onPostExecute(final ArrayList<Source> sources) {

            progressBar.setVisibility(View.INVISIBLE);

            String newsNames[] = new String[sources.size()];

            for (int j = 0; j < sources.size(); j++) {
                newsNames[j] = sources.get(j).name;
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.activity_list_view, R.id.label, newsNames);
            lv_news_names.setAdapter(adapter);

            lv_news_names.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d("Selected", String.valueOf(sources.get(i)));
                    Intent intent = new Intent(getBaseContext(), NewsActivity.class);
                    intent.putExtra(NEWS_KEY, sources.get(i).id.toString());
                    startActivity(intent);
                }
            });

        }
    }

}
