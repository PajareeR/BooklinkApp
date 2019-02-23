package th.ac.su.booklink.booklink;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
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
    TextView nameBook, authorBookCeleb, nameCeleb;
    int widthDevice , hieghDevice;



    ArrayList<BookAwardDetail> bookAward = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//barTop
        setContentView(R.layout.activity_book_celeb);

        widthDevice = getWindowManager().getDefaultDisplay().getWidth();
        hieghDevice = getWindowManager().getDefaultDisplay().getHeight();

        celebBookBoxLayout = (LinearLayout) findViewById(R.id.celebBookBoxLayout);
        imageCelebBook = (ImageView) findViewById(R.id.imageCelebBook);
        nameBook = (TextView) findViewById(R.id.nameBook);
        authorBookCeleb = (TextView) findViewById(R.id.authorBookCeleb);
        nameCeleb = (TextView) findViewById(R.id.nameCeleb);


        String url = "https://booklink-94984.firebaseio.com/.json"; //หัวใหญ่
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject objceleb = new JSONObject(response).getJSONObject("Celebs").getJSONObject(UserDetail.celebserect);
                    nameCeleb.setText(objceleb.getString("name"));
                    JSONObject obj = new JSONObject(response).getJSONObject("Celebs").getJSONObject(UserDetail.celebserect).getJSONObject("book");
                    Iterator i = obj.keys();
                    String key = "";
                    while (i.hasNext()) {
                        key = i.next().toString();

                        createImage(new JSONObject(response).getJSONObject("Books").getJSONObject(key),objceleb, key);
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
    public void createImage (final JSONObject obj ,final JSONObject objCeleb , final String bookId) throws JSONException {

            CardView cardView = new CardView(this);
            int cardwidth = (int) (widthDevice * 0.8);
            int cardhiegh = (int) (hieghDevice * 0.7);
            CardView.LayoutParams params = new CardView.LayoutParams(
                    cardwidth,
                    cardhiegh
            );

        params.setMargins( (int) (widthDevice * 0.1), (int) (widthDevice * 0.1), 0,  (int) (widthDevice * 0.1));
        cardView.setCardElevation((float) (widthDevice*0.02));
        cardView.setRadius((float) (widthDevice * 0.02));
        cardView.setLayoutParams(params);

        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                (int) (hieghDevice * 0.6)
        );
        imageView.setLayoutParams(imgParams);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        Picasso.get().load( obj.getString("imgbook")).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               UserDetail.bookserect =  bookId;

//                try {
                    Log.d("log show bunble" , "ddddddddddddddddddddddddddddd");
                    Intent mIntent = new Intent(BookCelebActivity.this, BookDetailActivity.class);
//                    Bundle mBundle = new Bundle();
//                    mBundle.putString("Celeb", objCeleb.getString("name"));
//                    mBundle.putString("ReviewCeleb",  objCeleb.getJSONObject(bookId).getString("review"));
//                    mIntent.putExtras(mBundle);
                    startActivity(mIntent);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

            }

        });

        TextView textView = new TextView(this);
        LinearLayout.LayoutParams layoutParamstxt = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParamstxt.setMargins((int) (widthDevice * 0.01),(int) (widthDevice * 0.01),(int) (widthDevice * 0.01),0);
        textView.setLayoutParams(layoutParamstxt);
        textView.setTextColor(Color.BLACK);
        textView.setMaxLines(1);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize((float) (widthDevice * 0.04));
        Typeface type = ResourcesCompat.getFont(this, R.font.sukhumvitsetbold);
        textView.setTypeface(type);
        textView.setText(obj.getString("bookname"));

        TextView textViewAuthor = new TextView(this);
        LinearLayout.LayoutParams layoutParamstextViewAuthor = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParamstxt.setMargins((int) (widthDevice * 0.01),(int) (widthDevice * 0.01),(int) (widthDevice * 0.01),0);
        textViewAuthor.setLayoutParams(layoutParamstextViewAuthor);
        textViewAuthor.setTextColor(Color.BLACK);
        textViewAuthor.setMaxLines(1);
        textViewAuthor.setGravity(Gravity.CENTER);
        textViewAuthor.setTextSize((float) (widthDevice * 0.03));

        textViewAuthor.setTypeface(type);

        textViewAuthor.setText("นักเขียน : "+obj.getString("authorname"));


        celebBookBoxLayout.addView(cardView);
        cardView.addView(linearLayout);
        linearLayout.addView(imageView);
        linearLayout.addView(textView);
        linearLayout.addView(textViewAuthor);

    }

    public String  getBook(JSONObject obj){

        try {
            return obj.getString("imgbook");
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return "";
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

