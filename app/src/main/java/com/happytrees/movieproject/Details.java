package com.happytrees.movieproject;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

public class Details extends AppCompatActivity {
    DBhelper dBhelper;
    EditText edtTitle,edtOverview,edtImageUrl;
    Button saveBtn,showBtn,cancelBtn;
    ImageView imageFromUrl;
    String stringUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ///in case there was received INTENT  from Internet Activity
        Intent incomingIntent= getIntent();//gets object passed by intent from previous activity


        String titlePassed = incomingIntent.getStringExtra("title");
        String overviewPassed = incomingIntent.getStringExtra("overview");
        String imageUrlPassed = incomingIntent.getStringExtra("imageUrl");
        final String keyPassed = incomingIntent.getStringExtra("key");
        final int idPassed = incomingIntent.getIntExtra("id",-1);
           /*  Default value (in this example "-1") is random value we choose to use instead ,if  no   int value was passed.Important that default value must
             be different from (in this example id numbers (1,2,3..) ) values  we pass  in order to prevent confusion.
             If no value was given  in putExtra then default value will be returned, else value that
             was sent will be returned */


        dBhelper = new DBhelper(this);//instantiating DBhelper class

        edtTitle = (EditText)findViewById(R.id.titleId);
        edtOverview = (EditText)findViewById(R.id.bodyId);
        edtImageUrl = (EditText)findViewById(R.id.URLid);

        imageFromUrl = (ImageView)findViewById(R.id.editableImage);

        //CANCEL BUTTON
        cancelBtn = (Button)findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //SHOW BUTTON
        showBtn = (Button)findViewById(R.id.showId);
        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtImageUrl.length()!=0) {
                    stringUrl = edtImageUrl.getText().toString();//if you would wrote this line before showbutton code it wouldn't work cause there no text yet in edit text
                    Picasso.with(Details.this).load(stringUrl).into(imageFromUrl);
                }else{
                    Toast.makeText(Details.this,"please provide image URl",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //SAVE/UPDATE BUTTON --> depends on passed key
        saveBtn = (Button)findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtTitle.length() != 0) {//edit text not empty

                    if (keyPassed.equals("FromMain")) {//"FromMain" means Intent from Main Activity
                        //UPDATE
                        dBhelper.updateMovie(edtTitle.getText().toString(), edtOverview.getText().toString(), edtImageUrl.getText().toString(), idPassed);
                        Toast.makeText(Details.this," movie updated",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Details.this,MainActivity.class);//creates explicit intent
                        finish();//closes current one
                        startActivity(intent);//opens main activity

                       //SAVE
                    }else {//Means Intent from Internet activity
                    boolean isInserted = dBhelper.insertData(edtTitle.getText().toString(), edtOverview.getText().toString(), edtImageUrl.getText().toString());
                    if (isInserted == true) {
                        Toast.makeText(Details.this, "insert success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Details.this,MainActivity.class);//creates explicit intent
                        finish();//closes current one
                        startActivity(intent);//opens main activity

                    }  else {
                        Toast.makeText(Details.this, "insert failure", Toast.LENGTH_SHORT).show();

                    }

                    }

                } else {//edit text empty
                    Toast.makeText(Details.this, "you forgot to fill title", Toast.LENGTH_SHORT).show();
                }

            }
        });

            //setting text in edit text fields from received text via intent
            edtTitle.setText(titlePassed);
            edtOverview.setText(overviewPassed);
            edtImageUrl.setText(imageUrlPassed);

    }
}

