package com.example.dipta.attendancesystem.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dipta.attendancesystem.R;
import com.example.dipta.attendancesystem.StaticValues;
import com.example.dipta.attendancesystem.util.AsyncTaskQuery;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements AsyncTaskQuery.OnAsyncCompletedListener {

    Button btnReg, btnLinkLogin;
    EditText txtUserName, txtStudentID, txtName, txtEmail, txtPassword, txtConfirmPass;
    Spinner txtUserType, txtDept, txtSection, txtDesignation;

    int userType;

    String userName, studentID, fullName, dept, section, designation, email, password, confirmPass; //inputs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnReg = (Button) findViewById(R.id.btnRegister);
        btnLinkLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        txtUserType = (Spinner) findViewById(R.id.spUserType);
        txtUserName = (EditText) findViewById(R.id.userName);
        txtStudentID = (EditText) findViewById(R.id.studentID);
        txtName = (EditText) findViewById(R.id.name);
        txtDept = (Spinner) findViewById(R.id.dept);
        txtSection = (Spinner) findViewById(R.id.section);
        txtDesignation = (Spinner) findViewById(R.id.designation);
        txtEmail = (EditText) findViewById(R.id.email);
        txtPassword = (EditText) findViewById(R.id.password);
        txtConfirmPass = (EditText) findViewById(R.id.confirmPassword);


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, StaticValues.userTypeList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txtUserType.setAdapter(dataAdapter);

        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, StaticValues.deptList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txtDept.setAdapter(dataAdapter);

        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, StaticValues.designationList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txtDesignation.setAdapter(dataAdapter);

        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, StaticValues.sectionList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txtSection.setAdapter(dataAdapter);


        switchUser(0); //by default student

        txtUserType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switchUser(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnReg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                userName = txtUserName.getText().toString().trim();
                studentID = txtStudentID.getText().toString().trim();
                fullName = txtName.getText().toString().trim();
                dept = txtDept.getSelectedItem().toString().trim();
                section = txtSection.getSelectedItem().toString().trim();
                designation = txtDesignation.getSelectedItem().toString().trim();
                email = txtEmail.getText().toString().trim();
                password = txtPassword.getText().toString().trim();
                confirmPass = txtConfirmPass.getText().toString().trim();

                if (!password.equals(confirmPass)) {
                    Toast.makeText(getApplicationContext(), "Please confirm password", Toast.LENGTH_LONG).show();
                } else {

                    final List<NameValuePair> params = new ArrayList<NameValuePair>();

                    params.add(new BasicNameValuePair("userType", userType + ""));
                    params.add(new BasicNameValuePair("userName", userName));
                    params.add(new BasicNameValuePair("studentID", studentID));
                    params.add(new BasicNameValuePair("fullName", fullName));
                    params.add(new BasicNameValuePair("dept", dept));
                    params.add(new BasicNameValuePair("section", section));
                    params.add(new BasicNameValuePair("designation", designation));
                    params.add(new BasicNameValuePair("email", email));
                    params.add(new BasicNameValuePair("password", password));

                    new AlertDialog.Builder(RegisterActivity.this)
                            .setMessage("Any kind of wrong information may lead to academic punishment. Do you want to register?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    new AsyncTaskQuery("my_task", StaticValues.registerUrl, params, RegisterActivity.this, RegisterActivity.this);
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }

            }
        });

        btnLinkLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    void switchUser(int i) {
        if (i == 0) //student
        {
            userType = 1;
            txtStudentID.setVisibility(View.VISIBLE);
            findViewById(R.id.sectionLayout).setVisibility(View.VISIBLE);
            findViewById(R.id.designationLayout).setVisibility(View.GONE);
        } else //teacher
        {
            userType = 2;
            findViewById(R.id.designationLayout).setVisibility(View.VISIBLE);
            txtStudentID.setVisibility(View.GONE);
            findViewById(R.id.sectionLayout).setVisibility(View.GONE);
        }
    }

    @Override
    public void onAsyncCompleted(String taskID, int success, ArrayList<String> keyList, ArrayList<HashMap<String, String>> dataList) {

        if (taskID.equals("my_task")) {

            if (success == 0)
                StaticValues.showToast(this, getResources().getString(R.string.server_error));
            else if (dataList.size() == 0)
                StaticValues.showToast(this, getResources().getString(R.string.no_data));
            else {

                String status = dataList.get(0).get("status");

                if (status.equals("ok")) {
                    StaticValues.showToast(this, "Registration Successful.");
                    finish(); //back to login activity
                } else {
                    StaticValues.showToast(this, status);
                }
            }
        }
    }
}
