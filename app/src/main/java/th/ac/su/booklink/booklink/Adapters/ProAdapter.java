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

import java.util.ArrayList;

import th.ac.su.booklink.booklink.BookDetailActivity;
import th.ac.su.booklink.booklink.Details.ProDetail;
import th.ac.su.booklink.booklink.Details.UserDetail;
import th.ac.su.booklink.booklink.R;

public class ProAdapter extends BaseAdapter {

    private ArrayList<ProDetail> list;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public ProAdapter(ArrayList<ProDetail> list, Context c) {
        this.list = list;
        this.mContext = c;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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


        View view = mLayoutInflater.inflate(R.layout.list_item_promotion, parent, false);
        Holder h = new Holder();

        // set id's
        h.clickDetaill = (CardView) (view.findViewById(R.id.clickDetaill));
        h.nameBookPro = (TextView) (view.findViewById(R.id.nameBookPro));
        h.authorBookPro = (TextView) (view.findViewById(R.id.authorBookPro));
        h.lobPro = (TextView) (view.findViewById(R.id.lobPro));
        h.datePro = (TextView) (view.findViewById(R.id.datePro));
        h.imageBookPro = (ImageView) (view.findViewById(R.id.imageBookPro));

        h.nameBookPro.setText(list.get(position).getNameBookPro());
        h.authorBookPro.setText("นักเขียน : " + list.get(position).getAuthorBookPro());
        h.lobPro.setText(list.get(position).getLobPro());
        h.datePro.setText("รหัสหมดอายุวันที่ : " + list.get(position).getDatePro());


        Picasso.get().load(list.get(position).getImageBookPro()).into(h.imageBookPro);

        h.clickDetaill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDetail.proserect = list.get(position).getProId();
                mContext.startActivity(new Intent(mContext, BookDetailActivity.class));

            }
        });


        return view;
    }


    class Holder {
        CardView clickDetaill;
        TextView nameBookPro;
        TextView authorBookPro;
        TextView lobPro;
        TextView datePro;
        ImageView imageBookPro;
    }
}
