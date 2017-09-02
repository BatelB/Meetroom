package com.meetingroomscheduler.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.meetingroomscheduler.Class.User;
import com.meetingroomscheduler.Global;
import com.meetingroomscheduler.MainActivity;
import com.meetingroomscheduler.R;

/**
 *
 */

public class ReadWriteUserPage extends AppCompatActivity {

    TextView button_schedule, button_view, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_write_user_page);

        TextView welcome = (TextView) findViewById(R.id.welcome);
        welcome.setText("Welcome " + Global.current_user.fullname  + "!");

        button_schedule = (TextView) findViewById(R.id.user_schedule);
        button_view = (TextView) findViewById(R.id.user_view);
        logout = (TextView) findViewById(R.id.user_logout);

        button_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReadWriteUserPage.this, ReadMyInvitations.class));
            }
        });

        button_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReadWriteUserPage.this, ScheduleActivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.current_user = new User();
                SharedPreferences prefs = ReadWriteUserPage.this.getSharedPreferences("m_r_schedule", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("password","");
                editor.commit();
                startActivity(new Intent(ReadWriteUserPage.this, MainActivity.class));
                finish();
            }
        });

    }

}