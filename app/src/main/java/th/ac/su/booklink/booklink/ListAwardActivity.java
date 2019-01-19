package th.ac.su.booklink.booklink;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import th.ac.su.booklink.booklink.Adapters.AwardAdapter;
import th.ac.su.booklink.booklink.Adapters.BookAwardAdapter;
import th.ac.su.booklink.booklink.Details.AwardDetail;
import th.ac.su.booklink.booklink.Details.BookAwardDetail;
import th.ac.su.booklink.booklink.Details.UserDetail;

public class ListAwardActivity extends AppCompatActivity {

    public TextView titileAward;
    public ListView ListAward;

    ArrayList<BookAwardDetail> bookAward = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//barTop

        setContentView(R.layout.activity_list_award);

        titileAward = (TextView) findViewById(R.id.titleAward);
        ListAward = (ListView) findViewById(R.id.ListAward);

        titileAward.setText(AwardDetail.Item[UserDetail.awardserect]);

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



                        if (obj.getJSONObject(key).getJSONObject("award")
                                .has(AwardDetail.ItemId[UserDetail.awardserect])){


                            bookAward.add( new BookAwardDetail(
                                    key,
                                    obj.getJSONObject(key).getString("bookname"),
                                    obj.getJSONObject(key).getString("authorname"),
                                    obj.getJSONObject(key).getString("imgbook"),
                                    obj.getJSONObject(key).getJSONObject("award")
                            ));

                        }

                    }

                    BookAwardAdapter customAdapter = new BookAwardAdapter(bookAward,ListAwardActivity.this);
                    ListAward.setAdapter(customAdapter);

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
        RequestQueue requestQueue = Volley.newRequestQueue(ListAwardActivity.this);
        requestQueue.add(request);

        ListAward.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetail.bookserect = bookAward.get(position).getId();
                startActivity(new Intent(ListAwardActivity.this, BookDetailActivity.class));

            }
        });

    }
}
