package com.meetingroomscheduler.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.meetingroomscheduler.Activity.EditSchedule;
import com.meetingroomscheduler.Activity.ManageRooms;
import com.meetingroomscheduler.Activity.ManageUsers;
import com.meetingroomscheduler.Activity.ScheduleRoomActivity;
import com.meetingroomscheduler.Class.Room;
import com.meetingroomscheduler.Class.Schedule;
import com.meetingroomscheduler.Global;
import com.meetingroomscheduler.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rafael on 8/23/2017.
 */

public class ListDateTimeForRoomAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<Schedule> arg_0;

    private boolean sent = false;

    public ListDateTimeForRoomAdapter(Context context, ArrayList<Schedule> arg_0) {

        super(context, R.layout.room_schedule_list_item, new String[arg_0.size()]);

        this.context = (Activity) context;
        this.arg_0 = arg_0;

        sent = false;
    }

    @Override
    public View getView(final int pos, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.room_schedule_list_item, null, true);

        TextView id = (TextView) rowView.findViewById(R.id.id);
        TextView day = (TextView) rowView.findViewById(R.id.day);
        TextView begin = (TextView) rowView.findViewById(R.id.begin);
        TextView end = (TextView) rowView.findViewById(R.id.end);

        LinearLayout edit_delete_layout = (LinearLayout) rowView.findViewById(R.id.edit_delete_layout);

        id.setText("#" + arg_0.get(pos).id);

        if(arg_0.get(pos).manager_id.equals(Global.current_user.id)){
            rowView.setBackgroundColor(Color.parseColor("#a7adba"));

            TextView edit = (TextView) rowView.findViewById(R.id.edit);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Global.edit_schedule_id = arg_0.get(pos).id;
                    context.startActivity(new Intent(context, EditSchedule.class));
                }
            });

            TextView delete = (TextView) rowView.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteRoom(pos);
                }
            });

        }else{
            edit_delete_layout.setVisibility(View.GONE);
        }

        if(arg_0.get(pos).begin.length() >= 10)
        day.setText(arg_0.get(pos).begin.substring(0,10));


        if(arg_0.get(pos).begin.length() >= 16)
            begin.setText(arg_0.get(pos).begin.substring(10, 16));


        if(arg_0.get(pos).end.length() >= 16)
            end.setText(arg_0.get(pos).end.substring(10, 16));

        return rowView;
    }


    private void deleteRoom(final int index){

        ScheduleRoomActivity.progressbar.setVisibility(View.VISIBLE);
        sent = true;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Global.base_URL + "/php/delete_schedule.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("MainActivity.class", "Response getUsers : " + response);
                        ScheduleRoomActivity.progressbar.setVisibility(View.GONE);
                        sent = false;

                        if(response.equals("success")){
                            Toast.makeText(context, "Schedule #" + arg_0.get(index).id + " deleted!", Toast.LENGTH_SHORT).show();
                            ScheduleRoomActivity.schedule_list.remove(index);
                            ScheduleRoomActivity.renderList();
                        }else{
                            Toast.makeText(context, "Error, could not delete schedule", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sent = false;
                        ManageRooms.progressbar.setVisibility(View.GONE);
                        Toast.makeText(context, "Error, please make sure there is internet connection and retry", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                params.put("id", arg_0.get(index).id);

                return params;
            }

        };

        Global.requestQueue.add(stringRequest);
    }

}
