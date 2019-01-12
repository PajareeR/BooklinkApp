package th.ac.su.booklink.booklink;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    EditText usernameId, emailId, passwordId;
    String usernameStr,emailStr,passwordStr;
    Button btnRegis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_register);

        usernameId = (EditText) findViewById(R.id.usernameId);
        emailId = (EditText) findViewById(R.id.emailId);
        passwordId = (EditText) findViewById(R.id.passwordId);

        btnRegis = (Button) findViewById(R.id.btnRegis);

        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCondition();
            }
        });

    }

    public final static boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void checkCondition() {
        boolean status = true;
        usernameStr = usernameId.getText().toString();
        emailStr = emailId.getText().toString();
        passwordStr = passwordId.getText().toString();

        if(usernameStr.equals("")){
            usernameId.setError("can't be blank");
            status = false;
        } else if(!usernameStr.matches("[A-Za-z0-9]+")){
            usernameId.setError("only alphabet or number allowed");
            status = false;
        }

        if(emailStr.equals("")){
            emailId.setError("can't be blank");
            status = false;
        }else if (!isValidEmail(emailStr)){
            emailId.setError("only email pattern");
            status = false;
        }

        if(passwordStr.equals("")){
            passwordId.setError("can't be blank");
            status = false;
        } else if(passwordStr.length()<5){
            passwordId.setError("at least 5 characters long");
            status = false;
        }

        if (status){
            final ProgressDialog pd = new ProgressDialog(RegisterActivity.this);
            pd.setMessage("Loading...");
            pd.show();

            String url = "https://booklink-94984.firebaseio.com/Users.json";

            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                @Override
                public void onResponse(String s) {

                    DatabaseReference reference = FirebaseDatabase.getInstance()
                            .getReferenceFromUrl("https://booklink-94984.firebaseio.com/Users");

                    if(s.equals("null")) {
                        reference.child(usernameStr).child("profile").child("password").setValue(passwordStr);
                        reference.child(usernameStr).child("profile").child("email").setValue(emailStr);

                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    }
                    else {
                        try {
                            JSONObject obj = new JSONObject(s);

                            if (!obj.has(usernameStr)) {
                                reference.child(usernameStr).child("profile").child("password").setValue(passwordStr);
                                reference.child(usernameStr).child("profile").child("email").setValue(emailStr);

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

            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    System.out.println("" + volleyError );
                    pd.dismiss();
                }
            });

            RequestQueue rQueue = Volley.newRequestQueue(RegisterActivity.this);
            rQueue.add(request);
        }


    }
}
