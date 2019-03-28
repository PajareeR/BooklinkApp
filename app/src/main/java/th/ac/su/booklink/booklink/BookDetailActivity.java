package th.ac.su.booklink.booklink;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import th.ac.su.booklink.booklink.Details.CommentDetail;
import th.ac.su.booklink.booklink.Details.UserDetail;

import static java.util.Comparator.comparing;

public class BookDetailActivity extends AppCompatActivity {
    TextView NameBook, AuthorBook, TitleBook, PublisherBook, CategoryBook, ISBNBook, AdditionBook, reviewCeleb, contentReview;
    ImageView ImageBook, imageShow;
    ImageButton btnFav, btnRead, btnWant, btnBought, btnReading;
    EditText edtComment;
    Button btnSendComment, btnImageUp;


    LinearLayout commentBox, layRec;

    DatabaseReference commentReference;
    Bitmap bitmapComment;
    boolean checkImg = false;
    Activity mcontext = BookDetailActivity.this;
    int widthDevice, hieghDevice;
    String celeb = "";
    String celebReview = "";



    ArrayList<CommentDetail> arrComment = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//barTop
        setContentView(R.layout.activity_book_detail);

        widthDevice = getWindowManager().getDefaultDisplay().getWidth();
        hieghDevice = getWindowManager().getDefaultDisplay().getHeight();

        NameBook = (TextView) findViewById(R.id.NameBook);
        AuthorBook = (TextView) findViewById(R.id.AuthorBook);
        TitleBook = (TextView) findViewById(R.id.TitleBook);
        PublisherBook = (TextView) findViewById(R.id.PublisherBook);
        CategoryBook = (TextView) findViewById(R.id.CatagoryBook);
        ISBNBook = (TextView) findViewById(R.id.ISBNBook); //xx
        AdditionBook = (TextView) findViewById(R.id.AdditionBook);

        reviewCeleb = (TextView) findViewById(R.id.reviewCeleb);
        contentReview = (TextView) findViewById(R.id.contentReview);


        btnSendComment = (Button) findViewById(R.id.btnSendComment);

        btnImageUp = (Button) findViewById(R.id.btnImageUp);


        ImageBook = (ImageView) findViewById(R.id.ImageBook);
        imageShow = (ImageView) findViewById(R.id.imageShow);
        imageShow.setVisibility(View.GONE);

        btnFav = (ImageButton) findViewById(R.id.btnFav);
        btnRead = (ImageButton) findViewById(R.id.btnRead);
        btnWant = (ImageButton) findViewById(R.id.btnWant);
        btnBought = (ImageButton) findViewById(R.id.btnBought);
        btnReading = (ImageButton) findViewById(R.id.btnReading);

        edtComment = (EditText) findViewById(R.id.edtComment);


        commentBox = (LinearLayout) findViewById(R.id.commentBox);
        layRec = (LinearLayout) findViewById(R.id.layRec);



        if (getIntent().hasExtra("Celeb")) {
            celeb = getIntent().getExtras().getString("Celeb");
            celebReview = getIntent().getExtras().getString("ReviewCeleb");
            reviewCeleb.setText("รีวิวจากคุณ" + celeb);
            contentReview.setText(celebReview);
        } else {
            reviewCeleb.setVisibility(View.GONE);
            contentReview.setVisibility(View.GONE);
        }

        if (ContextCompat.checkSelfPermission(mcontext, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mcontext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mcontext, android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mcontext,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.CAMERA}, 1);
        }

        String url = "https://booklink-94984.firebaseio.com/.json"; //หัวใหญ่
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response).getJSONObject("Books");
                    final JSONObject objUsers = new JSONObject(response).getJSONObject("Users");

                    NameBook.setText(obj.getJSONObject(UserDetail.bookserect).getString("bookname"));
                    AuthorBook.setText("นักเขียน : " + obj.getJSONObject(UserDetail.bookserect).getString("authorname"));
                    TitleBook.setText(obj.getJSONObject(UserDetail.bookserect).getString("titlebook"));

                    PublisherBook.setText("สำนักพิมพ์ : " + obj.getJSONObject(UserDetail.bookserect).getString("publisherbook"));
                    CategoryBook.setText("หมวดหมู่ : " + obj.getJSONObject(UserDetail.bookserect).getString("catagorybook"));
                    ISBNBook.setText("รหัส ISBN : " + UserDetail.bookserect); //XX
                    AdditionBook.setText(obj.getJSONObject(UserDetail.bookserect).getString("additionbook"));

                    Picasso.get().load(obj.getJSONObject(UserDetail.bookserect).getString("imgbook")).into(ImageBook);

                    getRecomemnd(obj.getJSONObject(UserDetail.bookserect).getString("authorname"),
                            obj.getJSONObject(UserDetail.bookserect).getString("catagorybook"),
                            obj.getJSONObject(UserDetail.bookserect).getString("bookname"));

                    getComment(objUsers);

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
        RequestQueue requestQueue = Volley.newRequestQueue(BookDetailActivity.this);
        requestQueue.add(request);

        setStatusData("load");

        btnSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertComment();

            }
        });


        btnImageUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImgOrTakeImg();
            }
        });
    }

    private void getRecomemnd(String author, String category , String title) {
        DatabaseReference recomemndReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://booklink-94984.firebaseio.com/Books");

        recomemndReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Map<String, String> map = (Map) ds.getValue();
                    boolean sameAuthor = (author.equals(map.get("authorname").toString()));
                    boolean sameCategory = (category.equals(map.get("catagorybook").toString()));
                    boolean sameBook = (title.equals(map.get("bookname").toString()));



                    if (!sameBook && (sameAuthor || sameCategory) ) {

                        LinearLayout boder = new LinearLayout(mcontext);
                        LinearLayout.LayoutParams boderparams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        boder.setLayoutParams(boderparams);
                        boder.setOrientation(LinearLayout.VERTICAL);

                        ImageView imageView = new ImageView(mcontext);
                        LinearLayout.LayoutParams roundparams = new LinearLayout.LayoutParams(
                                (int) (widthDevice * 0.20),
                                (int) (hieghDevice * 0.20)
                        );
                        int roundMargin = (int) (widthDevice * 0.01);
                        roundparams.setMargins( roundMargin, 0, roundMargin, 0);
                        imageView.setLayoutParams(roundparams);
                        imageView.setPadding(5,5,5,5);
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        Picasso.get()
                                .load(map.get("imgbook").toString())
                                .into(imageView);

                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                UserDetail.bookserect = ds.getKey();
                                finish();
                                startActivity(getIntent());
                            }
                        });


                        TextView txtTitle = new TextView(mcontext);
                        LinearLayout.LayoutParams layoutParamstxt = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        txtTitle.setLayoutParams(layoutParamstxt);
                        txtTitle.setTextColor(Color.BLACK);
                        txtTitle.setTextSize(20);
                        Typeface type = ResourcesCompat.getFont(mcontext, R.font.sukhumvitsetbold);
                        txtTitle.setTypeface(type);
                        txtTitle.setText(map.get("bookname").toString());

                        boder.addView(imageView);
                        boder.addView(txtTitle);

                        layRec.addView(boder);

                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });

    }

    public void getComment(final JSONObject objuser) {
        commentReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://booklink-94984.firebaseio.com/Books/" + UserDetail.bookserect + "/comments");

        commentReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                commentBox.removeAllViews();
                arrComment.clear();

                boolean haveLike = false;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Map<String, String> map = (Map) ds.getValue();

                    try {
                        JSONObject obj = objuser.getJSONObject(map.get("user")).getJSONObject("profile");
                        String img = (obj.has("pic") ? obj.getString("pic") : "");
                        String imgComment = map.get("pic").toString();

                        int count = 0;
                        boolean status = false;
                        boolean checkUserlike = (ds.hasChild("userlike") ? true : false);

                        if (checkUserlike) {
                            for (DataSnapshot dslike : ds.child("userlike").getChildren()) {

                                if (dslike.child("status").getValue().toString().equals("like")) {
                                    if (dslike.getKey().equals(UserDetail.username)) {
                                        status = true;
                                    }
                                    count += 1;
                                    haveLike = true;
                                }
                            }
                        }

                        arrComment.add(new CommentDetail(
                                ds.getKey(),
                                map.get("user").toString(),
                                new Date(Long.parseLong(map.get("time").toString())),
                                img,
                                map.get("comment").toString(),
                                imgComment,
                                status,
                                count
                        ));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                Collections.sort(arrComment, new Comparator<CommentDetail>() {
                    public int compare(CommentDetail o1, CommentDetail o2) {
                        return o1.getCommentTime().compareTo(o2.getCommentTime());
                    }
                });

                if (arrComment.size() != 0) {
                    if (haveLike) {
                        CommentDetail maxValue = arrComment.stream().max(comparing(CommentDetail::getCountLike)).get();
                        arrComment.remove(arrComment.indexOf(maxValue));
                        arrComment.add(arrComment.size(), maxValue);
                    }
                    for (int i = arrComment.size() - 1; i >= 0; i--) {
                        createComment(arrComment.get(i));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
    }

    public void createComment(CommentDetail commentDetail) {
        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        CircleImageView circleImageView = new CircleImageView(this);
        LinearLayout.LayoutParams circleParams = new LinearLayout.LayoutParams(
                (int) (hieghDevice * 0.08),
                (int) (hieghDevice * 0.08)
        );
        circleImageView.setLayoutParams(circleParams);
        circleImageView.setBorderWidth(3);
        circleImageView.setBorderColor(getResources().getColor(R.color.colorbg));

        String img = commentDetail.getUserImg();
        if (img.equals("")) {
            circleImageView.setImageResource(R.drawable.smile);
        } else {
            img = img.substring(1, img.length());
            StorageReference storageReference = FirebaseStorage.getInstance("gs://booklink-94984.appspot.com").getReference()
                    .child(img);

            Glide.with(getApplicationContext())
                    .using(new FirebaseImageLoader())
                    .load(storageReference)
                    .into(circleImageView);
        }


        LinearLayout linearLayout1 = new LinearLayout(this);
        linearLayout1.setLayoutParams(layoutParams);
        linearLayout1.setOrientation(LinearLayout.VERTICAL);
        linearLayout1.setPadding(20, 0, 20, 0);

        TextView txtUsername = new TextView(this);
        LinearLayout.LayoutParams layoutParamstxt = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        txtUsername.setLayoutParams(layoutParamstxt);
        txtUsername.setTextColor(Color.BLACK);
        txtUsername.setTextSize(20);
        Typeface type = ResourcesCompat.getFont(this, R.font.sukhumvitsetbold);
        txtUsername.setTypeface(type);
        txtUsername.setText(commentDetail.getCommentUser());

        TextView txtTime = new TextView(this);
        txtTime.setLayoutParams(layoutParamstxt);
        txtTime.setTextSize(16);
        txtTime.setTypeface(type);
        txtTime.setText(commentDetail.getCommentTimeDring());

        TextView txtComment = new TextView(this);
        txtComment.setLayoutParams(layoutParamstxt);
        txtComment.setTextColor(Color.BLACK);
        txtComment.setTextSize(20);
        Typeface type1 = ResourcesCompat.getFont(this, R.font.csprajad);
        txtComment.setTypeface(type1);
        txtComment.setText(commentDetail.getComment());

        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(
                (int) (widthDevice * 0.7),
                (int) (hieghDevice * 0.4),
                Gravity.CENTER
        );
        imageView.setLayoutParams(imgParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);


        String imgComment = commentDetail.getCommentImg();
        if (imgComment.equals("")) {
            imageView.setVisibility(View.GONE);
        } else {
            imgComment = imgComment.substring(1, imgComment.length());
            StorageReference storageReference = FirebaseStorage.getInstance("gs://booklink-94984.appspot.com").getReference()
                    .child(imgComment);

            Glide.with(getApplicationContext())
                    .using(new FirebaseImageLoader())
                    .load(storageReference)
                    .into(imageView);
        }


        LinearLayout linearLayoutLike = new LinearLayout(this);
        linearLayoutLike.setLayoutParams(layoutParams);

        linearLayoutLike.setOrientation(LinearLayout.HORIZONTAL);

        final ImageView imageViewLike = new ImageView(this);
        LinearLayout.LayoutParams imgParamslike = new LinearLayout.LayoutParams(

                (int) (widthDevice * 0.05),
                (int) (hieghDevice * 0.05)


        );
        imageViewLike.setLayoutParams(imgParamslike);
        if (commentDetail.isStatusLike()) {
            imageViewLike.setImageResource(R.drawable.likeb);
        } else {
            imageViewLike.setImageResource(R.drawable.liket);
        }

        TextView txtPeople = new TextView(this);
        txtPeople.setLayoutParams(layoutParamstxt);
        txtPeople.setPadding(0, 6, 0, 0);
        txtPeople.setTextSize(20);
        txtPeople.setTypeface(type);
        txtPeople.setText(" " + commentDetail.getCountLike() + " คน");

        imageViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentDetail.isStatusLike()) {
                    commentReference.child(commentDetail.getCommentKey()).child("userlike").child(UserDetail.username).child("status").setValue("unlike");
                } else {
                    commentReference.child(commentDetail.getCommentKey()).child("userlike").child(UserDetail.username).child("status").setValue("like");
                }
            }
        });

        linearLayoutLike.addView(imageViewLike);
        linearLayoutLike.addView(txtPeople);

        linearLayout1.addView(txtUsername);
        linearLayout1.addView(txtTime);
        linearLayout1.addView(txtComment);
        linearLayout1.addView(imageView);
        linearLayout1.addView(linearLayoutLike);

        linearLayout.addView(circleImageView);
        linearLayout.addView(linearLayout1);

        commentBox.addView(linearLayout);


    }

    private void selectImgOrTakeImg() {
        final int REQUEST_CAMERA = 0;
        final int SELECT_FILE = 1;

        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mcontext);
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
                    bitmapComment = (Bitmap) extras.get("data");
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
                    bitmapComment = BitmapFactory.decodeFile(picturePath);
                    break;

            }

            checkImg = true;

            imageShow.setVisibility(View.VISIBLE);

            bitmapComment = Bitmap.createScaledBitmap(bitmapComment, 130, 130, true);
            imageShow.setImageBitmap(bitmapComment);


        }


    }

    public void saveImg(Map<String, String> map) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapComment.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] dataPic = baos.toByteArray();

        String id = UUID.randomUUID().toString();

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://booklink-94984.appspot.com");
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("images/books/" + UserDetail.bookserect + "/comment/" +
                "comment_" + id + ".jpg");
        UploadTask uploadTask = imagesRef.putBytes(dataPic);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                map.put("pic", "");
                commentReference.push().setValue(map);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                map.put("pic", imagesRef.getPath());
                commentReference.push().setValue(map);
            }
        });
    }

    private void insertComment() {
        String comment = edtComment.getText().toString();
        Calendar calendar = Calendar.getInstance();
        Map<String, String> map = new HashMap<String, String>();
        map.put("user", UserDetail.username);
        if (!comment.equals("")) {
            map.put("comment", comment);
        } else {
            map.put("comment", "");
        }
        map.put("time", String.valueOf(calendar.getTimeInMillis()));
        map.put("like", "0");
        if (checkImg) {
            saveImg(map);
        } else {
            map.put("pic", "");
            commentReference.push().setValue(map);
        }

        edtComment.setText("");
        imageShow.setVisibility(View.GONE);
        checkImg = false;
    }

    public void Onclicktobookself(View view) {
        ImageButton btnSerect = (ImageButton) view;
        switch (btnSerect.getId()) {
            case R.id.btnBack:
                super.onBackPressed();

                break;
            case R.id.btnFav:
                setStatusData("fav");
                Toast toast = Toast.makeText(getApplicationContext(),
                        "บันทึกลงรายการหนังสือเล่มโปรด", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();

                break;
            case R.id.btnRead:

                setStatusData("read");
                Toast toast2 = Toast.makeText(getApplicationContext(),
                        "บันทึกลงรายการหนังสืออ่านแล้ว", Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast2.show();

                break;
            case R.id.btnWant:
                setStatusData("want");
                Toast toast3 = Toast.makeText(getApplicationContext(),
                        "บันทึกลงรายการหนังสืออยากซื้อ", Toast.LENGTH_SHORT);
                toast3.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast3.show();

                break;
            case R.id.btnBought:
                setStatusData("bought");
                Toast toast4 = Toast.makeText(getApplicationContext(),
                        "บันทึกลงรายการหนังสือซื้อแล้ว", Toast.LENGTH_SHORT);
                toast4.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast4.show();

                break;
            case R.id.btnReading:
                setStatusData("reading");
                Toast toast5 = Toast.makeText(getApplicationContext(),
                        "บันทึกลงรายการหนังสือกำลังอ่าน", Toast.LENGTH_SHORT);
                toast5.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast5.show();

                break;
        }
    }

    public void setStatusData(final String type) {

        String url = "https://booklink-94984.firebaseio.com/Users/" + UserDetail.username + "/bookselfs.json"; //หัวใหญ่
        Log.d("sss", url);
        final StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                DatabaseReference statusReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://booklink-94984.firebaseio.com");

                if (response.equals("null")) {
                    statusReference.child("Users").child(UserDetail.username).child("bookselfs").child(UserDetail.bookserect).child("fav")
                            .setValue("false");
                    statusReference.child("Users").child(UserDetail.username).child("bookselfs").child(UserDetail.bookserect).child("read")
                            .setValue("false");
                    statusReference.child("Users").child(UserDetail.username).child("bookselfs").child(UserDetail.bookserect).child("want")
                            .setValue("false");
                    statusReference.child("Users").child(UserDetail.username).child("bookselfs").child(UserDetail.bookserect).child("bought")
                            .setValue("false");
                    statusReference.child("Users").child(UserDetail.username).child("bookselfs").child(UserDetail.bookserect).child("reading")
                            .setValue("false");

                    setStatusData(type);
                } else {
                    try {
                        JSONObject objBookSelfs = new JSONObject(response);
                        if (!objBookSelfs.has(UserDetail.bookserect)) {

                            statusReference.child("Users").child(UserDetail.username).child("bookselfs").child(UserDetail.bookserect).child("fav")
                                    .setValue("false");
                            statusReference.child("Users").child(UserDetail.username).child("bookselfs").child(UserDetail.bookserect).child("read")
                                    .setValue("false");
                            statusReference.child("Users").child(UserDetail.username).child("bookselfs").child(UserDetail.bookserect).child("want")
                                    .setValue("false");
                            statusReference.child("Users").child(UserDetail.username).child("bookselfs").child(UserDetail.bookserect).child("bought")
                                    .setValue("false");
                            statusReference.child("Users").child(UserDetail.username).child("bookselfs").child(UserDetail.bookserect).child("reading")
                                    .setValue("false");

                            setStatusData(type);
                        } else {
                            JSONObject obj = objBookSelfs.getJSONObject(UserDetail.bookserect);
                            switch (obj.getString("fav")) {
                                case "true":
                                    btnFav.setImageResource(R.drawable.bfav);

                                    break;
                                case "false":
                                    btnFav.setImageResource(R.drawable.tfav);
                                    break;
                            }
                            switch (obj.getString("read")) {
                                case "true":
                                    btnRead.setImageResource(R.drawable.bread);

                                    break;
                                case "false":
                                    btnRead.setImageResource(R.drawable.tread);
                                    break;
                            }
                            switch (obj.getString("want")) {
                                case "true":
                                    btnWant.setImageResource(R.drawable.bwant);

                                    break;
                                case "false":
                                    btnWant.setImageResource(R.drawable.twant);
                                    break;
                            }
                            switch (obj.getString("bought")) {
                                case "true":
                                    btnBought.setImageResource(R.drawable.bbought);

                                    break;
                                case "false":
                                    btnBought.setImageResource(R.drawable.tbought);
                                    break;
                            }
                            switch (obj.getString("reading")) {
                                case "true":
                                    btnReading.setImageResource(R.drawable.breading);

                                    break;
                                case "false":
                                    btnReading.setImageResource(R.drawable.treading);
                                    break;
                            }

                            if (type != "load") {
                                statusReference.child("Users").child(UserDetail.username).child("bookselfs").child(UserDetail.bookserect).child(type)
                                        .setValue(((obj.getString(type).equals("true")) ? "false" : "true"));

                                setChangeStatus(type, obj.getString(type));
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("" + error);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(BookDetailActivity.this);
        requestQueue.add(request);
    }

    private void setChangeStatus(String type, String status) {
        switch (type) {
            case "fav":
                switch (status) {
                    case "true":
                        btnFav.setImageResource(R.drawable.tfav);
                        break;
                    case "false":
                        btnFav.setImageResource(R.drawable.bfav);
                        break;
                }
                break;

            case "read":
                switch (status) {
                    case "true":
                        btnRead.setImageResource(R.drawable.tread);
                        break;
                    case "false":
                        btnRead.setImageResource(R.drawable.bread);
                        break;
                }
                break;

            case "want":
                switch (status) {
                    case "true":
                        btnWant.setImageResource(R.drawable.twant);
                        break;
                    case "false":
                        btnWant.setImageResource(R.drawable.bwant);
                        break;
                }
                break;

            case "bought":
                switch (status) {
                    case "true":
                        btnBought.setImageResource(R.drawable.tbought);
                        break;
                    case "false":
                        btnBought.setImageResource(R.drawable.bbought);
                        break;
                }
                break;

            case "reading":
                switch (status) {
                    case "true":
                        btnReading.setImageResource(R.drawable.treading);
                        break;
                    case "false":
                        btnReading.setImageResource(R.drawable.breading);
                        break;
                }
        }
    }
}
