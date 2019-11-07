package com.vidal.testtask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.shuffle;

public class MainActivity extends AppCompatActivity {

    TipoDbHelper myHelper = null;
    SQLiteDatabase DB;
    int reqStatus = 0;

    Integer [] fruitCard = {R.mipmap.ic_download, R.mipmap.ic_share,R.mipmap.ic_wallpaper};
    List<Integer> fruitList = Arrays.asList(fruitCard);
    ArrayList<ImageView> pics;

    LinearLayout linearLayout;

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout = findViewById(R.id.container);
        pics = new ArrayList<>();
        pics.add( findViewById(R.id.im_desktop));
        pics.add( findViewById(R.id.im_download));
        pics.add( findViewById(R.id.im_share));
        linearLayout.setVisibility(View.INVISIBLE);

        button = findViewById(R.id.button);

        RotateAnimation anim = new RotateAnimation(0f, 350f, 5f, 5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(1);
        anim.setDuration(300);

        button.setOnClickListener((View v) -> {
            shuffle(fruitList);
            for(int i = 0; i<pics.size(); i++){
                pics.get(i).startAnimation(anim);
                pics.get(i).setImageResource(fruitList.get(i));
            }
            requestGet();

        });
        myHelper = new TipoDbHelper(getApplicationContext(), "TestDB", null, 1);
        requestGet();
    }
    public void requestGet(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://www.google.com/";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    Log.d(" STATUS CODE ", String.valueOf(reqStatus));
                    insertIntoDatabase(response);
                    readDatabase();
                    linearLayout.setVisibility(View.VISIBLE);
                }, error -> Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show()){

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                reqStatus = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };

        queue.add(stringRequest);
    }

    public void insertIntoDatabase(String response) {

            DB = myHelper.getWritableDatabase();
            ContentValues CV = new ContentValues();
            CV.put(myHelper.FIELD_CODE1, response);
            DB.insert(myHelper.TABLE_NAME, null, CV);
            DB.close();

    }

    public void readDatabase(){

        DB = myHelper.getReadableDatabase();
        String columns[] = {myHelper.FIELD_CODE1};
        Cursor cursor = DB.query(myHelper.TABLE_NAME, columns, null, null, null, null, myHelper.FIELD_CODE1);
        if(cursor != null){
            cursor.moveToFirst();
            if (cursor.moveToFirst()) {
                do {
                   Log.d("READ FROM DB ", cursor.getString(0));
                } while (cursor.moveToNext());
            }
        }
        DB.close();
    }

}

