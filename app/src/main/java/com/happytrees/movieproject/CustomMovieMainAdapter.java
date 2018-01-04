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
import java.util.List;

public class CustomMovieMainAdapter extends ArrayAdapter <Movie> {//class for custom  array adapter
    ArrayList<Movie> mainList;
    Context mainContext;
    int mainLayoutId;//id of custom xml layout

    public CustomMovieMainAdapter( Context mainContext, int mainLayoutId,  ArrayList<Movie> mainList) {
        super(mainContext, mainLayoutId, mainList);
        this.mainList = mainList;
        this.mainContext = mainContext;
        this.mainLayoutId = mainLayoutId;
    }


    @Override
    public View getView(int position,View convertView,  ViewGroup parent) {
        if(convertView==null) {


            LayoutInflater mlayoutInflater = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mlayoutInflater.inflate(R.layout.custom_layout_main_movielist,null,true);
        }
        Movie mmMovie = getItem(position);


        TextView txtMovieItem = (TextView)convertView.findViewById(R.id.movie_main_item_id);//id of TextView in custom_layout_main_movielist.xml
        txtMovieItem.setText(mmMovie.title);

        ImageView MainImageView = (ImageView)convertView.findViewById(R.id.movie_image_main_item);
        Picasso.with(mainContext).load(mmMovie.url).into(MainImageView);//line from picasso site


        return convertView;
    }
}



