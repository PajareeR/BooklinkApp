package th.ac.su.booklink.booklink;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.BottomNavigationView;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import th.ac.su.booklink.booklink.Details.ImageDetail;
import th.ac.su.booklink.booklink.Details.UserDetail;

public class BookselfActivity extends AppCompatActivity {

    ArrayList<ImageDetail> imageAL = new ArrayList<>();
    TextView textFav, textRead, textWant, textBought, textReading, textUserName;
    RelativeLayout imageBox;
    String nowStatus;
    ImageView imageProfile;
    Bitmap bitmapProfile;
    Activity mcontextt = BookselfActivity.this;
    boolean checkImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//      getSupportActionBar().hide();//barTop
        setContentView(R.layout.activity_bookself);
        imageProfile = (ImageView) findViewById(R.id.imageProfile);

        imageBox = (RelativeLayout) findViewById(R.id.imageBox);

        textFav = (TextView) findViewById(R.id.textFav);
        textRead = (TextView) findViewById(R.id.textRead);
        textWant = (TextView) findViewById(R.id.textWant);
        textBought = (TextView) findViewById(R.id.textBought);
        textReading = (TextView) findViewById(R.id.textReading);

        textUserName = (TextView) findViewById(R.id.textUserName);


        textFav.performClick();

        textUserName.setText(UserDetail.username);

        DatabaseReference reference1 = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://booklink-94984.firebaseio.com/Users/" + UserDetail.username + "");

        reference1.child("profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                String img = snapshot.child("pic").getValue().toString();

                img = img.substring(1, img.length());
                StorageReference storageReference = FirebaseStorage.getInstance("gs://booklink-94984.appspot.com").getReference()
                        .child(img);

                Glide.with(BookselfActivity.this)
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .into(imageProfile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.outsystem, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.logout:
                startActivity(new Intent(BookselfActivity.this, LoginActivity.class));
                Toast.makeText(BookselfActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void selectImgOrTakeImg() {
        final int REQUEST_CAMERA = 0;
        final int SELECT_FILE = 1;

        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mcontextt);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && null != data) {
            switch (requestCode) {
                case 0:
                    Bundle extras = data.getExtras();
                    bitmapProfile = (Bitmap) extras.get("data");
                    break;
                case 1:
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    bitmapProfile = BitmapFactory.decodeFile(picturePath);
                    break;

            }

            checkImg = true;

            imageProfile.setVisibility(View.VISIBLE);


            imageProfile.setImageBitmap(bitmapProfile);


        }


    }

    public String saveImg() {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapProfile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] dataPic = baos.toByteArray();

        String id = UUID.randomUUID().toString();

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://booklink-94984.appspot.com");
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("images/users/" + UserDetail.username + "/user/" +
                "profile_" + id + ".jpg");
        UploadTask uploadTask = imagesRef.putBytes(dataPic);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });

        return imagesRef.getPath();

    }


    public void OnclickMybookself(View view) {
        TextView btnSerect = (TextView) view;
        imageBox.removeAllViews();

        imageAL.clear();

        switch (btnSerect.getId()) {

            case R.id.textFav:
                nowStatus = "fav";
                setTextColor(textFav);
                loadBook("fav");
                break;
            case R.id.textRead:
                nowStatus = "read";
                setTextColor(textRead);
                loadBook("read");
                break;
            case R.id.textWant:
                nowStatus = "want";
                setTextColor(textWant);
                loadBook("want");
                break;
            case R.id.textBought:
                nowStatus = "bought";
                setTextColor(textBought);
                loadBook("bought");
                break;
            case R.id.textReading:
                nowStatus = "reading";
                setTextColor(textReading);
                loadBook("reading");
                break;
        }


    }

    private void setTextColor(TextView textWantColor) {
        textFav.setTextColor(Color.BLACK);
        textRead.setTextColor(Color.BLACK);
        textWant.setTextColor(Color.BLACK);
        textBought.setTextColor(Color.BLACK);
        textReading.setTextColor(Color.BLACK);


        textWantColor.setTextColor(getResources().getColor(R.color.colorPrimary));

    }

    public void getImage() {


        String url = "https://booklink-94984.firebaseio.com/Books.json"; //หัวใหญ่
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);

                    Iterator i = obj.keys();
                    String key = "";

                    int countImage;
                    countImage = 0;
                    RelativeLayout imageBox = (RelativeLayout) findViewById(R.id.imageBox);//o

                    while (i.hasNext()) {
                        key = i.next().toString();
                        for (int j = 0; j < imageAL.size(); j++) {
                            if (imageAL.get(j).getImagePath().contains(key) && imageAL.get(j).getImageStatus().equals(nowStatus)) {
                                countImage += 1; //4


                                LinearLayout.LayoutParams layparam = new LinearLayout.LayoutParams(130, 220);//o

                                switch (countImage % 3) {
                                    case 1:
                                        layparam.setMargins(20, 210 * (countImage / 3), 0, 0);//o
                                        break;
                                    case 2:
                                        layparam.setMargins(170, 210 * (countImage / 3), 0, 0);//o
                                        break;
                                    default:
                                        layparam.setMargins(320, 210 * ((countImage / 3) - 1), 0, 0);//o
                                        break;
                                }


                                ImageView image = new ImageView(BookselfActivity.this);//o
                                image.setLayoutParams(layparam);//o

                                Picasso.get().load(obj.getJSONObject(key).getString("imgbook")).into(image);

                                final int index = j;
                                image.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        UserDetail.bookserect = imageAL.get(index).getImagePath();
                                        startActivity(new Intent(BookselfActivity.this, BookDetailActivity.class));
                                    }
                                });

                                imageBox.addView(image);//o


                            }
                        }
                    }

//                    while (i.hasNext()) {
//                        key = i.next().toString();
//                        for (int j = 0 ; j < imageAL.size() ;  j++){
//                            if (imageAL.get(j).getImagePath().contains(key) && imageAL.get(j).getImageStatus().equals(nowStatus)){
//                                countImage+=1; //4
//
//
//                                LinearLayout.LayoutParams layparam = new LinearLayout.LayoutParams(220 , 320);//o
//
//                                switch (countImage % 3){
//                                    case  1:
//                                        layparam.setMargins(10,350* (countImage/3),0,0);//o
//                                        break;
//                                    case 2:
//                                        layparam.setMargins(230,350* (countImage/3),0,0);//o
//                                        break;
//                                    default :
//                                        layparam.setMargins(450,350* ((countImage/3)-1),0,0);//o
//                                        break;
//                                }
//
//
//                                ImageView image = new ImageView(BookselfActivity.this);//o
//                                image.setLayoutParams(layparam);//o
//
//                                Picasso.get().load(obj.getJSONObject(key).getString("imgbook")).into(image);
//
//                                final int index = j;
//                                image.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        UserDetail.bookserect = imageAL.get(index).getImagePath();
//                                        startActivity(new Intent(BookselfActivity.this,BookDetailActivity.class));
//                                    }
//                                });
//
//                                imageBox.addView(image);//o
//
//
//                            }
//                        }
//                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("" + error);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(BookselfActivity.this);
        requestQueue.add(request);

    }

    public void loadBook(final String type) {
        String url = "https://booklink-94984.firebaseio.com/Users/" + UserDetail.username + "/bookselfs.json"; //หัวใหญ่
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);

                    Iterator i = obj.keys();
                    String key = "";

                    while (i.hasNext()) {
                        key = i.next().toString();
                        if (obj.getJSONObject(key).getString(type).equals("true")) {
                            ImageDetail image = new ImageDetail();
                            image.setImagePath(key);
                            image.setImageStatus(type);
                            imageAL.add(image);

                        }
                    }


//                    NameBook.setText(obj.getJSONObject(UserDetail.bookserect).getString("bookname"));
//                    AuthorBook.setText("นักเขียน"+obj.getJSONObject(UserDetail.bookserect).getString("authorname"));
//                    Picasso.get().load(obj.getJSONObject(UserDetail.bookserect).getString("imgbook")).into(ImageBook);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("" + error);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(BookselfActivity.this);
        requestQueue.add(request);

        getImage();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_Home:
                        startActivity(new Intent(BookselfActivity.this, MainActivity.class));

                        return true;
                    case R.id.item_Award:
                        startActivity(new Intent(BookselfActivity.this, AwardActivity.class));

                        return true;
                    case R.id.item_Celebrity:
                        startActivity(new Intent(BookselfActivity.this, CelebActivity.class));

                        return true;
                    case R.id.item_Bookself:
                        // startActivity(new Intent(BookselfActivity.this, BookselfActivity.class));

                        return true;
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.item_Bookself);


    }


}
