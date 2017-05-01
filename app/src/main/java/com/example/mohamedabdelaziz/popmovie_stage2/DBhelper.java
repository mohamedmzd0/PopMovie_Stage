package com.example.mohamedabdelaziz.popmovie_stage2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mohamed Abd Elaziz on 30/04/2017.
 */

public class DBhelper extends SQLiteOpenHelper {

    private static final String name = "data";
    private static final int version = 1;

    public DBhelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + contract_class.table_name + " ( " + contract_class.id + " VARCHAR(20) PRIMARY KEY , " +
                contract_class.movie_poster
                + " vARCHAR(50) , " + contract_class.title + " VARCHAR(50) , " + contract_class.release_date + " VARHCAR(30) , " +
                contract_class.vote_average +
                " VARCHAR(5) , " + contract_class.plot_synopsis + " VARCHAR(100) )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + contract_class.table_name);
        onCreate(db);
    }
}


