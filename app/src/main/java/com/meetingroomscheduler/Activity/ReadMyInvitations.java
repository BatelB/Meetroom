package com.meetingroomscheduler.Activity;

import android.content.Context;
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
import com.meetingroomscheduler.Adapter.InvitationsAdapter;
import com.meetingroomscheduler.Adapter.InvitedUsersListAdapter;
import com.meetingroomscheduler.Adapter.ScheduleRoomListAdapter;
import com.meetingroomscheduler.Class.Invitation;
import com.meetingroomscheduler.Class.Room;
import com.meetingroomscheduler.Class.Schedule;
import com.meetingroomscheduler.Global;
import com.meetingroomscheduler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rafael on 8/24/2017.
 */

public class ReadMyInvitations extends AppCompatActivity {

    public static Context context;

    ProgressBar progressbar;
    ListView listview;

    boolean sent = false;


    ArrayList<Invitation> invitations_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_my_invitations);

        context = this;

        sent = false;
        paused = false;

        if (Global.requestQueue == null) {
            Global.requestQueue = Volley.newRequestQueue(this);
        }

        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        listview = (ListView) findViewById(R.id.listview);

        getMySchedule();
    }


    boolean paused = false;

    @Override
    public void onPause() {
        super.onPause();
        paused = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (paused) {
            paused = false;
            getMySchedule();
        }
    }

    void getMySchedule() {

        progressbar.setVisibility(View.VISIBLE);
        sent = true;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Global.base_URL + "/php/get_my_schedule.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("MainActivity.class", "Response getRooms : " + response);
                        progressbar.setVisibility(View.GONE);
                        sent = false;

                        invitations_list = new ArrayList<>();

                        try {
                            JSONArray json = new JSONArray(response);

                            for(int i=0;i<json.length();i++){
                                JSONObject json_item = new JSONObject(json.get(i).toString());

                                boolean index_room = true;

                                for(int j=0;j<invitations_list.size();j++){
                                    if(invitations_list.get(j).room.id.equals(json_item.getString("room_id"))){
                                        index_room = false;
                                        j = invitations_list.size();
                                    }
                                }

                                if(index_room){
                                    Invitation new_item = new Invitation();
                                    new_item.room.id = json_item.getString("room_id");
                                    new_item.room.number = json_item.getString("room_number");
                                    new_item.room.floor = json_item.getString("room_floor");
                                    invitations_list.add(new_item);
                                }

                                for(int j=0;j<invitations_list.size();j++){
                                    if(invitations_list.get(j).room.id.equals(json_item.getString("room_id"))){
                                        Schedule new_schedule = new Schedule();
                                        new_schedule.id = json_item.get("schedule_id").toString();
                                        new_schedule.begin = json_item.get("begin_time").toString();
                                        new_schedule.end = json_item.get("end_time").toString();
                                        invitations_list.get(j).schedules.add(new_schedule);
                                    }
                                }
                            }

                            renderPage();

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
                        Toast.makeText(context, "Error, please make sure there is internet connection and retry", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", Global.current_user.id);

                return params;
            }

        };

        Global.requestQueue.add(stringRequest);
    }

    void renderPage(){

        InvitationsAdapter adapter = new InvitationsAdapter(context, invitations_list);
        listview.setAdapter(adapter);

    }

    @Override
    public void onBackPressed(){
        finish();
    }

}
