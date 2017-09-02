package com.meetingroomscheduler.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.meetingroomscheduler.Adapter.ListUsersAdapter;
import com.meetingroomscheduler.Class.User;
import com.meetingroomscheduler.Global;
import com.meetingroomscheduler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *  Create user
 *  User properties: full name, password, email, user type
 */

public class CreateUser extends AppCompatActivity {

    EditText edit_email, edit_password, edit_fullname;
    Spinner spinner_type;

    ProgressBar progressbar;
    TextView back, submit;
    boolean sent = false;

    String fullname = "", password = "", email = "", type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user_layout);

        sent = false;
        if(Global.requestQueue == null) {
            Global.requestQueue = Volley.newRequestQueue(this);
        }

        edit_fullname = (EditText) findViewById(R.id.add_user_edit_fullname);
        edit_password = (EditText) findViewById(R.id.add_user_edit_password);
        edit_email = (EditText) findViewById(R.id.add_user_edit_email);
        spinner_type = (Spinner) findViewById(R.id.add_user_spinner_type);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        back = (TextView) findViewById(R.id.back);
        submit = (TextView) findViewById(R.id.submit);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                fullname = edit_fullname.getText().toString();
                if(fullname.length() == 0){
                    edit_fullname.setError("All fields are mandatory");
                    return;
                }

                type = "read_only";
                if(spinner_type.getSelectedItemId() == 1)
                    type = "read_write";
                else if(spinner_type.getSelectedItemId() == 2)
                    type = "admin";

                createUser();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    void createUser(){

        progressbar.setVisibility(View.VISIBLE);
        sent = true;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Global.base_URL + "/php/create_user.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("MainActivity.class", "Response getUsers : " + response);
                        progressbar.setVisibility(View.GONE);
                        sent = false;

                        if(response.equals("success")){
                            Toast.makeText(CreateUser.this, "New " + type + " user : " + fullname + " created !", Toast.LENGTH_SHORT).show();
                            edit_fullname.setText("");
                            edit_password.setText("");
                            edit_email.setText("");
                            spinner_type.setSelection(0);
                        }else{
                            Toast.makeText(CreateUser.this, "Error, could not create user", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sent = false;
                        progressbar.setVisibility(View.GONE);
                        Toast.makeText(CreateUser.this, "Error, please make sure there is internet connection and retry", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                params.put("email", email);
                params.put("password", password);
                params.put("type", type);
                params.put("fullname", fullname);

                return params;
            }

        };

        Global.requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
