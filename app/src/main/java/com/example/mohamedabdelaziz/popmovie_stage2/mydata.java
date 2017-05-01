package com.example.mohamedabdelaziz.popmovie_stage2;

/**
 * Created by Mohamed Abd Elaziz on 27/04/2017.
 */

public class mydata {
    String  title, release_date, movie_poster, vote_average,plot_synopsis,id ;
    public mydata(String title, String release_date, String movie_poster, String vote_average, String plot_synopsis,String id) {
        this.title = title;
        this.release_date = release_date;
        this.movie_poster = movie_poster;
        this.vote_average = vote_average;
        this.plot_synopsis = plot_synopsis;
        this.id=id;
    }
}
