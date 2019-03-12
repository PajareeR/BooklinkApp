package th.ac.su.booklink.booklink.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
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

import th.ac.su.booklink.booklink.BookDetailActivity;
import th.ac.su.booklink.booklink.Details.BookAwardDetail;
import th.ac.su.booklink.booklink.Details.QuoteDetail;
import th.ac.su.booklink.booklink.Details.UserDetail;
import th.ac.su.booklink.booklink.R;

public class BookQuoteAdapter extends BaseAdapter {


    private ArrayList<QuoteDetail> list;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public BookQuoteAdapter(ArrayList<QuoteDetail> list, Context c) {
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


        View view = mLayoutInflater.inflate(R.layout.list_item_quote,parent,false);
        Holder h = new Holder();

        // set id's
        h.clickDetail = (CardView)(view.findViewById(R.id.clickDetail));
        h.messageQuote = (TextView)(view.findViewById(R.id.messageQuote));
        h.subjectMess = (TextView)(view.findViewById(R.id.subjectMess));
        h.nameBookQuote = (TextView)(view.findViewById(R.id.nameBookQuote));
        h.authorBookQuote = (TextView)(view.findViewById(R.id.authorBookQuote));
        h.imageBook = (ImageView)(view.findViewById(R.id.imageBook));

        h.messageQuote.setText('"'+list.get(position).getMessageQuote()+'"');


        h.subjectMess.setText("ประโยคเด็ดโดย : "+list.get(position).getSubjectMess());
        h.nameBookQuote.setText("หนังสือ : "+list.get(position).getNameBookQuote());
        h.authorBookQuote.setText("นักเขียน : "+list.get(position).getAuthorBookQuote());


        Picasso.get().load(list.get(position).getImageBook()).into(h.imageBook);

        h.clickDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDetail.bookserect = list.get(position).getBookQuoteId();
                mContext.startActivity(new Intent(mContext,BookDetailActivity.class));

            }
        });


        return view;
    }


    class Holder
    {
        CardView clickDetail;
        TextView messageQuote;
        TextView subjectMess;
        TextView nameBookQuote;
        TextView authorBookQuote;
        ImageView imageBook;
    }
}