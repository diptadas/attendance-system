package com.example.dipta.attendancesystem.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.dipta.attendancesystem.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AddAdminListAdapter extends BaseAdapter {

    ArrayList<HashMap<String, String>> data;
    boolean checked[];
    private Activity activity;
    private LayoutInflater inflater;

    public AddAdminListAdapter(Activity activity, ArrayList<HashMap<String, String>> data) {
        this.activity = activity;
        this.data = data;
        checked = new boolean[data.size() + 1];
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
            convertView = inflater.inflate(R.layout.row_add_admin_list, null);

        TextView txtName = (TextView) convertView.findViewById(R.id.name);
        TextView txtEmail = (TextView) convertView.findViewById(R.id.email);
        TextView txtDept = (TextView) convertView.findViewById(R.id.dept);
        final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checked[position] = checkBox.isChecked();
            }
        });

        HashMap<String, String> map = data.get(position);

        txtName.setText(map.get("full_name"));
        txtEmail.setText(map.get("email"));
        txtDept.setText(map.get("designation") + ", Dept. of " + map.get("dept_name"));

        if (map.get("is_admin").equals("1")) checkBox.setChecked(true);
        else checkBox.setChecked(false);

        return convertView;
    }

    public boolean isChecked(int position) {
        if (position >= data.size()) return false;
        return checked[position];
    }

}