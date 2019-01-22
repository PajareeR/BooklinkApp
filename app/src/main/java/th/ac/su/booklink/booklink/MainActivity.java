package th.ac.su.booklink.booklink;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import th.ac.su.booklink.booklink.Adapters.BookAwardAdapter;
import th.ac.su.booklink.booklink.Adapters.BookSearchAdapter;
import th.ac.su.booklink.booklink.Details.BookAwardDetail;
import th.ac.su.booklink.booklink.Details.UserDetail;

public class MainActivity extends AppCompatActivity {
    LinearLayout quoteBoxLayout;
    ArrayList<BookAwardDetail> bookSearch = new ArrayList<>();



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

                            layoutParams.setMargins(0,30,0,0);

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
                        bookSearch.add( new BookAwardDetail(
                                key,
                                obj.getJSONObject(key).getString("bookname"),
                                obj.getJSONObject(key).getString("authorname"),
                                obj.getJSONObject(key).getString("imgbook")
                        ));






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
    public void OnTest(View view){
        startActivity(new Intent(MainActivity.this,BookselfActivity.class));
    }


    public void onButtonShowPopupWindowClick(View view) {



        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_search, null);


        EditText edtSearch = popupView.findViewById(R.id.edtSearch);
        final ListView listSearch = popupView.findViewById(R.id.listSearch);



        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                ArrayList<BookAwardDetail> search = new ArrayList<>();

                String searchStr = String.valueOf(s);

                Pattern patternword = Pattern.compile("[\\w\\s*]+");
                Matcher matcherword = patternword.matcher(searchStr);
                if (matcherword.find()) {
                    String word = searchStr.substring(matcherword.start(), matcherword.end());

                    for (int i = 0 ; i < bookSearch.size(); i++){
                        Pattern patternsearch = Pattern.compile(word);
                        Matcher matchertitlesearch = patternsearch.matcher(bookSearch.get(i).getTitle());
                        Matcher matcherauthorsearch = patternsearch.matcher(bookSearch.get(i).getAuthoor());
                        if (matchertitlesearch.find() || matcherauthorsearch.find() ) {
                            search.add(bookSearch.get(i));
                        }
                    }

                    BookSearchAdapter customAdapter = new BookSearchAdapter(search,MainActivity.this);
                    listSearch.setAdapter(customAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        final PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, 1000, true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
}