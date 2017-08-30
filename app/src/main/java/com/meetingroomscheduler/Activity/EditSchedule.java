package com.meetingroomscheduler.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.meetingroomscheduler.Adapter.DialogListUsersAdapter;
import com.meetingroomscheduler.Adapter.EditScheduleDialogListUsersAdapter;
import com.meetingroomscheduler.Adapter.EditScheduleInvitedUsersListAdapter;
import com.meetingroomscheduler.Adapter.InvitedUsersListAdapter;
import com.meetingroomscheduler.Adapter.ListDateTimeForRoomAdapter;
import com.meetingroomscheduler.Class.Schedule;
import com.meetingroomscheduler.Class.User;
import com.meetingroomscheduler.Global;
import com.meetingroomscheduler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rafael on 8/28/2017.
 */

public class EditSchedule extends AppCompatActivity {

    TextView tv_invite, tv_submit;

    public static TextView create_edit_day, create_edit_begin_hour, create_edit_end_hour;

    public static Context context;

    public static ListDateTimeForRoomAdapter adapter;

    boolean sent = false;

    public static ArrayList<User> invited_users;

    PickUserDialogClass pick_dialog;

    public static String new_s_day = "", new_s_begin_hour = "", new_s_end_hour = "";
    public static int max_invitations = 0;
    public static int invitations = 0;

    public static ListView listview_invited_users;
    public static ProgressBar progressbar_create_schedule;

    public static TextView invited_title;

    String invitations_json = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_schedule_activity);

        context = EditSchedule.this;

        sent = false;
        paused = false;

        if (Global.requestQueue == null) {
            Global.requestQueue = Volley.newRequestQueue(this);
        }

        invited_users = new ArrayList<User>();

        TextView title = (TextView) findViewById(R.id.edit_title);
        title.setText("Edit schedule #" + Global.edit_schedule_id);

        tv_invite = (TextView) findViewById(R.id.footer_invite);
        tv_submit = (TextView) findViewById(R.id.footer_submit);

        create_edit_day = (TextView) findViewById(R.id.create_edit_day);
        create_edit_begin_hour = (TextView) findViewById(R.id.create_edit_begin_hour);
        create_edit_end_hour = (TextView) findViewById(R.id.create_edit_end_hour);

        listview_invited_users = (ListView) findViewById(R.id.invited_users_listview);
        progressbar_create_schedule = (ProgressBar) findViewById(R.id.create_progressbar);

        invited_title = (TextView) findViewById(R.id.invited_title);
        invited_title.setText("Invited users : " + "1/" + ScheduleActivity.rooms_list.get(Global.view_room_index).chairs);

        max_invitations = 0;
        invitations = 1;

        invited_users = new ArrayList<>();
        User new_item = new User();
        new_item.id = Global.current_user.id;
        new_item.fullname = Global.current_user.fullname;
        invited_users.add(new_item);

        renderInvitations();

        if (ScheduleActivity.rooms_list.get(Global.view_room_index).chairs.length() > 0) {
            max_invitations = Integer.parseInt(ScheduleActivity.rooms_list.get(Global.view_room_index).chairs);
        }

        create_edit_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DayPickerFragment();
                newFragment.show(getFragmentManager(), "Day");
            }
        });

        create_edit_begin_hour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new beginTimePickerFragment();
                newFragment.show(getFragmentManager(), "Begin Hour");
            }
        });

        create_edit_end_hour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new endTimePickerFragment();
                newFragment.show(getFragmentManager(), "End Hour");
            }
        });


        tv_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pick_dialog = new  PickUserDialogClass(EditSchedule.this);
                pick_dialog.show();
            }
        });

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sent) return;

                create_edit_day.setError(null);
                create_edit_begin_hour.setError(null);
                create_edit_end_hour.setError(null);

                new_s_day = create_edit_day.getText().toString();
                if (new_s_day.equals("Day")) {
                    create_edit_day.setError("You must pick day");
                    return;
                }

                new_s_begin_hour = create_edit_begin_hour.getText().toString();
                if (new_s_begin_hour.equals("Begin hour")) {
                    create_edit_begin_hour.setError("You must pick day");
                    return;
                }

                new_s_end_hour = create_edit_end_hour.getText().toString();
                if (new_s_end_hour.equals("End Hour")) {
                    create_edit_end_hour.setError("You must pick day");
                    return;
                }

                if (invitations < 2) {
                    Toast.makeText(context, "You didn't invited any user", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {

                    JSONArray json_array = new JSONArray();
                    for (int i = 0; i < invited_users.size(); i++) {
                        JSONObject json_item = new JSONObject();

                        json_item.put("id", invited_users.get(i).id);
                        json_array.put(json_item);

                    }

                    invitations_json = json_array.toString();

                    Log.d("MainActivity.class", "Final invitations json : " + invitations_json);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                editSchedule();

            }
        });

        getSchedule();
    }

    public static class DayPickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            create_edit_day.setText(intToDateElement(year) + "-" + intToDateElement(month + 1) + "-" + intToDateElement(day));
        }
    }


    public static class beginTimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            create_edit_begin_hour.setText(intToDateElement(hourOfDay) + ":" + intToDateElement(minute));
        }
    }

    public static class endTimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
           create_edit_end_hour.setText(intToDateElement(hourOfDay) + ":" + intToDateElement(minute));
        }
    }

    public static String intToDateElement(int a) {
        String val = Integer.toString(a);
        Log.d("MainActivity.class", "Integer value : " + val + "   length : ");
        if (val.length() > 1) {
            return val;
        } else {
            return "0" + val;
        }
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
            getSchedule();
        }
    }


    void editSchedule() {

        progressbar_create_schedule.setVisibility(View.VISIBLE);
        sent = true;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Global.base_URL + "/php/edit_schedule.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("MainActivity.class", "Response createSchedule : " + response);
                        progressbar_create_schedule.setVisibility(View.GONE);
                        sent = false;

                        if (response.equals("success")) {

                            Toast.makeText(context, "Schedule #" + Global.edit_schedule_id + " edited !", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(context, "Error, could not edit schedule", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sent = false;
                        progressbar_create_schedule.setVisibility(View.GONE);
                        Toast.makeText(EditSchedule.this, "Error, please make sure there is internet connection and retry", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("schedule_id", Global.edit_schedule_id);
                params.put("begin_time", new_s_day + " " + new_s_begin_hour + ":00");
                params.put("end_time", new_s_day + " " + new_s_end_hour + ":00");
                params.put("invitations", invitations_json);

                return params;
            }

        };

        Global.requestQueue.add(stringRequest);
    }


    public static EditScheduleInvitedUsersListAdapter invited_adapter;

    public static void renderInvitations() {

        invited_adapter = new EditScheduleInvitedUsersListAdapter(context, invited_users);
        listview_invited_users.setAdapter(invited_adapter);
        setListViewHeightBasedOnChildren(listview_invited_users);

        invited_title.setText("Invited users : " + Integer.toString(invitations) + "/" + ScheduleActivity.rooms_list.get(Global.view_room_index).chairs);
    }

    void getSchedule() {

        progressbar_create_schedule.setVisibility(View.VISIBLE);
        sent = true;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Global.base_URL + "/php/get_schedule_by_id.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("MainActivity.class", "Response getSchedule : " + response);
                        progressbar_create_schedule.setVisibility(View.GONE);
                        sent = false;

                        try {

                            JSONObject json = new JSONObject(response);
                            if(json.getString("begin").length() >= 10)
                                create_edit_day.setText(json.getString("begin").substring(0,10));


                            if(json.getString("begin").length() >= 16)
                                create_edit_begin_hour.setText(json.getString("begin").substring(10, 16));


                            if(json.getString("end").length() >= 16)
                                create_edit_end_hour.setText(json.getString("begin").substring(10, 16));

                            Log.d("MainActivity.class","Invitations : " + json.getString("invitations"));

                            JSONArray json_array = new JSONArray(json.getString("invitations"));

                            for (int i = 0; i < json_array.length(); i++) {

                                JSONObject json_item = new JSONObject(json_array.get(i).toString());

                                Log.d("MainActivity.class","New user in json");

                                User new_invitation = new User();
                                new_invitation.id = json_item.getString("user_id");
                                new_invitation.fullname = json_item.getString("fullname");

                                if(!new_invitation.id.equals(Global.current_user.id)) {
                                    invited_users.add(new_invitation);
                                    invitations ++;
                                }

                            }

                            renderInvitations();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sent = false;
                        progressbar_create_schedule.setVisibility(View.GONE);
                        Toast.makeText(EditSchedule.this, "Error, please make sure there is internet connection and retry", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("id", Global.edit_schedule_id);
                return params;
            }

        };

        Global.requestQueue.add(stringRequest);
    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {

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


    public static class PickUserDialogClass extends Dialog {

        public Activity c;
        public Dialog d;

        public static ListView listview;
        public static ProgressBar progressbar;

        public static TextView header;

        TextView cancel, submit;

        boolean sent = false;

        public static ArrayList<User> users_list;

        EditScheduleDialogListUsersAdapter adapter;

        public PickUserDialogClass(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.invite_dialog);

            header = (TextView) findViewById(R.id.invited_dialog_counter);
            listview = (ListView) findViewById(R.id.invite_dialog_listview);
            progressbar = (ProgressBar) findViewById(R.id.invite_dialog_progressbar);
            submit = (TextView) findViewById(R.id.invite_dialog_add);

            header.setText("Invited users : " + Integer.toString(invitations) + "/" + Integer.toString(max_invitations));

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            getUsers();

        }

        void getUsers() {

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

                                for (int i = 0; i < json.length(); i++) {
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

                            } catch (JSONException e) {
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

                    return params;
                }

            };

            Global.requestQueue.add(stringRequest);
        }

        void renderList() {

            if (users_list.size() > 0) {
                adapter = new EditScheduleDialogListUsersAdapter(context, users_list);
                listview.setAdapter(adapter);
            }

        }

    }


    @Override
    public void onBackPressed() {
        if (pick_dialog != null) {
            if (pick_dialog.isShowing()) {
                pick_dialog.dismiss();
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

}

