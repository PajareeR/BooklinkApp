package th.ac.su.booklink.booklink;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import th.ac.su.booklink.booklink.Adapters.BookAwardAdapter;
import th.ac.su.booklink.booklink.Adapters.BookSearchAdapter;
import th.ac.su.booklink.booklink.Details.BookAwardDetail;
import th.ac.su.booklink.booklink.Details.CommentDetail;
import th.ac.su.booklink.booklink.Details.UserDetail;

import static java.util.Comparator.comparing;

public class MainActivity extends AppCompatActivity {
    LinearLayout quoteBoxLayout;
    ArrayList<BookAwardDetail> bookSearch = new ArrayList<>();
    public ListView listSearch;

    int widthDevice , hieghDevice;
    ArrayList<CommentDetail> arrComment = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//barTop
        setContentView(R.layout.activity_main);
        quoteBoxLayout  = (LinearLayout) findViewById(R.id.quoteBox);

        widthDevice = getWindowManager().getDefaultDisplay().getWidth();
        hieghDevice = getWindowManager().getDefaultDisplay().getHeight();


        String url = "https://booklink-94984.firebaseio.com/Books.json"; //หัวใหญ่
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);

                    Iterator i = obj.keys();
                    String key = "";


                    while (i.hasNext()) {
                        key = i.next().toString();
                        if (obj.getJSONObject(key).has("quote")){
                            if (obj.getJSONObject(key).getJSONObject("quote").getString("status").equals("true")){
                                JSONObject objbook = obj.getJSONObject(key);
                                JSONObject objQuote = obj.getJSONObject(key).getJSONObject("quote");
                                String mode = objQuote.getString("selectQuote");

                                if (mode.equals("comment")){
                                    JSONObject objComments = objbook.getJSONObject("comments");
                                    Iterator j = objComments.keys();
                                    String keycomment = "";
                                    while (j.hasNext()) {
                                        keycomment = j.next().toString();
                                        JSONObject objComment = objComments.getJSONObject(keycomment);
                                        JSONObject objLike = (objComment.has("userlike")?
                                                objComment.getJSONObject("userlike"):new JSONObject());
                                        arrComment.add(new CommentDetail(
                                                objComment.getString("user"),
                                                objComment.getString("comment"),
                                                calLike(objLike)

                                        ));
                                    }

                                    CommentDetail maxValue = arrComment.stream().max(comparing(CommentDetail::getCountLike)).get();

                                    createImage(objbook,key);




//                                    TextView textView = new TextView(MainActivity.this);
//                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                                            LinearLayout.LayoutParams.MATCH_PARENT,
//                                            LinearLayout.LayoutParams.WRAP_CONTENT);
//                                    textView.setLayoutParams(layoutParams);
//                                    textView.setText("Comment : " + maxValue.getComment() + "\n"
//                                            + "Author : " + maxValue.getCommentUser()+"\n"+ "Book name : "
//                                            + objbook.getString("bookname") +"\n" + " Book Author : "
//                                            + objbook.getString("authorname"));
//                                    quoteBoxLayout.addView(textView);
                                }else {

                                    createImage(objbook,key);
//                                    TextView textView = new TextView(MainActivity.this);
//                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                                            LinearLayout.LayoutParams.MATCH_PARENT,
//                                            LinearLayout.LayoutParams.WRAP_CONTENT);
//                                    textView.setLayoutParams(layoutParams);
//                                    textView.setText("Other : " + objQuote.getJSONObject("other").getString("title") + "\n"
//                                            + "Author : " + objQuote.getJSONObject("other").getString("author")+"\n"+ "Book name : "
//                                            + objbook.getString("bookname") +"\n" + " Book Author : "
//                                            + objbook.getString("authorname"));
//                                    quoteBoxLayout.addView(textView);
                                }

                            }
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
            System.out.println("error "+error);
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
                        startActivity(new Intent(MainActivity.this, CelebActivity.class));
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


    public void createImage (final JSONObject obj , final String bookId) throws JSONException {

        Log.d("create image ","null");
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
                startActivity(new Intent(MainActivity.this , BookDetailActivity.class));
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


        quoteBoxLayout.addView(cardView);
        cardView.addView(linearLayout);
        linearLayout.addView(imageView);
        linearLayout.addView(textView);
        linearLayout.addView(textViewAuthor);

    }
    public int calLike(JSONObject obj){
        int count = 0;
        try {
            Iterator j = obj.keys();
            String key = "";
            while (j.hasNext()) {
                key = j.next().toString();

                if ( obj.getJSONObject(key).getString("status").equals("like")){
                    count += 1;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return count;
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

                final ArrayList<BookAwardDetail> search = new ArrayList<>();

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
                    listSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            UserDetail.bookserect = search.get(position).getId();
                            startActivity(new Intent(MainActivity.this, BookDetailActivity.class));

                        }
                    });
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