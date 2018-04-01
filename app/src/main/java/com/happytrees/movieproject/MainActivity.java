package com.happytrees.movieproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DBhelper maindBhelper;
    Button plusBtn;
    ListView mainList;

    //listData IS NAME OF MOVIE  ARRAY. mainList IS NAME OF LISTVIEW --> DONT CONFUSE THEM


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //LIST VIEW
        mainList = (ListView) findViewById(R.id.MainListView);
        maindBhelper = new DBhelper(this);////instantiating DBhelper class
        populateListView();//method which populates Activity with list of objects from database

        //PLUS BUTTON -->opens dialog window
        plusBtn = (Button) findViewById(R.id.buttonPlus);
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ALERT DIALOG
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Adding Options");//dialog title
                builder.setMessage("which one suits you best?");//dialog message

                builder.setPositiveButton("Online", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent netIntent = new Intent(MainActivity.this, Internet.class);
                        startActivity(netIntent);//starts Internet activity
                    }
                });

                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();//closes dialog window
                    }
                });

                builder.setNegativeButton("Manual", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent manualIntent = new Intent(MainActivity.this, Details.class);//creates explicit intent
                        manualIntent.putExtra("key","manual");
                        startActivity(manualIntent);//starts Details activity
                    }
                });
                builder.show();//don't forget otherwise dialog wont show
            }
        });
    }


    //this method is instead of creating cursor adapter
    private void populateListView() {//populates listview in main activity from items in database
        //get the data and append it to a list
        Cursor data = maindBhelper.getData();
        final ArrayList<Movie> listData = new ArrayList<>();
        while (data.moveToNext()) {
            //then add it to the ArrayList
            /*
               column num        |    0    |      1     |      2        |        3         |
             column name         |    ID   |    TITLE   |  OVERVIEW     |     IMAGEURL     |
             Deadpool            |  0      |  Deadpool  |deadpool blala |deadpool.img      |
             DarkKnight          | 1       | DarkKnight |darknight bla  |darkkoght.img     |

             */
            String title = data.getString(1);//column 1 --> title column
            String overview = data.getString(2); //column 2 --> overview column
            String url = data.getString(3);//column 3 --> imageUrl column
            int id = data.getInt(0);//column 0 --> id column
            Movie nMovie = new Movie(title, overview, url, id);//we create movie object each time item added to list from database
            listData.add(nMovie);

        }
        //create the List adapter and set the adapter
        final CustomMovieMainAdapter mainAdapter = new CustomMovieMainAdapter(this, R.layout.custom_layout_main_movielist, listData);
        mainList.setAdapter(mainAdapter);
        //make ListView clickable

        //REGULAR CLICK
        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Movie clickMovie = listData.get(position);
                Toast.makeText(MainActivity.this, clickMovie.title, Toast.LENGTH_SHORT).show();//toast message with name of movie clicked
                Intent MainClickIntent = new Intent(MainActivity.this, Details.class);//creates explicit intent
                MainClickIntent.putExtra("title", clickMovie.title);
                MainClickIntent.putExtra("overview", clickMovie.overview);
                MainClickIntent.putExtra("imageUrl", clickMovie.url);
                MainClickIntent.putExtra("id", clickMovie.id);
                MainClickIntent.putExtra("key", "FromMain");//key to differentiate between intents origins
                startActivity(MainClickIntent);
            }
        });

        //LONG CLICK
        mainList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final Movie mainMovie = listData.get(position);//create temporal object of Movie type and getting position out of it

                //AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                //builder.setIcon();--> IN FUTURE SET ICON FOR DIALOG
                builder.setTitle("movie options");//dialog title
                builder.setMessage("please choose one");//dialog message

                builder.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//dismiss dialog
                        dialog.dismiss();
                    }
                });

                builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Movie mMovie = listData.get(position);
                        //passing info of movie from search list into Details activity for editing
                        Intent MainItmIntent = new Intent(MainActivity.this, Details.class);//creates explicit intent
                        MainItmIntent.putExtra("title", mMovie.title);
                        MainItmIntent.putExtra("overview", mMovie.overview);
                        MainItmIntent.putExtra("imageUrl", mMovie.url);
                        MainItmIntent.putExtra("id", mMovie.id);
                        MainItmIntent.putExtra("key", "FromMain");//key to differentiate between intents origins
                        startActivity(MainItmIntent);
                    }
                });
                //remove item
                builder.setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        maindBhelper.deleteMovie(mainMovie);//deletes Movie type object by its name
                        // this code refreshes activity
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                        Toast.makeText(MainActivity.this, "movie has been removed", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();//don't forget otherwise dialog wont show


                return true;
            }
        });
    }

    @Override
    protected void onResume() {//instead of cursor swap.make List of db refresh itself.onResume means when you open main activity again it will refresh itself
        super.onResume();
        populateListView();
    }
    //OPTIONS MENU

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_template, menu);//inflate menu via xml template
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//make menu clickable
        if (item.getItemId() == R.id.ExitMenuBtn) {//exits from app
            ActivityCompat.finishAffinity(MainActivity.this);//closes all activities at time.for API 4.1+ (in this case 4.3+) you can use "finishAffinity();"
        } else if (item.getItemId() == R.id.deleteAllMenuBtn) {
            maindBhelper.deleteEverything();//deletes everything
            // this code refreshes activity
            Intent i = getIntent();
            finish();
            startActivity(i);
            Toast.makeText(MainActivity.this, "All movies removed", Toast.LENGTH_SHORT).show();
        }
        return true;
        //Alternatively you can use item.getTitle
    }
}
