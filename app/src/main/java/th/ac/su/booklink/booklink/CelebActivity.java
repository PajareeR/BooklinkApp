package th.ac.su.booklink.booklink;

import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import th.ac.su.booklink.booklink.Details.BookAwardDetail;
import th.ac.su.booklink.booklink.Details.UserDetail;

public class CelebActivity extends AppCompatActivity {
    LinearLayout celebBoxLayout;
    int widthDevice, hieghDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//barTop


        setContentView(R.layout.activity_celeb);

        celebBoxLayout = (LinearLayout) findViewById(R.id.celebBox);
        widthDevice = getWindowManager().getDefaultDisplay().getWidth();
        hieghDevice = getWindowManager().getDefaultDisplay().getHeight();

        String url = "https://booklink-94984.firebaseio.com/Celebs.json"; //หัวใหญ่
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);

                    Iterator i = obj.keys();
                    String key = "";


                    while (i.hasNext()) {
                        key = i.next().toString();

                        ImageView imageView = new ImageView(CelebActivity.this);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                (int) (hieghDevice * 0.35)
                        );

                        layoutParams.setMargins(0, 20, 0, 0);

                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        imageView.setLayoutParams(layoutParams);


                        celebBoxLayout.addView(imageView);


                        final String finalKey = key;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                OnclickBookDetail(finalKey);
                            }
                        });

                        Picasso.get().load(obj.getJSONObject(key).getString("image")).into(imageView);


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("" + error);
            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(CelebActivity.this);
        requestQueue.add(request);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_Home:
                        startActivity(new Intent(CelebActivity.this, MainActivity.class));
                        return true;
                    case R.id.item_Award:
                        startActivity(new Intent(CelebActivity.this, AwardActivity.class));
                        return true;
                    case R.id.item_Celebrity:
                        //  startActivity(new Intent(CelebActivity.this, RegisterActivity.class));
                        return true;
                    case R.id.item_Bookself:
                        startActivity(new Intent(CelebActivity.this, BookselfActivity.class));
                        return true;
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.item_Celebrity);
    }

    public void OnclickBookDetail(String bookName) {
        UserDetail.celebserect = bookName;
        startActivity(new Intent(CelebActivity.this, BookCelebActivity.class));
    }
}

