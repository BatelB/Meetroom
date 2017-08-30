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
 *
 */

public class CreateRoom extends AppCompatActivity {

    EditText edit_number, edit_floor, edit_chairs, edit_equipment;

    ProgressBar progressbar;

    TextView back, submit;

    boolean sent = false;

    String number = "", floor = "", chairs = "", equipment = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_room_layout);

        sent = false;
        if(Global.requestQueue == null) {
            Global.requestQueue = Volley.newRequestQueue(this);
        }

        edit_number = (EditText) findViewById(R.id.add_room_edit_number);
        edit_floor = (EditText) findViewById(R.id.add_room_edit_floor);
        edit_chairs = (EditText) findViewById(R.id.add_room_edit_chairs);
        edit_equipment = (EditText) findViewById(R.id.add_room_edit_equipment);

        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        back = (TextView) findViewById(R.id.back);
        submit = (TextView) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sent) return;

                number = edit_number.getText().toString();
                if(number.length() == 0){
                    edit_number.setError("All fields are mandatory");
                    return;
                }

                floor = edit_floor.getText().toString();
                if(floor.length() == 0){
                    edit_floor.setError("All fields are mandatory");
                    return;
                }

                chairs = edit_chairs.getText().toString();
                if(chairs.length() == 0){
                    edit_chairs.setError("All fields are mandatory");
                    return;
                }

                equipment = edit_equipment.getText().toString();
                if(edit_equipment.length() == 0){
                    edit_equipment.setError("All fields are mandatory");
                    return;
                }

                createRoom();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    void createRoom(){

        progressbar.setVisibility(View.VISIBLE);
        sent = true;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Global.base_URL + "/php/create_room.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("MainActivity.class", "Response getUsers : " + response);
                        progressbar.setVisibility(View.GONE);
                        sent = false;

                        if(response.equals("success")){
                            Toast.makeText(CreateRoom.this, "New room created !", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(CreateRoom.this, "Error, could not create room", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sent = false;
                        progressbar.setVisibility(View.GONE);
                        Toast.makeText(CreateRoom.this, "Error, please make sure there is internet connection and retry", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                params.put("number", number);
                params.put("floor", floor);
                params.put("chairs", chairs);
                params.put("equipment", equipment);

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
