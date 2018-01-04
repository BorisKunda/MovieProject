package com.happytrees.movieproject;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "NewMovies.db";
    public static final String TABLE_MOVIES = "movies";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "movie_name";
    public static final String COLUMN_OVERVIEW = "overview";
    public static final String COLUMN_IMAGE_URL = "image_url";

    public DBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /* The onCreate() method gets called when the database first gets created on the device.
     The method should include all the code needed to create the tables you need for your app.  */
    @Override
    public void onCreate(SQLiteDatabase db) {//TRY MAKING COLUMN TITLE UNIQUE AFTERWARDS
        String query = " CREATE TABLE " + TABLE_MOVIES + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + COLUMN_TITLE + " TEXT  , "
                + COLUMN_OVERVIEW + " TEXT , " + COLUMN_IMAGE_URL + " TEXT ) ; ";
        db.execSQL(query);
    }


    /*
     The onUpgrade() method gets called when the database needs to
 be upgraded. As an example, if you need to modify the structure of the
 database after it’s been released, this is the method to do it in.
    @Override
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_MOVIES);
        onCreate(db);
    }

    public boolean insertData(String title, String overview, String imageUrl) {//method inserts data
        SQLiteDatabase db = this.getWritableDatabase();//"getWritable" for database edit (delete,update,add)
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_OVERVIEW, overview);
        contentValues.put(COLUMN_IMAGE_URL, imageUrl);
        long result = db.insert(TABLE_MOVIES, null, contentValues);
        //you can write if statement without curly brackets if it returns single sentence

        if (result == -1)//insert method returns by default  "-1" in case of failure
            return false;
        else
            return true;
    }

    public Cursor getData() {//method which uses cursor for reading from sqlite database. a database cursor is a structure that enables traveling over the records in a database.
        SQLiteDatabase db = this.getReadableDatabase();//getReadable for reading getWritable for Editing
        String query = "SELECT * FROM " + TABLE_MOVIES;//basic sql command : SELECT * FROM table;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    //remove method
    public void deleteMovie(Movie title) {//deletes movie from db  according to its  name
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_MOVIES + " WHERE " + COLUMN_TITLE + "=\"" + title + "\";");

    }

    //update method
    public void updateMovie(String title, String overview, String url, int id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_OVERVIEW, overview);
        cv.put(COLUMN_IMAGE_URL, url);
        db.update(TABLE_MOVIES, cv, " _id = " + id, null);
    }


    //delete all objects from db table method
    public void deleteEverything() {//Deletes everything from table
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MOVIES, null, null);
        db.close();
    }
}



