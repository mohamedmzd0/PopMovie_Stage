package com.example.mohamedabdelaziz.popmovie_stage2;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Mohamed Abd Elaziz on 30/04/2017.
 */

public class movie_provider extends ContentProvider {
    private static final String provider_name = "movie.data";
    private static final String url= "content://"+provider_name+"/table";
    final Uri uri = Uri.parse(url) ;
    SQLiteDatabase db ;
    DBhelper dBhelper;


    @Override
    public boolean onCreate() {
     dBhelper = new DBhelper(getContext());


        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor = dBhelper.getReadableDatabase().query(
                contract_class.table_name, projection, selection, selectionArgs,
                null, null, sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        db  = dBhelper.getWritableDatabase();
         long check = db.insert(contract_class.table_name, "", values);
        if(check>0)
        Toast.makeText(getContext(), "sucess", Toast.LENGTH_SHORT).show();
        else {
            delete(uri, values.getAsString(contract_class.id),null);
        }return uri;
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
     db.delete(contract_class.table_name,selection,selectionArgs);
        Toast.makeText(getContext(), "delete", Toast.LENGTH_SHORT).show();
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }


    /**
     * Created by Mohamed Abd Elaziz on 29/04/2017.
     */


}
