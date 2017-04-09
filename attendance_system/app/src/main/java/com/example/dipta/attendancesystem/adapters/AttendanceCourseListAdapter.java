package com.example.dipta.attendancesystem.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dipta.attendancesystem.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AttendanceCourseListAdapter extends BaseAdapter {

    ArrayList<HashMap<String, String>> data;
    private Activity activity;
    private LayoutInflater inflater;

    public AttendanceCourseListAdapter(Activity activity, ArrayList<HashMap<String, String>> data) {
        this.activity = activity;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int location) {
        return data.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.row_attendance_courses_list, null);

        TextView txtCourseID = (TextView) convertView.findViewById(R.id.courseID);
        TextView txtCourseTitle = (TextView) convertView.findViewById(R.id.courseTitle);
        TextView txtSession = (TextView) convertView.findViewById(R.id.session);

        HashMap<String, String> map = data.get(position);

        txtCourseID.setText(map.get("course_id"));
        txtCourseTitle.setText(map.get("course_title"));
        txtSession.setText("Session: " + map.get("session") + " | Section: " + map.get("section"));

        return convertView;
    }
}