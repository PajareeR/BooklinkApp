package th.ac.su.booklink.booklink;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.BottomNavigationView;
import android.support.annotation.NonNull;
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

import th.ac.su.booklink.booklink.Details.ImageDetail;
import th.ac.su.booklink.booklink.Details.UserDetail;

public class BookselfActivity extends AppCompatActivity {

    ArrayList<ImageDetail> imageAL = new ArrayList<>();
    TextView textFav, textRead, textWant, textBought, textReading;
    RelativeLayout imageBox;
    String nowStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//barTop

        setContentView(R.layout.activity_bookself);

        imageBox = (RelativeLayout) findViewById(R.id.imageBox);

        textFav = (TextView) findViewById(R.id.textFav);
        textRead = (TextView) findViewById(R.id.textRead);
        textWant = (TextView) findViewById(R.id.textWant);
        textBought = (TextView) findViewById(R.id.textBought);
        textReading = (TextView) findViewById(R.id.textReading);

        textFav.performClick();


    }

    public void OnclickMybookself(View view) {
        TextView btnSerect = (TextView) view;
        imageBox.removeAllViews();

        imageAL.clear();

        switch(btnSerect.getId())
        {

            case R.id.textFav:
                nowStatus = "fav";
                setTextColor(textFav);
                loadBook("fav");
                break;
            case R.id.textRead:
                nowStatus = "read";
                setTextColor(textRead);
                loadBook("read");
                break;
            case R.id.textWant:
                nowStatus = "want";
                setTextColor(textWant);
                loadBook("want");
                break;
            case R.id.textBought:
                nowStatus = "bought";
                setTextColor(textBought);
                loadBook("bought");
                break;
            case R.id.textReading:
                nowStatus = "reading";
                setTextColor(textReading);
                loadBook("reading");
                break;
        }


    }

    private void setTextColor(TextView textWantColor) {
        textFav.setTextColor(Color.BLACK);
        textRead.setTextColor(Color.BLACK);
        textWant.setTextColor(Color.BLACK);
        textBought.setTextColor(Color.BLACK);
        textReading.setTextColor(Color.BLACK);


        textWantColor.setTextColor(getResources().getColor(R.color.colorPrimary));

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
                        for (int j = 0 ; j < imageAL.size() ;  j++){
                            if (imageAL.get(j).getImagePath().contains(key) && imageAL.get(j).getImageStatus().equals(nowStatus)){
                                countImage+=1; //4

//                                LinearLayout.LayoutParams layparam = new LinearLayout.LayoutParams(100, 150);//o
                                    LinearLayout.LayoutParams layparam = new LinearLayout.LayoutParams(220 , 320);//o

                                switch (countImage % 3){
                                    case  1:
                                        layparam.setMargins(10,350* (countImage/3),0,0);//o
                                        break;
                                    case 2:
                                        layparam.setMargins(230,350* (countImage/3),0,0);//o
                                        break;
                                    default :
                                        layparam.setMargins(450,350* ((countImage/3)-1),0,0);//o
                                        break;
                                }

                                    ImageView image = new ImageView(BookselfActivity.this);//o
                                    image.setLayoutParams(layparam);//o

                                    Picasso.get().load(obj.getJSONObject(key).getString("imgbook")).into(image);

                                final int index = j;
                                image.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            UserDetail.bookserect = imageAL.get(index).getImagePath();
                                            startActivity(new Intent(BookselfActivity.this,BookDetailActivity.class));
                                        }
                                    });

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
                            ImageDetail image = new ImageDetail();
                            image.setImagePath(key);
                            image.setImageStatus(type);
                            imageAL.add(image);

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
                        startActivity(new Intent(BookselfActivity.this, AwardActivity.class));

                        return true;
                    case R.id.item_Celebrity:
                        return true;
                    case R.id.item_Bookself:
                       // startActivity(new Intent(BookselfActivity.this, BookselfActivity.class));

                        return true;
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.item_Bookself);


    }


}
