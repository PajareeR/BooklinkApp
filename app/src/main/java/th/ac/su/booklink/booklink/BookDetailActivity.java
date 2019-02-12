package th.ac.su.booklink.booklink;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import th.ac.su.booklink.booklink.Details.UserDetail;

public class BookDetailActivity extends AppCompatActivity {
    TextView NameBook, AuthorBook, TitleBook, PublisherBook, CategoryBook, ISBNBook, AdditionBook;
    ImageView ImageBook;
    ImageButton btnFav, btnRead, btnWant, btnBought, btnReading;
    EditText edtComment;
    Button btnSendComment;

    LinearLayout commentBox;

    DatabaseReference commentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//barTop
        setContentView(R.layout.activity_book_detail);

        NameBook = (TextView) findViewById(R.id.NameBook);
        AuthorBook = (TextView) findViewById(R.id.AuthorBook);
        TitleBook = (TextView) findViewById(R.id.TitleBook);
        PublisherBook = (TextView) findViewById(R.id.PublisherBook);
        CategoryBook = (TextView) findViewById(R.id.CatagoryBook);
        ISBNBook = (TextView) findViewById(R.id.ISBNBook); //xx
        AdditionBook = (TextView) findViewById(R.id.AdditionBook);
        btnSendComment = (Button) findViewById(R.id.btnSendComment);


        ImageBook = (ImageView) findViewById(R.id.ImageBook) ;

        btnFav = (ImageButton) findViewById(R.id.btnFav);
        btnRead = (ImageButton) findViewById(R.id.btnRead);
        btnWant = (ImageButton) findViewById(R.id.btnWant);
        btnBought = (ImageButton) findViewById(R.id.btnBought);
        btnReading = (ImageButton) findViewById(R.id.btnReading);

        edtComment = (EditText)findViewById(R.id.edtComment);

        commentBox = (LinearLayout)findViewById(R.id.commentBox);

        String url = "https://booklink-94984.firebaseio.com/Books.json"; //หัวใหญ่
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);

                    NameBook.setText(obj.getJSONObject(UserDetail.bookserect).getString("bookname"));
                    AuthorBook.setText("นักเขียน : "+obj.getJSONObject(UserDetail.bookserect).getString("authorname"));
                    TitleBook.setText(obj.getJSONObject(UserDetail.bookserect).getString("titlebook"));

                    PublisherBook.setText("สำนักพิมพ์ : "+obj.getJSONObject(UserDetail.bookserect).getString("publisherbook"));
                    CategoryBook.setText("หมวดหมู่ : "+obj.getJSONObject(UserDetail.bookserect).getString("catagorybook"));
                    ISBNBook.setText("รหัส ISBN : "+UserDetail.bookserect); //XX
                    AdditionBook.setText(obj.getJSONObject(UserDetail.bookserect).getString("additionbook"));

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

        setStatusData("load");

        btnSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertComment();

            }
        });

        commentReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://booklink-94984.firebaseio.com/Books/"+UserDetail.bookserect+"/comments");
        commentReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String, String> map = (Map)dataSnapshot.getValue();
                String comment = map.get("comment").toString();
                String user = map.get("user").toString();

                TextView textComment = new TextView(BookDetailActivity.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                textComment.setText(user + "\n"+comment);
                textComment.setLayoutParams(layoutParams);


                commentBox.addView(textComment);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    private void insertComment() {
        String comment = edtComment.getText().toString() ;
        String id = UUID.randomUUID().toString();

        if (!comment.equals("")){
            Map<String, String> map = new HashMap<String, String>();
            map.put("user", UserDetail.username);
            map.put("comment", comment);
            commentReference.push().setValue(map);
            edtComment.setText("");
        }
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

        String url = "https://booklink-94984.firebaseio.com/Users/"+UserDetail.username+"/bookselfs.json"; //หัวใหญ่
        Log.d("sss",url);
        final StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                DatabaseReference statusReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://booklink-94984.firebaseio.com");

                if(response.equals("null")){
                    statusReference.child("Users").child(UserDetail.username).child("bookselfs").child(UserDetail.bookserect).child("fav")
                            .setValue("false");
                    statusReference.child("Users").child(UserDetail.username).child("bookselfs").child(UserDetail.bookserect).child("read")
                            .setValue("false");
                    statusReference.child("Users").child(UserDetail.username).child("bookselfs").child(UserDetail.bookserect).child("want")
                            .setValue("false");
                    statusReference.child("Users").child(UserDetail.username).child("bookselfs").child(UserDetail.bookserect).child("bought")
                            .setValue("false");
                    statusReference.child("Users").child(UserDetail.username).child("bookselfs").child(UserDetail.bookserect).child("reading")
                            .setValue("false");

                    setStatusData(type);
                }else {
                    try {
                        JSONObject objBookSelfs = new JSONObject(response);
                        if (!objBookSelfs.has(UserDetail.bookserect)) {

                            statusReference.child("Users").child(UserDetail.username).child("bookselfs").child(UserDetail.bookserect).child("fav")
                                    .setValue("false");
                            statusReference.child("Users").child(UserDetail.username).child("bookselfs").child(UserDetail.bookserect).child("read")
                                    .setValue("false");
                            statusReference.child("Users").child(UserDetail.username).child("bookselfs").child(UserDetail.bookserect).child("want")
                                    .setValue("false");
                            statusReference.child("Users").child(UserDetail.username).child("bookselfs").child(UserDetail.bookserect).child("bought")
                                    .setValue("false");
                            statusReference.child("Users").child(UserDetail.username).child("bookselfs").child(UserDetail.bookserect).child("reading")
                                    .setValue("false");

                            setStatusData(type);
                        } else {
                            JSONObject obj = objBookSelfs.getJSONObject(UserDetail.bookserect);
                            switch (obj.getString("fav")) {
                                case "true":
                                    btnFav.setImageResource(R.drawable.bfav);
                                    break;
                                case "false":
                                    btnFav.setImageResource(R.drawable.tfav);
                                    Toast.makeText(getApplicationContext(),"บันทึกลงรายการหนังสือเล่มโปรด",Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            switch (obj.getString("read")) {
                                case "true":
                                    btnRead.setImageResource(R.drawable.bread);
                                    break;
                                case "false":
                                    btnRead.setImageResource(R.drawable.tread);
                                    Toast.makeText(getApplicationContext(),"บันทึกลงรายการหนังสืออ่านแล้ว",Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            switch (obj.getString("want")) {
                                case "true":
                                    btnWant.setImageResource(R.drawable.bwant);
                                    break;
                                case "false":
                                    btnWant.setImageResource(R.drawable.twant);
                                    Toast.makeText(getApplicationContext(),"บันทึกลงรายการหนังสืออยากซื้อ",Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            switch (obj.getString("bought")) {
                                case "true":
                                    btnBought.setImageResource(R.drawable.bbought);
                                    break;
                                case "false":
                                    btnBought.setImageResource(R.drawable.tbought);
                                    Toast.makeText(getApplicationContext(),"บันทึกลงรายการหนังสือซื้อแล้ว",Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            switch (obj.getString("reading")) {
                                case "true":
                                    btnReading.setImageResource(R.drawable.breading);
                                    break;
                                case "false":
                                    btnReading.setImageResource(R.drawable.treading);
                                    Toast.makeText(getApplicationContext(),"บันทึกลงรายการหนังสือเกำลังอ่าน",Toast.LENGTH_SHORT).show();
                                    break;
                            }

                            if (type != "load") {
                                statusReference.child("Users").child(UserDetail.username).child("bookselfs").child(UserDetail.bookserect).child(type)
                                        .setValue(((obj.getString(type).equals("true")) ? "false" : "true"));

                                setChangeStatus(type, obj.getString(type));
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

    private void setChangeStatus(String type, String status) {
                    switch (type) {
                        case  "fav":
                            switch(status)
                            {
                                case "true":
                                    btnFav.setImageResource(R.drawable.tfav);
                                    break;
                                case "false":
                                    btnFav.setImageResource(R.drawable.bfav);
                                    break;
                            }
                            break;

                        case  "read":
                            switch(status)
                            {
                                case "true":
                                    btnRead.setImageResource(R.drawable.tread);
                                    break;
                                case "false":
                                    btnRead.setImageResource(R.drawable.bread);
                                    break;
                            }
                            break;

                        case  "want":
                            switch(status)
                            {
                                case "true":
                                    btnWant.setImageResource(R.drawable.twant);
                                    break;
                                case "false":
                                    btnWant.setImageResource(R.drawable.bwant);
                                    break;
                            }
                            break;

                        case  "bought":
                            switch(status)
                            {
                                case "true":
                                    btnBought.setImageResource(R.drawable.tbought);
                                    break;
                                case "false":
                                    btnBought.setImageResource(R.drawable.bbought);
                                    break;
                            }
                            break;

                        case  "reading":
                            switch(status)
                            {
                                case "true":
                                    btnReading.setImageResource(R.drawable.treading);
                                    break;
                                case "false":
                                    btnReading.setImageResource(R.drawable.breading);
                                    break;
                            }
                    }
    }

}
