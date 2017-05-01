package com.example.mohamedabdelaziz.popmovie_stage2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Mohamed Abd Elaziz on 27/04/2017.
 */

public class custom_adapter extends BaseAdapter {
    Context context;
    ArrayList<mydata>arrayList ;

    public custom_adapter(Context context, ArrayList<mydata> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.gridview_item,parent,false);
        ImageView poster = (ImageView)convertView.findViewById(R.id.poster_thumbnail) ;
        Picasso.with(context).load("http://image.tmdb.org/t/p/w185/"+arrayList.get(position).movie_poster).into(poster);
        return convertView;
    }
}
