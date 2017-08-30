package com.meetingroomscheduler.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.meetingroomscheduler.Activity.EditSchedule;
import com.meetingroomscheduler.Activity.ManageUsers;
import com.meetingroomscheduler.Activity.ScheduleRoomActivity;
import com.meetingroomscheduler.Class.User;
import com.meetingroomscheduler.Global;
import com.meetingroomscheduler.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class EditScheduleDialogListUsersAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<User> arg_0;

    private boolean sent = false;

    public EditScheduleDialogListUsersAdapter(Context context, ArrayList<User> arg_0) {

        super(context, R.layout.dialog_list_users_item, new String[arg_0.size()]);

        this.context = (Activity) context;
        this.arg_0 = arg_0;

        sent = false;
    }

    @Override
    public View getView(final int pos, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.dialog_list_users_item, null, true);

        TextView id = (TextView) rowView.findViewById(R.id.dialog_user_item_id);
        TextView fullname = (TextView) rowView.findViewById(R.id.dialog_user_item_fullname);
        final CheckBox checkbox = (CheckBox) rowView.findViewById(R.id.dialog_user_item_check);

        if(arg_0.get(pos).id.equals(Global.current_user.id)) {
            checkbox.setVisibility(View.GONE);
        }

        for(int i = 0; i < EditSchedule.invited_users.size(); i++){
            if(EditSchedule.invited_users.get(i).id.equals(arg_0.get(pos).id)) {
                checkbox.setChecked(true);
                i=EditSchedule.invited_users.size();
            }
        }

        id.setText("#" + arg_0.get(pos).id);
        fullname.setText(arg_0.get(pos).fullname);

        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity.class", "Checkbox : at pos :  " + pos + "   status : "  + checkbox.isChecked());
                if(checkbox.isChecked()){
                    if(EditSchedule.invitations + 1 <= EditSchedule.max_invitations) {
                        User new_item = new User();
                        new_item.id = arg_0.get(pos).id;
                        new_item.fullname = arg_0.get(pos).fullname;
                        EditSchedule.invited_users.add(new_item);
                        EditSchedule.invitations++;
                    }else{
                        checkbox.setChecked(false);
                        Toast.makeText(context, "You can invite maximum " + Integer.toString(EditSchedule.max_invitations) + " users in this room", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    for(int i = 0;i<EditSchedule.invited_users.size();i++){
                        if(EditSchedule.invited_users.get(i).id.equals(arg_0.get(pos).id)) {
                            EditSchedule.invited_users.remove(i);
                            i = EditSchedule.invited_users.size();
                        }
                    }

                    EditSchedule.invitations --;
                }

                EditSchedule.renderInvitations();
                EditSchedule.PickUserDialogClass.header.setText("Invited users : " + Integer.toString(EditSchedule.invitations) + "/" + Integer.toString(EditSchedule.max_invitations));
            }
        });

        return rowView;
    }

}