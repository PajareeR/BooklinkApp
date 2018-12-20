package th.ac.su.booklink.booklink;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class AwardActivity extends AppCompatActivity {

    ListView simpleList;
    String  Item[] = {"Apple", "Banana", "Lemon", "Cherry", "Strawberry", "Avocado"};
    int flags[] = {R.drawable.sewrite, R.drawable.samakom, R.drawable.sevenbooks, R.drawable.nay, R.drawable.pan, R.drawable.vankeaw};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award);

        simpleList = (ListView)findViewById(R.id.ListView);
        AwardAdapter customAdapter = new AwardAdapter(getApplicationContext(), Item, flags);
        simpleList.setAdapter(customAdapter);
    }
}
