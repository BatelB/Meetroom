package com.meetingroomscheduler.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.Image;
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
import com.meetingroomscheduler.Activity.CreateUser;
import com.meetingroomscheduler.Activity.ManageUsers;
import com.meetingroomscheduler.Class.User;
import com.meetingroomscheduler.Global;
import com.meetingroomscheduler.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListUsersAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<User> arg_0;

    private boolean sent = false;

    public ListUsersAdapter(Context context, ArrayList<User> arg_0) {

        super(context, R.layout.list_users_item, new String[arg_0.size()]);

        this.context = (Activity) context;
        this.arg_0 = arg_0;

        sent = false;
    }

    @Override
    public View getView(final int pos, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_users_item, null, true);

        TextView id = (TextView) rowView.findViewById(R.id.user_item_id);
        TextView fullname = (TextView) rowView.findViewById(R.id.user_item_fullname);
        TextView email = (TextView) rowView.findViewById(R.id.user_item_email);
        TextView entry_date = (TextView) rowView.findViewById(R.id.user_item_entry_date);
        TextView type = (TextView) rowView.findViewById(R.id.user_item_type);
        ImageView delete = (ImageView) rowView.findViewById(R.id.user_item_delete);

        id.setText("#" + arg_0.get(pos).id);
        fullname.setText(arg_0.get(pos).fullname);
        email.setText("Email : " + arg_0.get(pos).email);
        entry_date.setText("Entry date : " + arg_0.get(pos).entry_date);
        type.setText("User type : " + arg_0.get(pos).type);

        if(arg_0.get(pos).id.equals(Global.current_user.id)) {
            fullname.setBackgroundColor(Color.parseColor("#ff6060"));
            delete.setVisibility(View.GONE);
        }else{
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteUser(pos);
                }
            });
        }

        return rowView;
    }

    private void deleteUser(final int index){

        ManageUsers.progressbar.setVisibility(View.VISIBLE);
        sent = true;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Global.base_URL + "/php/delete_user.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("MainActivity.class", "Response getUsers : " + response);
                        ManageUsers.progressbar.setVisibility(View.GONE);
                        sent = false;

                        if(response.equals("success")){
                            Toast.makeText(context, "User : " + arg_0.get(index).fullname + " deleted!", Toast.LENGTH_SHORT).show();
                            ManageUsers.users_list.remove(index);
                            ManageUsers.renderList();
                        }else{
                            Toast.makeText(context, "Error, could not delete user", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sent = false;
                        ManageUsers.progressbar.setVisibility(View.GONE);
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