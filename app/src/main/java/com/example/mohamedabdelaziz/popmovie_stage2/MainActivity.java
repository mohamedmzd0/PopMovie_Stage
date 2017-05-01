package com.example.mohamedabdelaziz.popmovie_stage2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

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
import java.util.ArrayList;

import static android.R.attr.type;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {
    GridView gridView ;
    String view_as = "popular" ;
    ArrayList<mydata> arrayList ;
    SharedPreferences sharedPreferences ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView=(GridView)findViewById(R.id.gridView);
        arrayList=new ArrayList<>();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent  = new Intent(getApplicationContext(),Detail_Activity.class);
                intent.putExtra("title", arrayList.get(position).title);
                intent.putExtra("poster_path", arrayList.get(position).movie_poster);
                intent.putExtra("release_date", arrayList.get(position).release_date);
                intent.putExtra("vote_average", arrayList.get(position).vote_average);
                intent.putExtra("plot_synopsis", arrayList.get(position).plot_synopsis);
                intent.putExtra("id", arrayList.get(position).id);
                startActivity(intent);
            }
        });
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()) ;

        }

    @Override
    protected void onStart() {
        super.onStart();
        view_as=sharedPreferences.getString("view","popular") ;
        new getdata().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.setting)
        {
            startActivity(new Intent(this,settings.class));
        }
        else  if(item.getItemId()==R.id.refresh)
        {
            new getdata().execute();
        }
        else if (item.getItemId()==R.id.favourite)
        {
            Cursor c = getContentResolver().query(Uri.parse("content://movie.data/table"), null, null, null, "");
                arrayList.clear();
            if (c.moveToFirst()) {
                do{
                   arrayList.add(new mydata(c.getString(c.getColumnIndex(contract_class.title)),c.getString(c.getColumnIndex(contract_class.release_date))
                           ,c.getString(c.getColumnIndex(contract_class.movie_poster)),c.getString(c.getColumnIndex(contract_class.vote_average))
                           ,c.getString(c.getColumnIndex(contract_class.plot_synopsis)),c.getString(c.getColumnIndex(contract_class.id))));

                } while (c.moveToNext());

        }
            gridView.setAdapter(new custom_adapter(getApplicationContext(),arrayList));
        }
        return super.onOptionsItemSelected(item);
    }
    class getdata extends AsyncTask<Void,Void,Void>
    {
        URL url = null ;
        HttpURLConnection httpURLConnection = null ;
        BufferedReader bufferedReader=null;
        StringBuffer stringBuffer=new StringBuffer() ;
        String Json ;
        InputStream inputStream=null ;

        @Override
        protected void onPreExecute() {
            arrayList.clear();
            try {
                url=new URL("https://api.themoviedb.org/3/movie/"+view_as+"?api_key=832f13a97b5d2df50ecf0dbc8a0f46ae");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

            } catch (IOException e) {
                e.printStackTrace();

            }
            try {
                inputStream = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream)) ;
                String temp;

                while((temp=bufferedReader.readLine())!=null)
                    stringBuffer.append(temp+"\n");

            } catch (IOException e) {
                e.printStackTrace();
            }

            Json =stringBuffer.toString();
            /************************************************************************/
            try {
                JSONObject object1 = new JSONObject(Json);
                JSONArray jsonArray  =object1.getJSONArray("results") ;

                for (int i = 0; i < jsonArray.length(); i++) {
                    String temp = jsonArray.getString(i);
                    JSONObject object  = new JSONObject(temp) ;
                    arrayList.add(new mydata(object.getString("original_title"),object.getString("release_date"),object.getString("poster_path"),
                            object.getString("vote_average"),object.getString("overview"),object.getString("id")));
                }

            } catch (JSONException e) {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            gridView.setAdapter(new custom_adapter(getApplicationContext(),arrayList));
        }

    }
}
