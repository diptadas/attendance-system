package com.example.dipta.attendancesystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.dipta.attendancesystem.R;
import com.example.dipta.attendancesystem.StaticValues;
import com.example.dipta.attendancesystem.util.AsyncTaskQuery;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AsyncTaskQuery.OnAsyncCompletedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StaticValues.prefLoggedIn = getPreferences(MODE_PRIVATE);
        int userID = StaticValues.prefLoggedIn.getInt("userID", -1);

        if (userID != -1) //already logged in, load user data
        {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("userID", userID + ""));

            new AsyncTaskQuery("my_task", StaticValues.userInfoUrl, params, this, this);

        } else {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onAsyncCompleted(String taskID, int success, ArrayList<String> keyList, ArrayList<HashMap<String, String>> dataList) {

        if (taskID.equals("my_task")) {

            if (success == 0) {
                StaticValues.showToast(this, getResources().getString(R.string.server_error));
                finish();
            } else if (dataList.size() == 0) {
                StaticValues.showToast(this, getResources().getString(R.string.no_data));
                finish();
            } else {

                StaticValues.userData = dataList.get(0);

                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
                finish();
            }
        }
    }
}
