package com.example.dipta.attendancesystem.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.dipta.attendancesystem.R;
import com.example.dipta.attendancesystem.StaticValues;
import com.example.dipta.attendancesystem.adapters.AttendanceStudentListAdapter;
import com.example.dipta.attendancesystem.adapters.PercentageListAdapter;
import com.example.dipta.attendancesystem.util.AsyncTaskQuery;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PercentageActivity extends AppCompatActivity implements AsyncTaskQuery.OnAsyncCompletedListener {

    ListView listView;
    String assignmentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pecentage);

        listView = (ListView) findViewById(R.id.listView);

        assignmentID = getIntent().getStringExtra("assignmentID");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("assignmentID", assignmentID));
        new AsyncTaskQuery("my_task", StaticValues.percentageUrl, params, this, this);
    }

    @Override
    public void onAsyncCompleted(String taskID, int success, ArrayList<String> keyList, ArrayList<HashMap<String, String>> dataList) {

        if (taskID.equals("my_task")) {

            if (success == 0)
                StaticValues.showToast(this, getResources().getString(R.string.server_error));
            else if (dataList.size() == 0)
                StaticValues.showToast(this, getResources().getString(R.string.no_data));
            else {
                listView.setAdapter(new PercentageListAdapter(this, dataList));
            }
        }
    }
}
