package com.meetingroomscheduler.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.meetingroomscheduler.Adapter.ListUsersAdapter;
import com.meetingroomscheduler.Class.User;
import com.meetingroomscheduler.Global;
import com.meetingroomscheduler.MainActivity;
import com.meetingroomscheduler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Exchanger;

/**
 *
 */

public class ManageUsers  extends AppCompatActivity {

    TextView back, add_user;

    public static ProgressBar progressbar;

    public static ListView listview;
    public static ListUsersAdapter adapter;

    boolean sent = false;

    public static ArrayList<User> users_list;

    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_manage_users_layout);

        context = ManageUsers.this;

        sent = false;
        paused = false;

        if(Global.requestQueue == null) {
            Global.requestQueue = Volley.newRequestQueue(this);
        }

        back  = (TextView) findViewById(R.id.back);
        add_user = (TextView) findViewById(R.id.add);

        progressbar = (ProgressBar) findViewById(R.id.manage_users_progressbar);
        listview = (ListView) findViewById(R.id.listview);

        add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageUsers.this, CreateUser.class));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getUsers();

    }

    boolean paused = false;

    @Override public void onPause(){
        super.onPause();
        paused = true;
    }

    @Override public void onResume(){
        super.onResume();
        if(paused){
            paused = false;
            getUsers();
        }
    }

    void getUsers(){

        progressbar.setVisibility(View.VISIBLE);
        sent = true;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Global.base_URL + "/php/get_users_list.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("MainActivity.class", "Response getUsers : " + response);
                        progressbar.setVisibility(View.GONE);
                        sent = false;

                        users_list = new ArrayList<User>();

                        try {
                            JSONArray json = new JSONArray(response);

                            for(int i=0;i<json.length();i++){
                                JSONObject json_item = new JSONObject(json.get(i).toString());

                                User new_item = new User();
                                new_item.id = json_item.getString("id");
                                new_item.entry_date = json_item.getString("entry_date");
                                new_item.email = json_item.getString("email");
                                new_item.fullname = json_item.getString("fullname");
                                new_item.type = json_item.getString("type");

                                users_list.add(new_item);

                            }

                            Log.d("MainActivity.class", "Response length : " + response);

                           renderList();

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sent = false;
                        progressbar.setVisibility(View.GONE);
                        Toast.makeText(ManageUsers.this, "Error, please make sure there is internet connection and retry", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                return params;
            }

        };

        Global.requestQueue.add(stringRequest);
    }

    public static void renderList(){

        if(users_list.size() > 0){
            adapter = new ListUsersAdapter(context, users_list);
            listview.setAdapter(adapter);
        }

    }

}
