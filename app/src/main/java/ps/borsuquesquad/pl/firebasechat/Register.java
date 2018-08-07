package ps.borsuquesquad.pl.firebasechat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {
    EditText username, password;
    Button registerButton;
    String user, pass;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        registerButton = findViewById(R.id.registerButton);
        login = findViewById(R.id.login);

        Firebase.setAndroidContext(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = username.getText().toString();
                pass = password.getText().toString();

                if(user.equals("")){
                    username.setError("This field is required");
                }
                else if(pass.equals("")){
                    password.setError("This fileld is required");
                }
                else if(!user.matches("[A-Za-z0-9]+")){
                    username.setError("Only letters and numbers");
                }
                else if(user.length()<4){
                    username.setError("Please use at least 4 characters");
                }
                else if(pass.length()<4){
                    password.setError("Please use at least 4 characters");
                }
                else {
                    registration();
                }
            }
        });
    }

    protected void registration(){
        final ProgressDialog progressDialog = new ProgressDialog(Register.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        String url = "https://fir-chat-4efae.firebaseio.com/users.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String str) {
                Firebase reference = new Firebase("https://fir-chat-4efae.firebaseio.com/users");

                if(str.equals("null")) {
                    reference.child(user).child("password").setValue(pass);
                    Toast.makeText(Register.this, "Registration successful", Toast.LENGTH_LONG).show();
                }
                else {
                    try {
                        JSONObject object = new JSONObject(str);

                        if (!object.has(user)) {
                            reference.child(user).child("password").setValue(pass);
                            Toast.makeText(Register.this, "Registration successful!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Register.this, "Username already exists", Toast.LENGTH_LONG).show();
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
                System.out.println("" + volleyError );
                progressDialog.dismiss();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(Register.this);
        rQueue.add(request);
    }

}
