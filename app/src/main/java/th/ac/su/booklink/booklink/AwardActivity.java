package th.ac.su.booklink.booklink;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import th.ac.su.booklink.booklink.Adapters.AwardAdapter;
import th.ac.su.booklink.booklink.Adapters.BookAwardAdapter;
import th.ac.su.booklink.booklink.Details.AwardDetail;
import th.ac.su.booklink.booklink.Details.BookAwardDetail;
import th.ac.su.booklink.booklink.Details.UserDetail;

public class AwardActivity extends AppCompatActivity {

    ListView simpleList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award);
        getSupportActionBar().hide();//barTop

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view_award);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_Home:
                        startActivity(new Intent(AwardActivity.this, MainActivity.class));
                        return true;
                    case R.id.item_Award:
                        // startActivity(new Intent(AwardActivity.this, AwardActivity.class));

                        return true;
                    case R.id.item_Celebrity:
                        startActivity(new Intent(AwardActivity.this, CelebActivity.class));
                        return true;
                    case R.id.item_Bookself:
                        startActivity(new Intent(AwardActivity.this, BookselfActivity.class));
                        return true;
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.item_Award);


        simpleList = (ListView) findViewById(R.id.ListAward);
        AwardAdapter customAdapter = new AwardAdapter(getApplicationContext(), AwardDetail.Item, AwardDetail.logo);
        simpleList.setAdapter(customAdapter);
        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetail.awardserect = position;
                startActivity(new Intent(AwardActivity.this, ListAwardActivity.class));


            }

        });


    }

}
