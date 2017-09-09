package com.meetingroomscheduler.Adapter;

/**
 *
 */

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.meetingroomscheduler.Activity.ScheduleRoomActivity;
import com.meetingroomscheduler.Class.Invitation;
import com.meetingroomscheduler.Class.User;
import com.meetingroomscheduler.Global;
import com.meetingroomscheduler.R;

import java.util.ArrayList;

public class InvitationsAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<Invitation> arg_0;

    private boolean sent = false;

    public InvitationsAdapter(Context context, ArrayList<Invitation> arg_0) {

        super(context, R.layout.invitation_item, new String[arg_0.size()]);

        this.context = (Activity) context;
        this.arg_0 = arg_0;

        sent = false;
    }

    @Override
    public View getView(final int pos, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.invitation_item, null, true);

        //TextView room_id = (TextView) rowView.findViewById(R.id.invitation_room_id);
        TextView room_number = (TextView) rowView.findViewById(R.id.invitation_room_number);
        TextView room_floor = (TextView) rowView.findViewById(R.id.invitation_room_floor);
        ListView listview = (ListView) rowView.findViewById(R.id.invitation_listview);

       // room_id.setText("#" + arg_0.get(pos).room.id);
        room_number .setText("Number : " + arg_0.get(pos).room.number);
        room_floor  .setText("Floor  : " + arg_0.get(pos).room.floor);

        ListDateTimeForRoomAdapter adapter = new ListDateTimeForRoomAdapter(context, arg_0.get(pos).schedules);
        listview.setAdapter(adapter);

        setListViewHeightBasedOnChildren(listview);

        return rowView;
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ActionBar.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


}