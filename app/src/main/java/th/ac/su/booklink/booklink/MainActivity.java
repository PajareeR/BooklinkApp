package th.ac.su.booklink.booklink;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import th.ac.su.booklink.booklink.Details.UserDetail;

public class MainActivity extends AppCompatActivity {
    LinearLayout quoteBoxLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//barTop

        setContentView(R.layout.activity_main);
        quoteBoxLayout  = (LinearLayout) findViewById(R.id.quoteBox);

        String url = "https://booklink-94984.firebaseio.com/Books.json"; //หัวใหญ่
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);

                    Iterator i = obj.keys();
                    String key = "";


                    while (i.hasNext()) {
                        key = i.next().toString();

                        if (!obj.getJSONObject(key).getString("quotebook").equals("nill")){
                            ImageView imageView = new ImageView(MainActivity.this);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    390

                            );

                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            imageView.setLayoutParams(layoutParams);


                            quoteBoxLayout.addView(imageView);


                            final String finalKey = key;
                            imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    OnclickBookDetail(finalKey);
                                }
                            });

                            Picasso.get().load(obj.getJSONObject(key).getString("quotebook")).into(imageView);

                        }



                    }

                } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            System.out.println(""+error);
        }
    });
    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);

                BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_Home:
//                        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                        return true;
                    case R.id.item_Award:
                        startActivity(new Intent(MainActivity.this, AwardActivity.class));

                        return true;
                    case R.id.item_Celebrity:
                        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                        return true;
                    case R.id.item_Bookself:
                        startActivity(new Intent(MainActivity.this, BookselfActivity.class));
                        return true;
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.item_Home);
    }
    public void OnclickBookDetail(String bookName){
        UserDetail.bookserect = bookName;
        startActivity(new Intent(MainActivity.this,BookDetailActivity.class));
    }
}