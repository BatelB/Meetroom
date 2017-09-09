package com.meetingroomscheduler;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.meetingroomscheduler.Class.User;

/**
 *
 */

public class Global {

    public static String base_URL = "https://meetingproject.000webhostapp.com";

    public static User current_user = new User();

    public static RequestQueue requestQueue;

    public static int view_room_index = 0;

    public static String edit_schedule_id = "";


}
