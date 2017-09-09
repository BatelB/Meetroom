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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.meetingroomscheduler.Adapter.ListRoomsAdapter;
import com.meetingroomscheduler.Adapter.ListUsersAdapter;
import com.meetingroomscheduler.Class.Room;
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
 * Manage rooms
 * User can View and add all rooms
 **/

public class ManageRooms extends AppCompatActivity {

    TextView back, add_room;

    public static ProgressBar progressbar;

    public static ListView listview;
    public static ListRoomsAdapter adapter;

    boolean sent = false;

    public static ArrayList<Room> rooms_list;

    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_manage_rooms_layout);

        context = ManageRooms.this;

        sent = false;
        paused = false;

        if(Global.requestQueue == null) {
            Global.requestQueue = Volley.newRequestQueue(this);
        }

        back  = (TextView) findViewById(R.id.back);
        add_room = (TextView) findViewById(R.id.add);

        progressbar = (ProgressBar) findViewById(R.id.manage_rooms_progressbar);
        listview = (ListView) findViewById(R.id.listview);

        add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageRooms.this, CreateRoom.class));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getRooms();

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
            getRooms();
        }
    }

    void getRooms(){

        progressbar.setVisibility(View.VISIBLE);
        sent = true;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Global.base_URL + "/php/get_rooms_list.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("MainActivity.class", "Response getRooms : " + response);
                        progressbar.setVisibility(View.GONE);
                        sent = false;

                        rooms_list = new ArrayList<Room>();

                        try {
                            JSONArray json = new JSONArray(response);

                            for(int i=0;i<json.length();i++){
                                JSONObject json_item = new JSONObject(json.get(i).toString());

                                Room new_item = new Room();
                                new_item.id = json_item.getString("id");
                                new_item.number = json_item.getString("number");
                                new_item.floor = json_item.getString("floor");
                                new_item.chairs = json_item.getString("chairs");
                                new_item.equipment = json_item.getString("equipment");

                                rooms_list.add(new_item);

                            }

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
                        Toast.makeText(ManageRooms.this, "Error, please make sure there is internet connection and retry", Toast.LENGTH_LONG).show();
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

        if(rooms_list.size() > 0){
            adapter = new ListRoomsAdapter(context, rooms_list);
            listview.setAdapter(adapter);
        }

    }

}