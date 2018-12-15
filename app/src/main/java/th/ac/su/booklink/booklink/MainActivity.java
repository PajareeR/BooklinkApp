package th.ac.su.booklink.booklink;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    ImageView ImageQuote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageQuote = (ImageView) findViewById(R.id.ImageQuote);

        String url = "https://booklink-94984.firebaseio.com/Books.json"; //หัวใหญ่
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);

                    Iterator i = obj.keys();
                    String key = "";

                    LinearLayout quoteBox = (LinearLayout) findViewById(R.id.quoteBox);//o


                    while (i.hasNext()) {
                        key = i.next().toString();

                        Picasso.get().load(obj.getJSONObject(key).getString("quotebook")).into(ImageQuote);

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
                        return true;
                    case R.id.item_Award:
                        return true;
                    case R.id.item_Celebrity:
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
    public void OnclickBookDetail(View view){
        UserDetail.bookserect = "9786161826192";
        startActivity(new Intent(MainActivity.this,BookDetailActivity.class));
    }
}