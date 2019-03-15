//package th.ac.su.booklink.booklink.Adapters;
//
//public class BookCelebAdapter {
//}
//package th.ac.su.booklink.booklink;
//
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//
//public class BookCelebActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_book_celeb);
//    }
//}
package th.ac.su.booklink.booklink.Adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import th.ac.su.booklink.booklink.Model;
import th.ac.su.booklink.booklink.R;


public class BookCelebAdapter extends PagerAdapter {

    private List<Model> models;
    private LayoutInflater layoutInflater;
    private Context context;

    public BookCelebAdapter(List<Model> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item, container, false);

        ImageView imageCelebBook;
        TextView nameBook, authorBookCeleb;

        imageCelebBook = view.findViewById(R.id.imageCelebBook);
        nameBook = view.findViewById(R.id.nameBook);
        authorBookCeleb = view.findViewById(R.id.authorBookCeleb);

        imageCelebBook.setImageResource(models.get(position).getImageCelebBook());
        nameBook.setText(models.get(position).getNameBook());
        authorBookCeleb.setText(models.get(position).getAuthorBookCeleb());

        container.addView(view, 0);


        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
