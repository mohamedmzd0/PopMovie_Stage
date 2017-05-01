package com.example.mohamedabdelaziz.popmovie_stage2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Detail_Activity extends AppCompatActivity {
    TextView text_title  , date_vote , plot  ,review ;
    ImageView image_poster ;
    ArrayList<String>trailers_list ;
    Button favourite ;
    ListView listView ;
    StringBuffer content ;
    String id  , title , release_date , movie_poster , vote_average , plot_synopsis ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_);
        text_title = (TextView)findViewById(R.id.ttitle);
        review = (TextView)findViewById(R.id.review);
        image_poster=(ImageView)findViewById(R.id.image_poster);
        date_vote = (TextView)findViewById(R.id.date_and_vote);
        favourite=(Button) findViewById(R.id.favourite_button);
        listView = (ListView) findViewById(R.id.trailer);
        trailers_list = new ArrayList<>();
        plot = (TextView)findViewById(R.id.plot);
        final Intent intent = getIntent() ;
        title=  intent.getStringExtra("title");
        release_date = intent.getStringExtra("release_date");
        movie_poster = intent.getStringExtra("poster_path");
        vote_average = intent.getStringExtra("vote_average");
        plot_synopsis=intent.getStringExtra("plot_synopsis") ;
        id = intent.getStringExtra("id");
        text_title.setText(title);
        Picasso.with(getApplicationContext()).load("http://image.tmdb.org/t/p/w185/"+movie_poster).into(image_poster);
        date_vote.setText("Date : "+release_date+"\n\n"+"Vote : "+vote_average);
        plot.setText(plot_synopsis);
        new getreview().execute();
        new gettrailers().execute();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent();
                intent1.setData(Uri.parse("https://www.youtube.com/watch?v="+trailers_list.get(position)));
                startActivity(intent1);
            }
        });
        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(contract_class.id,id);
                values.put(contract_class.title,title);
                values.put(contract_class.movie_poster,movie_poster);
                values.put(contract_class.vote_average,vote_average);
                values.put(contract_class.plot_synopsis,plot_synopsis);
                values.put(contract_class.release_date,release_date);
                Uri uri = getContentResolver().insert(Uri.parse(R.string.provider_uri),values);
                is_favourite();
            }
        });
        is_favourite();
    }
    void is_favourite()
    {
        Cursor c = getContentResolver().query(Uri.parse("content://movie.data/table"), null, null, null, "");
        boolean temp=false ;
        if (c.moveToFirst()) {
            do{
              if(id.equals(c.getString(c.getColumnIndex(contract_class.id)))) {
                  temp = true;
                    return;
              }
            } while (c.moveToNext());

        }
        if(temp)
            favourite.setBackgroundResource(R.drawable.fav);
        else
            favourite.setBackgroundResource(R.drawable.not_fav);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu,menu);
        menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share_it) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
            i.putExtra(Intent.EXTRA_TEXT, trailers_list.get(0));
            startActivity(Intent.createChooser(i, "Share URL"));
        }
        return true;
    }

    class getreview extends AsyncTask<String, String, String> {
        protected String doInBackground(String... args) {
            HttpURLConnection httpURLConnection = null ;
            BufferedReader bufferedReader =null ;
            InputStream inputStream ;
            StringBuffer stringBuffer=new StringBuffer("") ;
            try {
                URL myurl = new URL("https://api.themoviedb.org/3/movie/"+id+"/reviews?api_key="+"832f13a97b5d2df50ecf0dbc8a0f46ae");
                httpURLConnection=(HttpURLConnection) myurl.openConnection() ;
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                inputStream = httpURLConnection.getInputStream();
                if(inputStream==null)
                    return null ;
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream)) ;
                String line=null ;
                while ((line = bufferedReader.readLine())!=null)
                    stringBuffer.append(line+"\n") ;
                String jsn = stringBuffer.toString() ;
                JSONObject jsonObject = new JSONObject(jsn);
                JSONArray result = jsonObject.getJSONArray("results");
                content = new StringBuffer();
                for (int i = 0; i < result.length() ; i++) {
                    JSONObject jsonObject1 = result.getJSONObject(i);
                    content.append( jsonObject1.getString("content")) ;
                }
            }catch (Exception e)
            {
            }
            return null ;
        }

        @Override
        protected void onPostExecute(String s) {
            review.setText(content.toString());
        }
    }

    /****************************************************************/
    class gettrailers extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            trailers_list.clear();
        }
        protected String doInBackground(String... args) {

            HttpURLConnection httpURLConnection = null ;
            BufferedReader bufferedReader =null ;
            InputStream inputStream ;
            StringBuffer stringBuffer=new StringBuffer() ;
            try {
                URL myurl = new URL("https://api.themoviedb.org/3/movie/"+id+"/videos?api_key="+"832f13a97b5d2df50ecf0dbc8a0f46ae");
                httpURLConnection=(HttpURLConnection) myurl.openConnection() ;
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                inputStream = httpURLConnection.getInputStream();
                if(inputStream==null)
                    return null ;
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream)) ;
                String line ;
                while ((line = bufferedReader.readLine())!=null)
                    stringBuffer.append(line+"\n") ;
                String jsn = stringBuffer.toString() ;
                JSONObject jsonObject = new JSONObject(jsn);
                JSONArray result = jsonObject.getJSONArray("results");
                for (int i = 0; i < result.length() ; i++) {
                    JSONObject jsonObject1 = result.getJSONObject(i);
                    String key = jsonObject1.getString("key") ;
                    trailers_list.add(key) ;
                }
            }catch (Exception e)
            {
                Log.e(e.getMessage(),"background") ;
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ArrayList<String> strings = new ArrayList<>();
            for (int i = 0; i < trailers_list.size(); i++) {
                strings.add("Trailer "+(i+1));
            }
            ArrayAdapter adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,strings) ;
            ViewGroup.LayoutParams L = listView.getLayoutParams();
            L.height = adapter.getCount()*75;
            listView.setLayoutParams(L);
            listView.requestLayout();
            listView.setAdapter(adapter);
        }
    }
}
