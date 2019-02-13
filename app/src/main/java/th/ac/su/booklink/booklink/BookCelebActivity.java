package th.ac.su.booklink.booklink;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.RequestQueue;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import th.ac.su.booklink.booklink.Adapters.AwardAdapter;
import th.ac.su.booklink.booklink.Details.BookAwardDetail;
import th.ac.su.booklink.booklink.Details.UserDetail;

//import th.ac.su.booklink.booklink.BookCelebAdapter;
public class BookCelebActivity extends AppCompatActivity {
    LinearLayout celebBookBoxLayout;
    ImageView imageCelebBook;
    TextView nameBook, authorBookCeleb;



    ArrayList<BookAwardDetail> bookAward = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//barTop

        setContentView(R.layout.activity_book_celeb);
        celebBookBoxLayout = (LinearLayout) findViewById(R.id.celebBookBoxLayout);
        imageCelebBook = (ImageView) findViewById(R.id.imageCelebBook);
        nameBook = (TextView) findViewById(R.id.nameBook);
        authorBookCeleb = (TextView) findViewById(R.id.authorBookCeleb);


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

                        ImageView imageView = new ImageView(BookCelebActivity.this);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                270
                        );

                        layoutParams.setMargins(0,20,0,0);

                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        imageView.setLayoutParams(layoutParams);




                        celebBookBoxLayout.addView(imageView);



                        final String finalKey = key;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                OnclickBookDetail(finalKey);
                            }

                        });

                        Picasso.get().load(obj.getJSONObject(key).getString("image")).into(imageCelebBook);



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


        RequestQueue requestQueue = Volley.newRequestQueue(BookCelebActivity.this);
        requestQueue.add(request);


    }
    public void OnclickBookDetail(String bookName){
        UserDetail.bookserect = bookName;
        startActivity(new Intent(BookCelebActivity.this,BookDetailActivity.class));
    }
}




















//public class BookCelebActivity extends AppCompatActivity {
//
//    ViewPager viewPager;
//    Adapter adapter;
//    List<Model> models;
//    Integer[] colors = null;
//    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
//    private Object BookCelebAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_book_celeb);
//
//        models = new ArrayList<>();
//        models.add(new Model(R.drawable.sambook,"BookNo1","AuthorNo1"));
//        models.add(new Model(R.drawable.sambook,"BookNo2","AuthorNo2"));
//        models.add(new Model(R.drawable.sambook,"BookNo3","AuthorNo3"));
//
//        Integer[] colors_temp ={
//                getResources().getColor(R.color.colorbg),
//                getResources().getColor(R.color.colorAccent),
//                getResources().getColor(R.color.colorPrimary),
//
//        };
//        colors = colors_temp;
//
//        adapter = new AwardAdapter(models,this);
//
//        viewPager = findViewById(R.id.viewPager);
//        viewPager.setAdapter((PagerAdapter) BookCelebAdapter);
//        viewPager.setPadding(130,0,130,0);
//
//        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                if (position < (adapter.getCount()-1) && position<(colors.length - 1)) {
//                    viewPager.setBackgroundColor((Integer) argbEvaluator.evaluate
//                            (positionOffset,
//                                    colors[position],
//                                    colors[position + 1]
//                            )
//                    );
//                }
//                else {
//                    viewPager.setBackgroundColor(colors[colors.length - 1]);
//                }
//                }
//
//
//
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//
//
//
//    }
//}

