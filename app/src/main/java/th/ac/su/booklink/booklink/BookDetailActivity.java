package th.ac.su.booklink.booklink;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class BookDetailActivity extends AppCompatActivity {
    TextView NameBook, AuthorBook, TitleBook, PublisherBook, CatagoryBook;
    ImageView ImageBook;
    ImageButton btnFav, btnRead, btnWant, btnBought, btnReading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//barTop
        setContentView(R.layout.activity_book_detail);

        NameBook = (TextView) findViewById(R.id.NameBook);
        AuthorBook = (TextView) findViewById(R.id.AuthorBook);
        TitleBook = (TextView) findViewById(R.id.TitleBook);
        PublisherBook = (TextView) findViewById(R.id.PublisherBook);
        CatagoryBook = (TextView) findViewById(R.id.CatagoryBook);

        ImageBook = (ImageView) findViewById(R.id.ImageBook) ;

        btnFav = (ImageButton) findViewById(R.id.btnFav);
        btnRead = (ImageButton) findViewById(R.id.btnRead);
        btnWant = (ImageButton) findViewById(R.id.btnWant);
        btnBought = (ImageButton) findViewById(R.id.btnBought);
        btnReading = (ImageButton) findViewById(R.id.btnReading);

        String url = "https://booklink-94984.firebaseio.com/Books.json"; //หัวใหญ่
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);

                    NameBook.setText(obj.getJSONObject(UserDetail.bookserect).getString("bookname"));
                    AuthorBook.setText("นักเขียน"+obj.getJSONObject(UserDetail.bookserect).getString("authorname"));
                    TitleBook.setText(obj.getJSONObject(UserDetail.bookserect).getString("titlebook"));

                    PublisherBook.setText("สำนักพิมพ์ : "+obj.getJSONObject(UserDetail.bookserect).getString("publisherbook"));
                    CatagoryBook.setText("หมวดหมู่ : "+obj.getJSONObject(UserDetail.bookserect).getString("catagorybook"));



                    Picasso.get().load(obj.getJSONObject(UserDetail.bookserect).getString("imgbook")).into(ImageBook);

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
        RequestQueue requestQueue = Volley.newRequestQueue(BookDetailActivity.this);
        requestQueue.add(request);
    }

    public void Onclicktobookself(View view) {
        ImageButton btnSerect = (ImageButton) view;
        switch(btnSerect.getId())
        {
            case R.id.btnBack:
                super.onBackPressed();

                break;
            case R.id.btnFav:
                setStatusData("fav");
                break;
            case R.id.btnRead:
                setStatusData("read");

                break;
            case R.id.btnWant:
                setStatusData("want");
                break;
            case R.id.btnBought:
                setStatusData("bought");
                break;
            case R.id.btnReading:
                setStatusData("reading");
                break;
        }


    }

    public void setStatusData (final String type) {
        String url = "https://booklink-94984.firebaseio.com/Users/"+UserDetail.username+"/bookselfs/"+UserDetail.bookserect+".json"; //หัวใหญ่
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);

                    DatabaseReference statusReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://booklink-94984.firebaseio.com");

//                    if (type.equals("want")&& obj.getString("bought").equals("true")){
//                        statusReference.child("Users").child(UserDetail.username).child("bookselfs").child(UserDetail.bookserect).child(type)
//                                .setValue("false");
//                    }else {
//                        statusReference.child("Users").child(UserDetail.username).child("bookselfs").child(UserDetail.bookserect).child(type)
//                            .setValue(((obj.getString(type).equals("true"))? "false":"true"));
//                    }
                    //ซื้อแล้ว ห้ามอยากซื้อนะ

                    statusReference.child("Users").child(UserDetail.username).child("bookselfs").child(UserDetail.bookserect).child(type)
                            .setValue(((obj.getString(type).equals("true"))? "false":"true"));


                    switch (type) {
                        case  "fav":
                            switch(obj.getString(type))
                            {
                                case "true":
                                    btnFav.setImageResource(R.drawable.bfav);
                                    break;
                                case "false":
                                    btnFav.setImageResource(R.drawable.tfav);
                                    break;
                            }
                            break;

                        case  "read":
                            switch(obj.getString(type))
                            {
                                case "true":
                                    btnRead.setImageResource(R.drawable.common_google_signin_btn_icon_light);
                                    break;
                                case "false":
                                    btnRead.setImageResource(R.drawable.common_google_signin_btn_icon_dark);
                                    break;
                            }
                            break;

                        case  "want":
                            switch(obj.getString(type))
                            {
                                case "true":
                                    btnWant.setImageResource(R.drawable.common_google_signin_btn_icon_light);
                                    break;
                                case "false":
                                    btnWant.setImageResource(R.drawable.common_google_signin_btn_icon_dark);
                                    break;
                            }
                            break;

                        case  "bought":
                            switch(obj.getString(type))
                            {
                                case "true":
                                    btnBought.setImageResource(R.drawable.common_google_signin_btn_icon_light);
                                    break;
                                case "false":
                                    btnBought.setImageResource(R.drawable.common_google_signin_btn_icon_dark);
                                    break;
                            }
                            break;

                        case  "reading":
                            switch(obj.getString(type))
                            {
                                case "true":
                                    btnReading.setImageResource(R.drawable.common_google_signin_btn_icon_light);
                                    break;
                                case "false":
                                    btnReading.setImageResource(R.drawable.common_google_signin_btn_icon_dark);
                                    break;
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
        RequestQueue requestQueue = Volley.newRequestQueue(BookDetailActivity.this);
        requestQueue.add(request);

    }

}
