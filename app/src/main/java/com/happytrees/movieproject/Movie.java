package com.happytrees.movieproject;


public class Movie {
    String title;
    String overview;
    String url;
    int id;


    //constructor
    public Movie(String title, String overview, String url) {
        this.title = title;
        this.overview = overview;
        this.url = url;
    }

    //constructor 2
    public Movie(String title, String overview, String url, int id) {
        this.title = title;
        this.overview = overview;
        this.url = url;
        this.id = id;
    }

    //override toString
    @Override
    public String toString() {
        return title;
        //Alternatively  --> return title + overview + url ;
    }
}
