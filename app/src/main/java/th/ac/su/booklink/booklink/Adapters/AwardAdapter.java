package th.ac.su.booklink.booklink.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import th.ac.su.booklink.booklink.R;

public class AwardAdapter extends BaseAdapter {

    Context context;
    String Item[];
    int flags[];
    LayoutInflater inflter;
    View view;


    public AwardAdapter(Context context, String[] Item, int[] flags) {
        this.context = context;
        this.Item = Item;
        this.flags = flags;
        inflter = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return Item.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {

        view = inflter.inflate(R.layout.list_item_award,parent,false);
        TextView item = (TextView) view.findViewById(R.id.item);
        ImageView image = (ImageView) view.findViewById(R.id.image);
        item.setText(Item[i]);
        image.setImageResource(flags[i]);
        return view;
    }
}


