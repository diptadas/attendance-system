package com.example.dipta.attendancesystem.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dipta.attendancesystem.R;
import com.example.dipta.attendancesystem.StaticValues;
import com.example.dipta.attendancesystem.util.AsyncTaskQuery;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements AsyncTaskQuery.OnAsyncCompletedListener {

    Button btnLogin, btnLinkReg;
    EditText txtUserName, txtPassword;
    CheckBox checkLoggedIn;

    String inputUserName, inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkReg = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        txtUserName = (EditText) findViewById(R.id.userName);
        txtPassword = (EditText) findViewById(R.id.password);
        checkLoggedIn = (CheckBox) LoginActivity.this.findViewById(R.id.checkLoggedIn);

        btnLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                inputUserName = txtUserName.getText().toString().trim();
                inputPassword = txtPassword.getText().toString().trim();

                if (inputUserName.equals("") || inputPassword.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please fill up all fields", Toast.LENGTH_LONG).show();
                } else {
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("userName", inputUserName));
                    params.add(new BasicNameValuePair("password", inputPassword));

                    new AsyncTaskQuery("my_task", StaticValues.userInfoUrl, params, LoginActivity.this, LoginActivity.this);
                }

            }
        });

        btnLinkReg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onAsyncCompleted(String taskID, int success, ArrayList<String> keyList, ArrayList<HashMap<String, String>> dataList) {

        if (taskID.equals("my_task")) {

            if (success == 0)
                StaticValues.showToast(this, getResources().getString(R.string.server_error));
            else if (dataList.size() == 0)
                StaticValues.showToast(this, "Invalid username or password.");
            else {

                StaticValues.userData = dataList.get(0);

                if (checkLoggedIn.isChecked())  //save userID
                {
                    SharedPreferences.Editor editor = StaticValues.prefLoggedIn.edit();
                    editor.putInt("userID", Integer.parseInt(StaticValues.userData.get("user_id")));
                    editor.commit();
                }

                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
                finish();
            }
        }
    }
}
