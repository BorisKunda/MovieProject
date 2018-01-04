package com.happytrees.movieproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Internet extends AppCompatActivity {
    ArrayList<Movie> arrayList;
    ListView lv;
    Button goBtn, cleanBtn, BtnCancel;
    EditText edtSearch;
    String frstpart, edtPart, finalpart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);


        lv = (ListView) findViewById(R.id.OnlineListView);
        edtSearch = (EditText) findViewById(R.id.edtSearch);

        frstpart = "https://api.themoviedb.org/3/search/movie?&query=";
        finalpart = "&api_key=7feab044341d650481d056f7b6fe4441";

        //GO BUTTON
        goBtn = (Button) findViewById(R.id.goBtn);
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtSearch.length() != 0) {
                    edtPart = edtSearch.getText().toString();
                    edtPart = edtPart.replaceAll(" ", "%20");//replaces empty places with "%20" cause in url query instead  of space use "%20"
                    String newEdittableUrl = frstpart + edtPart + finalpart;

                    READJSON readjson = new READJSON();//instantiate class  READJSON
                    readjson.execute(newEdittableUrl);
                } else {
                    Toast.makeText(Internet.this, "you forgot to write name of movie", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //CLEAN BUTTON --> Clean Edit Text field button
        cleanBtn = (Button) findViewById(R.id.CleanBtn);
        cleanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText(" ");
            }
        });
        //CANCEL BUTTON
        BtnCancel = (Button) findViewById(R.id.BtnCancel);
        BtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    //inner class
    class READJSON extends AsyncTask<String, Integer, String> {//AsynckTask for API search
        ProgressDialog dialog = new ProgressDialog(Internet.this);//set ProgressDialog
  /*
      <Params,Progress,Result>
       Params - The type of the input variables value you want to set to the background process. This can be an array of objects -->doInBackground
       Progress - The type of the objects you are going to enter in the onProgressUpdate method.-->onProgressUpdate
       Result - The type of the result from the operations you have done in the background process.-->onPostExecute()
       */

        @Override
        protected void onPreExecute() {
            dialog.setMessage("content loading");//message which will be displayed by dialog box
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {//three dots mean array. in this case array named params consisting of Strings
            return readURL(params[0]);//params[0] refers to  first element in array(because of i =0 ),like array of different urls you need execute
        }

        @Override
        protected void onPostExecute(String content) {
            arrayList = new ArrayList<>();
            try {
                //JSON parsing
                JSONObject jsonObject = new JSONObject(content);
                JSONArray jsonArray = jsonObject.getJSONArray("results");//"results" is name of array of movies in json link


                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject movieObject = jsonArray.getJSONObject(i);
                    arrayList.add(new Movie(movieObject.getString("title"), movieObject.getString("overview"), movieObject.getString("poster_path")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //custom array adapter
            CustomMovieSearchAdapter customMovieSearchAdapter = new CustomMovieSearchAdapter(getApplicationContext(), R.layout.custom_layout_movielist, arrayList);
            lv.setAdapter(customMovieSearchAdapter);
            dialog.dismiss();//stop dialog box from appearing after task executed
            //make ListView clickable
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Movie netMovie = arrayList.get(position);
                    Toast.makeText(Internet.this, netMovie.title, Toast.LENGTH_SHORT).show();//Displays toast with item name
                    //passing info of movie from search list into Details activity for editing
                    Intent edtSrchItmIntent = new Intent(Internet.this, Details.class);//creates explicit intent
                    edtSrchItmIntent.putExtra("title", netMovie.title);
                    edtSrchItmIntent.putExtra("overview", netMovie.overview);
                    edtSrchItmIntent.putExtra("imageUrl", "https://image.tmdb.org/t/p/w640" + netMovie.url);//"https://image.tmdb.org/t/p/w640" is lost part of poster_path url
                    edtSrchItmIntent.putExtra("key", "FromInternet");//key to differentiate between intents origins
                    startActivity(edtSrchItmIntent);//starts Details activity


                }
            });

        }
    }

    private String readURL(String theUrl) {
        StringBuilder content = new StringBuilder();
        try {
            //create a url object
            URL url = new URL(theUrl);
            //creates a urlConnection object
            URLConnection urlConnection = url.openConnection();
            //wrap the urlConnection in a BufferReader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            //read from the urlConnection via the bufferReader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}



