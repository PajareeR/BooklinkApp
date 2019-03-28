package th.ac.su.booklink.booklink.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import th.ac.su.booklink.booklink.Details.QuoteDetail;
import th.ac.su.booklink.booklink.R;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<QuoteDetail> list;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView clickDetail;
        TextView messageQuote;
        TextView subjectMess;
        TextView nameBookQuote;
        TextView authorBookQuote;
        ImageView imageBook;

        public MyViewHolder(View view) {
            super(view);
            clickDetail = (CardView) (view.findViewById(R.id.clickDetail));
            messageQuote = (TextView) (view.findViewById(R.id.messageQuote));
            subjectMess = (TextView) (view.findViewById(R.id.subjectMess));
            nameBookQuote = (TextView) (view.findViewById(R.id.nameBookQuote));
            authorBookQuote = (TextView) (view.findViewById(R.id.authorBookQuote));
            imageBook = (ImageView) (view.findViewById(R.id.imageBook));

        }
    }


    public MyAdapter(ArrayList<QuoteDetail> list) {
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_quote, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder h, int position) {

        Log.d("test " , "show me !!!!!");
        h.messageQuote.setText('"' + list.get(position).getMessageQuote() + '"');


        h.subjectMess.setText("ประโยคเด็ดโดย : " + list.get(position).getSubjectMess());
        h.nameBookQuote.setText("หนังสือ : " + list.get(position).getNameBookQuote());
        h.authorBookQuote.setText("นักเขียน : " + list.get(position).getAuthorBookQuote());


        Picasso.get().load(list.get(position).getImageBook()).into(h.imageBook);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}