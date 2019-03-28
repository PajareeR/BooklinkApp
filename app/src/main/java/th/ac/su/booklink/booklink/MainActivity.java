package th.ac.su.booklink.booklink;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import th.ac.su.booklink.booklink.Adapters.BookSearchAdapter;
import th.ac.su.booklink.booklink.Adapters.MyAdapter;
import th.ac.su.booklink.booklink.Adapters.ProAdapter;
import th.ac.su.booklink.booklink.Details.BookAwardDetail;
import th.ac.su.booklink.booklink.Details.CommentDetail;
import th.ac.su.booklink.booklink.Details.ProDetail;
import th.ac.su.booklink.booklink.Details.QuoteDetail;
import th.ac.su.booklink.booklink.Details.UserDetail;

import static java.util.Comparator.comparing;

public class MainActivity extends AppCompatActivity {
    ListView listPromotion;
    TextView messageQuote, subjectMess, nameBookQuote, authorBookQuote, nameBookPro, authorBookPro, lobPro, datePro;
    ImageView imageBook, imageBookPro;
    public ListView listSearch;

    ArrayList<BookAwardDetail> bookSearch = new ArrayList<>();
    ArrayList<CommentDetail> arrComment = new ArrayList<>();
    ArrayList<QuoteDetail> arrQuote = new ArrayList<>();
    ArrayList<ProDetail> arrPro = new ArrayList<>();
    Context mcontext = MainActivity.this;
    MyAdapter adapter ;

    RecyclerView recyclerViewQuote;
//    View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//barTop
        setContentView(R.layout.activity_main);
        listPromotion = (ListView) findViewById(R.id.listPromotion);

        recyclerViewQuote = (RecyclerView) findViewById(R.id.my_recycler_view);

        adapter = new MyAdapter(arrQuote);
        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(getApplicationContext());
        recyclerViewQuote.setLayoutManager(mLayoutManager);
        recyclerViewQuote.setItemAnimator(new DefaultItemAnimator());
        recyclerViewQuote.setAdapter(adapter);

        recyclerViewQuote.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerViewQuote, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                UserDetail.bookserect = arrQuote.get(position).getBookQuoteId();
                startActivity(new Intent(mcontext, BookDetailActivity.class));
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


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

        DatabaseReference promotionReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://booklink-94984.firebaseio.com/Promotion");

        promotionReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Map<String, String> map = (Map) ds.getValue();

                    arrPro.add(new ProDetail(ds.getKey(),map.get("namebook"),map.get("author"),map.get("lobpro"),map.get("bookimg"),map.get("exp")));

//                    (String proId, String nameBookPro, String authorBookPro, String lobPro, String imageBookPro, String datePro)
                }

                ProAdapter proAdapter = new ProAdapter(arrPro,mcontext);
                listPromotion.setAdapter(proAdapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });




//        // Get intent, action and MIME type
//        Intent intent = getIntent();
//        String action = intent.getAction();
//        String type = intent.getType();
//
//        if (Intent.ACTION_SEND.equals(action) && type != null) {
//            if ("text/plain".equals(type)) {
//                handleSendText(intent); // Handle text being sent
//            } else if (type.startsWith("image/")) {
//                handleSendImage(intent); // Handle single image being sent
//            }
//        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
//            if (type.startsWith("image/")) {
//                handleSendMultipleImages(intent); // Handle multiple images being sent
//            }
//        } else {
//            // Handle other intents, such as being started from the home screen
//        }
////    ...
//    }
//
//    void handleSendText(Intent intent) {
//        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
//        if (sharedText != null) {
//            // Update UI to reflect text being shared
//        }
//    }
//
//    void handleSendImage(Intent intent) {
//        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
//        if (imageUri != null) {
//            // Update UI to reflect image being shared
//        }
//    }
//
//    void handleSendMultipleImages(Intent intent) {
//        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
//        if (imageUris != null) {
//            // Update UI to reflect multiple images being shared
//        }






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


                    }






//
//                    BookQuoteAdapter bookQuoteAdapter = new BookQuoteAdapter(arrQuote, mcontext);
//                    listQuote.setAdapter(bookQuoteAdapter);
//                    bookQuoteAdapter.notifyDataSetChanged();

//                    adapter = new MyAdapter(arrQuote,mcontext);
//
//                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//                    recyclerView.setLayoutManager(mLayoutManager);
//                    recyclerView.setItemAnimator(new DefaultItemAnimator());
//                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
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



//    public String storeImage(Bitmap capture, String s) {
//        return s;
//    }



//    private String storeImage(Bitmap capture, String s) {
//
//    }


//    int PERMISSION_REQUEST_CODE;
//    public void Share(View view, View rootView, String message) {
//        if (!readyToShare && Build.VERSION.SDK_INT>=23) {
//            ensurePermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            return;
//        }
//    }
//
//    private void ensurePermissions(String writeExternalStorage) {
//        boolean request = false;
//
//
//    }

//



















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
        UserDetail.proserect = bookName;
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