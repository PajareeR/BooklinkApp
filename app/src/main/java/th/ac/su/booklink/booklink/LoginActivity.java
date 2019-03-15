package th.ac.su.booklink.booklink;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import th.ac.su.booklink.booklink.Details.UserDetail;

public class LoginActivity extends AppCompatActivity {
    private CallbackManager callbackManager;

    EditText edtUser, edtPass;
    String userStr, passStr;
    Button btnLogin;
    LoginButton btnFBLogin;
    TextView btnRegis;


    String userF, nameF, genderF, birthdayF, emailF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_login);

        edtUser = (EditText) findViewById(R.id.edtUser);
        edtPass = (EditText) findViewById(R.id.edtPass);

        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCondition();
            }
        });

        btnFBLogin = (LoginButton) findViewById(R.id.btnFBLogin);

        btnFBLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithFB();
            }
        });
        callbackManager = CallbackManager.Factory.create();
        btnFBLogin.setReadPermissions(Arrays.asList("public_profile", "user_friends", "email", "user_birthday"));
        btnRegis = (TextView) findViewById(R.id.btnRegis);
        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void loginWithFB() {
        btnFBLogin.setReadPermissions(Arrays.asList("public_profile", "user_birthday", "email"));
        btnFBLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("onSuccess");
                GraphRequest mGraphRequest = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {
                                if (response.getError() != null) {
                                    // handle error
                                } else {
                                    userF = me.optString("id");
                                    nameF = me.optString("name");
                                    genderF = me.optString("gender");
                                    birthdayF = me.optString("birthday");
                                    emailF = me.optString("email");

                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,gender, birthday,email");
                mGraphRequest.setParameters(parameters);
                mGraphRequest.executeAsync();


                String url = "https://booklink-94984.firebaseio.com/Users.json";
                StringRequest requestFace = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        DatabaseReference reference = FirebaseDatabase.getInstance()
                                .getReferenceFromUrl("https://booklink-94984.firebaseio.com/Users");
                        if (s.equals("null")) {
                            reference.child(userF).child("profile").child("email").setValue(emailF);
                        } else {
                            try {
                                JSONObject obj = new JSONObject(s);

                                if (!obj.has(userF)) {

                                    reference.child(userF).child("profile").child("email").setValue(emailF);

                                }
                                UserDetail.username = userF;
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println("" + volleyError);

                    }
                });

                RequestQueue rQueue = Volley.newRequestQueue(LoginActivity.this);
                rQueue.add(requestFace);

            }

            @Override
            public void onCancel() {
                System.out.println("onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println("onError");
                Log.v("LoginActivity", exception.getCause().toString());
            }
        });

    }

    private void checkCondition() {

        boolean status = true;
        userStr = edtUser.getText().toString();
        passStr = edtPass.getText().toString();

        if (userStr.equals("")) {
            edtUser.setError("can't be blank");
            status = false;
        }

        if (passStr.equals("")) {
            edtPass.setError("can't be blank");
            status = false;
        }

        if (status) {
            final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
            pd.setMessage("Loading...");
            pd.show();

            String url = "https://booklink-94984.firebaseio.com/Users.json";

            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {


                    try {
                        JSONObject obj = new JSONObject(s);

                        if (obj.has(userStr)) {

                            if (obj.getJSONObject(userStr).getJSONObject("profile").getString("password").equals(passStr)) {
                                UserDetail.username = userStr;
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            } else {
                                edtPass.setError("Password not correct");
                            }


                        } else {
                            edtUser.setError("not found user");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
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

            RequestQueue rQueue = Volley.newRequestQueue(LoginActivity.this);
            rQueue.add(request);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
