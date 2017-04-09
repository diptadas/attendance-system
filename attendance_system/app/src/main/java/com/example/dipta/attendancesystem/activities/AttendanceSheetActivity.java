package com.example.dipta.attendancesystem.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.dipta.attendancesystem.R;
import com.example.dipta.attendancesystem.StaticValues;
import com.example.dipta.attendancesystem.adapters.AttendanceStudentListAdapter;
import com.example.dipta.attendancesystem.util.AsyncTaskQuery;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AttendanceSheetActivity extends AppCompatActivity implements AsyncTaskQuery.OnAsyncCompletedListener {

    ListView listView;
    Button submit;

    String assignmentID;

    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    AttendanceStudentListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_sheet);

        listView = (ListView) findViewById(R.id.listView);
        submit = (Button) findViewById(R.id.submit);

        assignmentID = getIntent().getStringExtra("assignmentID");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("assignmentID", assignmentID));
        new AsyncTaskQuery("my_task", StaticValues.attendanceStudentListUrl, params, this, this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(AttendanceSheetActivity.this)
                        .setMessage("Are you sure you want to submit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                String date = "'" + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()) + "'"; //mysql datetime format

                                String values = "";

                                for (int i = 0; i < dataList.size(); i++) //(assignment_id, student_id, date_time, is_present)
                                {
                                    HashMap<String, String> map = dataList.get(i);

                                    if (!values.equals("")) values += ", ";

                                    if (listAdapter.isChecked(i)) //present
                                        values += "(" + assignmentID + ", " + map.get("student_id") + ", " + date + ", 1)";
                                    else //absent
                                        values += "(" + assignmentID + ", " + map.get("student_id") + ", " + date + ", 0)";
                                }

                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair("values", values));
                                new AsyncTaskQuery("my_task_2", StaticValues.submitAttendanceUrl, params, AttendanceSheetActivity.this, AttendanceSheetActivity.this);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

    }

    @Override
    public void onAsyncCompleted(String taskID, int success, ArrayList<String> keyList, ArrayList<HashMap<String, String>> dataList) {

        if (taskID.equals("my_task")) {

            if (success == 0)
                StaticValues.showToast(this, getResources().getString(R.string.server_error));
            else if (dataList.size() == 0)
                StaticValues.showToast(this, getResources().getString(R.string.no_data));
            else {

                this.dataList = dataList;
                listAdapter = new AttendanceStudentListAdapter(this, dataList);
                listView.setAdapter(listAdapter);
            }
        } else if (taskID.equals("my_task_2")) {

            if (success == 0)
                StaticValues.showToast(this, getResources().getString(R.string.server_error));
            else if (dataList.size() == 0)
                StaticValues.showToast(this, getResources().getString(R.string.no_data));
            else {

                String status = dataList.get(0).get("status");

                if (status.equals("ok")) {
                    StaticValues.showToast(this, "Attendance Submitted.");
                    finish();
                } else {
                    StaticValues.showToast(this, status);
                }
            }
        }
    }
}
