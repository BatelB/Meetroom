package com.meetingroomscheduler.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.meetingroomscheduler.Activity.ManageRooms;
import com.meetingroomscheduler.Activity.ManageUsers;
import com.meetingroomscheduler.Class.Room;
import com.meetingroomscheduler.Global;
import com.meetingroomscheduler.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rafael on 8/23/2017.
 */

public class ScheduleRoomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<Room> arg_0;

    private boolean sent = false;

    public ScheduleRoomListAdapter(Context context, ArrayList<Room> arg_0) {

        super(context, R.layout.list_rooms_item, new String[arg_0.size()]);

        this.context = (Activity) context;
        this.arg_0 = arg_0;

        sent = false;
    }

    @Override
    public View getView(final int pos, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_rooms_item, null, true);

        TextView id = (TextView) rowView.findViewById(R.id.room_item_id);
        TextView floor = (TextView) rowView.findViewById(R.id.room_item_floor);
        TextView chairs = (TextView) rowView.findViewById(R.id.room_item_chairs);
        TextView equipment = (TextView) rowView.findViewById(R.id.room_item_equipment);

        ImageView delete = (ImageView) rowView.findViewById(R.id.room_item_delete);
        delete.setVisibility(View.GONE);

        id.setText("#" + arg_0.get(pos).id);
        floor    .setText("Floor     : " + arg_0.get(pos).floor);
        chairs   .setText("Chairs    : " + arg_0.get(pos).chairs);
        equipment.setText("Equipment : " + arg_0.get(pos).equipment);

        return rowView;
    }


}