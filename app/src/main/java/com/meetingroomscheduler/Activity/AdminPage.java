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

import org.w3c.dom.Text;

/**
 * Admin page - Admin can:
 * 1. manage users: add\delete user, view all existing users
 * 2. manage rooms: add\delete room, view all existing rooms
 * 3. schedule room: add meeting to available room
 * 4. View invitations: view all invitations for room
 * 5. logout from user
 */

public class AdminPage extends AppCompatActivity {

    TextView admin_manage_users, admin_manage_rooms, admin_schedule, admin_view, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_page_layout);

        TextView welcome = (TextView) findViewById(R.id.welcome);
        welcome.setText("Welcome " + Global.current_user.fullname + " !");

        admin_manage_users = (TextView) findViewById(R.id.admin_manage_users);
        admin_manage_rooms = (TextView) findViewById(R.id.admin_manage_rooms);
        admin_schedule = (TextView) findViewById(R.id.admin_schedule);
        admin_view = (TextView) findViewById(R.id.admin_view);

        logout = (TextView) findViewById(R.id.admin_logout);

        admin_manage_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminPage.this, ManageUsers.class));
            }
        });

        admin_manage_rooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminPage.this, ManageRooms.class));
            }
        });

        admin_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminPage.this, ScheduleActivity.class));
            }
        });

        admin_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminPage.this, ReadMyInvitations.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.current_user = new User();
                SharedPreferences prefs = AdminPage.this.getSharedPreferences("m_r_schedule", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("password","");
                editor.commit();
                startActivity(new Intent(AdminPage.this, MainActivity.class));
                finish();
            }
        });

    }

}
