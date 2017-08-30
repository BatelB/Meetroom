package com.meetingroomscheduler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.meetingroomscheduler.Activity.AdminPage;
import com.meetingroomscheduler.Activity.ReadOnlyUserPage;
import com.meetingroomscheduler.Activity.ReadWriteUserPage;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText edit_email, edit_password;
    ProgressBar progressbar;
    TextView submit;

    boolean sent = false;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    String email = "", password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("MainActivity.class", "onCreate()");

        if(Global.requestQueue == null) {
            Global.requestQueue = Volley.newRequestQueue(this);
        }

        sent = false;

        edit_email = (EditText) findViewById(R.id.main_edit_email);
        edit_password = (EditText) findViewById(R.id.main_edit_password);
        progressbar = (ProgressBar) findViewById(R.id.main_progressbar);
        submit = (TextView) findViewById(R.id.main_submit);

        prefs = this.getSharedPreferences("m_r_schedule", Context.MODE_PRIVATE);
        editor = prefs.edit();

        email = prefs.getString("email", "");
        password = prefs.getString("password", "");

        edit_email.setText(email);
        edit_password.setText(password);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edit_email.setError(null);
                edit_password.setError(null);

                if(sent) return;

                email = edit_email.getText().toString();
                if(email.length() == 0){
                    edit_email.setError("All fields are mandatory");
                    return;
                }

                password = edit_password.getText().toString();
                if(password.length() == 0){
                    edit_password.setError("All fields are mandatory");
                    return;
                }

                authenticateUser();
            }
        });

    }

    private void authenticateUser(){

        Log.d("MainActivity.class", "authenticateUser()");

        progressbar.setVisibility(View.VISIBLE);
        sent = true;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Global.base_URL + "/php/login_user.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("MainActivity.class", "Response authenticate : " + response);
                        progressbar.setVisibility(View.GONE);
                        sent = false;

                        try {
                            JSONObject json = new JSONObject(response);

                            if(json.getString("status").equals("true")){
                                editor.putString("email", email);
                                editor.putString("password", password);
                                editor.commit();
                                Toast.makeText(MainActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                                Global.current_user.id = json.getString("id");
                                Global.current_user.email = email;
                                Global.current_user.type = json.getString("type");
                                Global.current_user.fullname = json.getString("fullname");
                                if(Global.current_user.type.equals("admin")) {
                                    startActivity(new Intent(MainActivity.this, AdminPage.class));
                                    finish();
                                }else if(Global.current_user.type.equals("read_write")){
                                    startActivity(new Intent(MainActivity.this, ReadWriteUserPage.class));
                                    finish();
                                }else if(Global.current_user.type.equals("read_only")){
                                    startActivity(new Intent(MainActivity.this, ReadOnlyUserPage.class));
                                    finish();
                                }
                            }else{
                                Toast.makeText(MainActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sent = false;
                        progressbar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Error, please make sure there is internet connection and retry", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password",password);
                return params;
            }

        };

        Global.requestQueue.add(stringRequest);
    }

}
