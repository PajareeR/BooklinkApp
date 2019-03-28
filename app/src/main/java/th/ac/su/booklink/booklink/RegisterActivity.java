package th.ac.su.booklink.booklink;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import th.ac.su.booklink.booklink.Details.UserDetail;

public class RegisterActivity extends AppCompatActivity {

    EditText usernameId, emailId, passwordId;
    String usernameStr, emailStr, passwordStr;
    Button btnRegis;
    TextView btnLLog;
    private CircleImageView ProfileImage;
    private static final int PICK_IMAGE = 1;
    Uri selectedImage;
    Bitmap imageSelect;
    DatabaseReference profileReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_register);

        usernameId = (EditText) findViewById(R.id.usernameId);
        emailId = (EditText) findViewById(R.id.emailId);
        passwordId = (EditText) findViewById(R.id.passwordId);

        btnLLog = (TextView) findViewById(R.id.btnLLog);


        btnRegis = (Button) findViewById(R.id.btnRegis);

        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCondition();
            }
        });
        btnLLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });


        if (ContextCompat.checkSelfPermission(RegisterActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(RegisterActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(RegisterActivity.this, android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RegisterActivity.this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.CAMERA}, 1);
        }//รูป pop up take photo//galley


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Register");
        //กดกลับ ตั้งชื่อหน้านั้น


        ProfileImage = (CircleImageView) findViewById(R.id.profile);
        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                imgClick();

            }
        });

    }

    public void imgClick() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 0);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 1);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }//click img

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && null != data) {
            switch (requestCode) {
                case 0:
                    Bundle extras = data.getExtras();
                    imageSelect = (Bitmap) extras.get("data");
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
                    imageSelect = BitmapFactory.decodeFile(picturePath);
                    break;

            }

            imageSelect = Bitmap.createScaledBitmap(imageSelect, 100, 100, true);
            ProfileImage.setImageBitmap(imageSelect);
        }

    }//เลือกรูป


    public final static boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void checkCondition() {
        boolean status = true;
        usernameStr = usernameId.getText().toString();
        emailStr = emailId.getText().toString();
        passwordStr = passwordId.getText().toString();

        if (usernameStr.equals("")) {
            usernameId.setError("can't be blank");
            status = false;
        } else if (!usernameStr.matches("[A-Za-z0-9]+")) {
            usernameId.setError("only alphabet or number allowed");
            status = false;
        }

        if (emailStr.equals("")) {
            emailId.setError("can't be blank");
            status = false;
        } else if (!isValidEmail(emailStr)) {
            emailId.setError("only email pattern");
            status = false;
        }

        if (passwordStr.equals("")) {
            passwordId.setError("can't be blank");
            status = false;
        } else if (passwordStr.length() < 5) {
            passwordId.setError("at least 5 characters long");
            status = false;
        }

        if (status) {
            final ProgressDialog pd = new ProgressDialog(RegisterActivity.this);
            pd.setMessage("Loading...");
            pd.show();

            String url = "https://booklink-94984.firebaseio.com/Users.json";

            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {

                    DatabaseReference reference = FirebaseDatabase.getInstance()
                            .getReferenceFromUrl("https://booklink-94984.firebaseio.com/Users");

                    if (s.equals("null")) {
                        InsertData();
                        reference.child(usernameStr).child("profile").child("password").setValue(passwordStr);
                        reference.child(usernameStr).child("profile").child("email").setValue(emailStr);
                        UserDetail.username = usernameStr;
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    } else {
                        try {
                            JSONObject obj = new JSONObject(s);

                            if (!obj.has(usernameStr)) {
                                InsertData();
                                reference.child(usernameStr).child("profile").child("password").setValue(passwordStr);
                                reference.child(usernameStr).child("profile").child("email").setValue(emailStr);
                                UserDetail.username = usernameStr;
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            } else {
                                usernameId.setError("username already exists");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    pd.dismiss();
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    System.out.println("" + volleyError);
                    pd.dismiss();
                }
            });

            RequestQueue rQueue = Volley.newRequestQueue(RegisterActivity.this);
            rQueue.add(request);
        }

    }

    public void InsertData() {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageSelect.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] dataPic = baos.toByteArray();

        String id = UUID.randomUUID().toString();// ชื่อรูปไม่ซ้ำกัน Random
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://booklink-94984.appspot.com/");
        //FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("images/users/"+usernameStr+"/profile_" + id + ".jpg"); //พาทรูป



        UploadTask uploadTask = imagesRef.putBytes(dataPic);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                Toast.makeText(RegisterActivity.this, "incorrect ", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(RegisterActivity.this, "correct ", Toast.LENGTH_LONG).show(); //บันทึกเข้าแล้ว
            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://booklink-94984.firebaseio.com/Users");

        reference.child(usernameStr).child("profile").child("pic").setValue(imagesRef.getPath());
    }
}