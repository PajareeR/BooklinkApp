package th.ac.su.booklink.booklink;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import th.ac.su.booklink.booklink.Adapters.BookQuoteAdapter;
import th.ac.su.booklink.booklink.Adapters.BookSearchAdapter;
import th.ac.su.booklink.booklink.Adapters.ProAdapter;
import th.ac.su.booklink.booklink.Details.AwardDetail;
import th.ac.su.booklink.booklink.Details.BookAwardDetail;
import th.ac.su.booklink.booklink.Details.CommentDetail;
import th.ac.su.booklink.booklink.Details.ProDetail;
import th.ac.su.booklink.booklink.Details.QuoteDetail;
import th.ac.su.booklink.booklink.Details.UserDetail;

import static java.util.Comparator.comparing;

public class MainActivity extends AppCompatActivity {
    ListView listQuote, listPromotion;
    TextView messageQuote, subjectMess, nameBookQuote, authorBookQuote, nameBookPro, authorBookPro, lobPro, datePro;
    ImageView imageBook, imageBookPro;
    public ListView listSearch;

    ArrayList<BookAwardDetail> bookSearch = new ArrayList<>();
    ArrayList<CommentDetail> arrComment = new ArrayList<>();
    ArrayList<QuoteDetail> arrQuote = new ArrayList<>();
    ArrayList<ProDetail> arrPro = new ArrayList<>();
    Context mcontext = MainActivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//barTop
        setContentView(R.layout.activity_main);
        listQuote = (ListView) findViewById(R.id.listQuote);
        listPromotion = (ListView) findViewById(R.id.listPromotion);

        messageQuote = (TextView) findViewById(R.id.messageQuote);
        subjectMess = (TextView) findViewById(R.id.subjectMess);
        nameBookQuote = (TextView) findViewById(R.id.nameBookQuote);
        authorBookQuote = (TextView) findViewById(R.id.authorBookQuote);

        nameBookPro = (TextView) findViewById(R.id.nameBookPro);
        authorBookPro = (TextView) findViewById(R.id.authorBookPro);
        lobPro = (TextView) findViewById(R.id.lobPro);
        datePro = (TextView) findViewById(R.id.datePro);


        imageBook = (ImageView) findViewById(R.id.imageBook);
        imageBookPro = (ImageView) findViewById(R.id.imageBookPro);


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
                        if (obj.getJSONObject(key).has("quote")) {
                            if (obj.getJSONObject(key).getJSONObject("quote").getString("status").equals("true")) {
                                JSONObject objbook = obj.getJSONObject(key);
                                JSONObject objQuote = obj.getJSONObject(key).getJSONObject("quote");
                                String mode = objQuote.getString("selectQuote");
                                if (mode.equals("comment")) {
                                    JSONObject objComments = objbook.getJSONObject("comments");
                                    Iterator j = objComments.keys();
                                    String keycomment = "";
                                    while (j.hasNext()) {
                                        keycomment = j.next().toString();
                                        JSONObject objComment = objComments.getJSONObject(keycomment);
                                        JSONObject objLike = (objComment.has("userlike") ?
                                                objComment.getJSONObject("userlike") : new JSONObject());
                                        arrComment.add(new CommentDetail(
                                                objComment.getString("user"),
                                                objComment.getString("comment"),
                                                calLike(objLike)

                                        ));
                                    }
                                    CommentDetail maxValue = arrComment.stream().max(comparing(CommentDetail::getCountLike)).get();
                                    arrQuote.add(new QuoteDetail(
                                            key, maxValue.getComment(),
                                            maxValue.getCommentUser(),
                                            objbook.getString("imgbook"),
                                            objbook.getString("bookname"),
                                            objbook.getString("authorname")));
                                } else {
                                    arrQuote.add(new QuoteDetail(
                                            key, objQuote.getJSONObject("other").getString("title"),
                                            objQuote.getJSONObject("other").getString("author"),
                                            objbook.getString("imgbook"),
                                            objbook.getString("bookname"),
                                            objbook.getString("authorname")));
                                }
                            }


                        }

                        bookSearch.add(new BookAwardDetail(
                                key,
                                obj.getJSONObject(key).getString("bookname"),
                                obj.getJSONObject(key).getString("authorname"),
                                obj.getJSONObject(key).getString("imgbook")
                        ));
//                        arrPro.add(new ProDetail(
//                                key,
//                                obj.getJSONObject(key).getString("bookname"),
//                                obj.getJSONObject(key).getString("authorname"),
//                                obj.getJSONObject(key).getString("imgbook"),
//                                obj.getJSONObject(key).getJSONObject("award")
//                        ));
//
//                        private  String proId;
//                        private  String nameBookPro;
//                        private  String authorBookPro;
//                        private  String lobPro;
//                        private  String imageBookPro;
//                        private  String datePro;


                    }


                    BookQuoteAdapter bookQuoteAdapter = new BookQuoteAdapter(arrQuote, mcontext);
                    listQuote.setAdapter(bookQuoteAdapter);
                    bookQuoteAdapter.notifyDataSetChanged();
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


    public int calLike(JSONObject obj) {
        int count = 0;
        try {
            Iterator j = obj.keys();
            String key = "";
            while (j.hasNext()) {
                key = j.next().toString();

                if (obj.getJSONObject(key).getString("status").equals("like")) {
                    count += 1;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return count;
    }

    public void OnclickBookDetail(String bookName) {
        UserDetail.bookserect = bookName;
        startActivity(new Intent(MainActivity.this, BookDetailActivity.class));
    }

    public void OnTest(View view) {
        startActivity(new Intent(MainActivity.this, BookselfActivity.class));
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

                    for (int i = 0; i < bookSearch.size(); i++) {
                        Pattern patternsearch = Pattern.compile(word);
                        Matcher matchertitlesearch = patternsearch.matcher(bookSearch.get(i).getTitle());
                        Matcher matcherauthorsearch = patternsearch.matcher(bookSearch.get(i).getAuthoor());
                        if (matchertitlesearch.find() || matcherauthorsearch.find()) {
                            search.add(bookSearch.get(i));
                        }
                    }

                    BookSearchAdapter customAdapter = new BookSearchAdapter(search, MainActivity.this);
                    listSearch.setAdapter(customAdapter);
                    listSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            UserDetail.bookserect = search.get(position).getId();
                            startActivity(new Intent(MainActivity.this, BookDetailActivity.class));

                        }
                    });
                    listPromotion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            UserDetail.bookserect = arrPro.get(position).getProId();
                            startActivity(new Intent(MainActivity.this, BookDetailActivity.class));

                        }
                    });
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        final PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT, 1000, true);
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