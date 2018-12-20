package th.ac.su.booklink.booklink;

import android.content.Intent;
import android.media.Image;
import android.support.design.widget.BottomNavigationView;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class BookselfActivity extends AppCompatActivity {

    ArrayList<String> imageAL = new ArrayList<>();
    TextView textFav, textRead, textWant, textBought, textReading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//barTop

        setContentView(R.layout.activity_bookself);

        textFav = (TextView) findViewById(R.id.textFav);
        textRead = (TextView) findViewById(R.id.textRead);
        textWant = (TextView) findViewById(R.id.textWant);
        textBought = (TextView) findViewById(R.id.textBought);
        textReading = (TextView) findViewById(R.id.textReading);

        textFav.performClick();
        textRead.performClick();
        textWant.performClick();
        textBought.performClick();
        textReading.performClick();

    }

    public void OnclickMybookself(View view) {
        TextView btnSerect = (TextView) view;
        switch(btnSerect.getId())
        {

            case R.id.textFav:
                loadBook("fav");
                break;
            case R.id.textRead:
                loadBook("read");
                break;
            case R.id.textWant:
                loadBook("want");
                break;
            case R.id.textBought:
                loadBook("bought");

                break;
            case R.id.textReading:
                loadBook("reading");
                break;
        }


    }

    public void getImage() {



        String url = "https://booklink-94984.firebaseio.com/Books.json"; //หัวใหญ่
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);

                    Iterator i = obj.keys();
                    String key = "";

                    int countImage;
                    countImage=0;
                    RelativeLayout imageBox = (RelativeLayout) findViewById(R.id.imageBox);//o

                    while (i.hasNext()) {
                        key = i.next().toString();

                        if (imageAL.contains(key)){
                            countImage+=1;
                            if(countImage>3 || countImage==1){
                                countImage =1;
//                                LinearLayout.LayoutParams layparam = new LinearLayout.LayoutParams(100, 150);//o
                                LinearLayout.LayoutParams layparam = new LinearLayout.LayoutParams(220, 320);//o

                                layparam.setMargins(7,0,0,0);//o

                                ImageView image = new ImageView(BookselfActivity.this);//o
                                image.setLayoutParams(layparam);//o

                                Picasso.get().load(obj.getJSONObject(key).getString("imgbook")).into(image);

                                imageBox.addView(image);//o
                                //Image.Setonclick
                                //set Userdetail.bookselect
                                //staractvity

                            }

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
        RequestQueue requestQueue = Volley.newRequestQueue(BookselfActivity.this);
        requestQueue.add(request);

    }

    public void loadBook(final String type) {
        String url = "https://booklink-94984.firebaseio.com/Users/"+UserDetail.username+"/bookselfs.json"; //หัวใหญ่
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);

                    Iterator i = obj.keys();
                    String key = "";

                    while (i.hasNext()) {
                        key = i.next().toString();
                        if (obj.getJSONObject(key).getString(type).equals("true")) {
                            imageAL.add(key);

                        }
                    }

//                    NameBook.setText(obj.getJSONObject(UserDetail.bookserect).getString("bookname"));
//                    AuthorBook.setText("นักเขียน"+obj.getJSONObject(UserDetail.bookserect).getString("authorname"));
//                    Picasso.get().load(obj.getJSONObject(UserDetail.bookserect).getString("imgbook")).into(ImageBook);

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
        RequestQueue requestQueue = Volley.newRequestQueue(BookselfActivity.this);
        requestQueue.add(request);

        getImage();

            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_Home:
                        startActivity(new Intent(BookselfActivity.this, MainActivity.class));

                        return true;
                    case R.id.item_Award:
                        return true;
                    case R.id.item_Celebrity:
                        return true;
                    case R.id.item_Bookself:
                        return true;
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.item_Bookself);



    }
    public void OnclickBookDetail(View view){
        UserDetail.bookserect = "9786161826192";
        startActivity(new Intent(BookselfActivity.this,MainActivity.class));
    }

}
