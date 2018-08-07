package ps.borsuquesquad.pl.firebasechat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    TextView registerUser;
    EditText username, password;
    Button loginButton;
    String user, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerUser = findViewById(R.id.register);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = username.getText().toString();
                pass = password.getText().toString();

                if(user.equals("")){
                    username.setError("Thies field is required");
                }
                else if(pass.equals("")){
                    password.setError("This field is required");
                }
                else{
                    login();
                }

            }
        });
    }
    protected void login(){
        String url = "https://fir-chat-4efae.firebaseio.com/users.json";
        final ProgressDialog progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                if(s.equals("null")){
                    Toast.makeText(Login.this, "user not found", Toast.LENGTH_LONG).show();
                }
                else{
                    try {
                        JSONObject obj = new JSONObject(s);

                        if(!obj.has(user)){
                            Toast.makeText(Login.this, "user not found", Toast.LENGTH_LONG).show();
                        }
                        else if(obj.getJSONObject(user).getString("password").equals(pass)){
                            UserDetails.username = user;
                            UserDetails.password = pass;
                            startActivity(new Intent(Login.this, Users.class));
                        }
                        else {
                            Toast.makeText(Login.this, "incorrect password", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                progressDialog.dismiss();
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
                progressDialog.dismiss();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(Login.this);
        rQueue.add(request);
    }
}
