package th.ac.su.booklink.booklink;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class AwardActivity extends AppCompatActivity {

    ListView simpleList;
    String  Item[] = {"รางวัลซีไรต์", "รางวัลหนังสือดีเด่น กระทรวงศึกษาธิการ", "รางวัลเซเว่นบุ๊คอวอร์ด", "รางวัลนายอินทร์อะวอร์ด", "รางวัลแว่นแก้ว", "รางวัลเรื่องสั้นมูลนิธิ สุภาว์ เทวกุล", "รางวัลพานแว่นฟ้า", "รางวัลสมาคมนักเขียนแห่งประเทศไทย"};
    int flags[] = {R.drawable.sewrite, R.drawable.suksa, R.drawable.sevenbooks, R.drawable.nay, R.drawable.vankeaw, R.drawable.supa, R.drawable.pan, R.drawable.samakom};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award);
        getSupportActionBar().hide();//barTop


        simpleList = (ListView)findViewById(R.id.ListAward);
        AwardAdapter customAdapter = new AwardAdapter(getApplicationContext(), Item, flags);
        simpleList.setAdapter(customAdapter);
    }
}
