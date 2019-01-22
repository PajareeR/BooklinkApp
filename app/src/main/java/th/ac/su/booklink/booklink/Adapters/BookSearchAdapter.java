package th.ac.su.booklink.booklink.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import th.ac.su.booklink.booklink.Details.BookAwardDetail;
import th.ac.su.booklink.booklink.R;

public class BookSearchAdapter extends BaseAdapter {


    private ArrayList<BookAwardDetail> list;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public BookSearchAdapter(ArrayList<BookAwardDetail> list, Context c) {
        this.list = list;
        this.mContext = c;
        mLayoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        View view = mLayoutInflater.inflate(R.layout.list_item_book_award,parent,false);
        Holder h = new Holder();

        // set id's
        h.bookName = (TextView)(view.findViewById(R.id.bookName));

        h.bookName.setText(list.get(position).getTitle());

        h.authorName = (TextView)(view.findViewById(R.id.authorName));
        h.authorName.setText(list.get(position).getAuthoor());

        h.imgBook = (ImageView)(view.findViewById(R.id.imgBook));

        Picasso.get().load(list.get(position).getImgPath()).into(h.imgBook);


        return view;
    }


    private class Holder
    {
        TextView bookName;
        TextView authorName;
        ImageView imgBook;
    }
}