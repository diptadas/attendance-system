package com.example.dipta.attendancesystem.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dipta.attendancesystem.R;
import com.example.dipta.attendancesystem.StaticValues;

import java.util.ArrayList;
import java.util.HashMap;

public class PercentageListAdapter extends BaseAdapter {

    ArrayList<HashMap<String, String>> data;
    private Activity activity;
    private LayoutInflater inflater;

    public PercentageListAdapter(Activity activity, ArrayList<HashMap<String, String>> data) {
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
            convertView = inflater.inflate(R.layout.row_percentage_list, null);

        TextView txtStudentID = (TextView) convertView.findViewById(R.id.studentID);
        TextView txtClassCount = (TextView) convertView.findViewById(R.id.classCount);

        HashMap<String, String> map = data.get(position);

        txtStudentID.setText(map.get("student_id") + " (" + map.get("full_name") + ")");

        int total = Integer.parseInt(map.get("total_count"));
        int attended = Integer.parseInt(map.get("attend_count"));
        int percent = (int) ((attended * 100.0d) / total);

        txtClassCount.setText("Total: " + total + " | Attended: " + attended + " (" + percent + "%)"); //student

        return convertView;
    }
}