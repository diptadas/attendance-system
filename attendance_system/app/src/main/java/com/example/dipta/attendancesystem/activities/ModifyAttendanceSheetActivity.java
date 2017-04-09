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
import com.example.dipta.attendancesystem.adapters.ModifyAttendanceStudentListAdapter;
import com.example.dipta.attendancesystem.util.AsyncTaskQuery;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModifyAttendanceSheetActivity extends AppCompatActivity implements AsyncTaskQuery.OnAsyncCompletedListener {

    ListView listView;
    Button submit;

    String assignmentID;
    String date;

    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    ModifyAttendanceStudentListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_sheet);

        listView = (ListView) findViewById(R.id.listView);
        submit = (Button) findViewById(R.id.submit);

        assignmentID = getIntent().getStringExtra("assignmentID");
        date = getIntent().getStringExtra("date");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("assignmentID", assignmentID));
        params.add(new BasicNameValuePair("date", date));
        new AsyncTaskQuery("my_task", StaticValues.modifyAttendanceStudentListUrl, params, this, this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(ModifyAttendanceSheetActivity.this)
                        .setMessage("Are you sure you want to submit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                String presentIDs = "";
                                String absentIDs = "";

                                for (int i = 0; i < dataList.size(); i++) {
                                    String studentID = dataList.get(i).get("student_id");
                                    if (listAdapter.isChecked(i)) {
                                        if (!presentIDs.equals("")) presentIDs += ", ";
                                        presentIDs += studentID;
                                    } else {
                                        if (!absentIDs.equals("")) absentIDs += ", ";
                                        absentIDs += studentID;
                                    }
                                }

                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair("date", date));
                                params.add(new BasicNameValuePair("assignmentID", assignmentID));
                                params.add(new BasicNameValuePair("presentIDs", presentIDs));
                                params.add(new BasicNameValuePair("absentIDs", absentIDs));
                                new AsyncTaskQuery("my_task_2", StaticValues.submitModifyAttendanceUrl, params, ModifyAttendanceSheetActivity.this, ModifyAttendanceSheetActivity.this);
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
                listAdapter = new ModifyAttendanceStudentListAdapter(this, dataList);
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
