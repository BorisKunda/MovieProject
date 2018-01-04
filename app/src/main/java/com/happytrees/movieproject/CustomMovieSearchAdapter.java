package com.happytrees.movieproject;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomMovieSearchAdapter  extends ArrayAdapter <Movie> { //class for custom  array adapter

    ArrayList<Movie> searchList;
    Context context;
    int layoutId;//id of custom xml layout

    //constructor
    public CustomMovieSearchAdapter( Context context, int layoutId, ArrayList<Movie> searchList) {
        super(context, layoutId, searchList);
        this.searchList = searchList;
        this.context = context;
        this.layoutId = layoutId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.custom_layout_movielist,null,true);
        }
        Movie movie = getItem(position);

        TextView txtMovieItem = (TextView)convertView.findViewById(R.id.movie_item_id);//id of TextView in custom_layout_movielist.xml
        txtMovieItem.setText(movie.title);

        ImageView imageView = (ImageView)convertView.findViewById(R.id.movie_image_search_item);
        Picasso.with(context).load("https://image.tmdb.org/t/p/w640" + movie.url).into(imageView);//line from picasso site




        return convertView;
    }
}