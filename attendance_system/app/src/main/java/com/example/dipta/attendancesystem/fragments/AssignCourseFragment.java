package com.example.dipta.attendancesystem.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.example.dipta.attendancesystem.R;
import com.example.dipta.attendancesystem.StaticValues;
import com.example.dipta.attendancesystem.util.AsyncTaskQuery;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AssignCourseFragment extends Fragment implements AsyncTaskQuery.OnAsyncCompletedListener {

    Spinner spCourse, spTeacherName, spSession, spSection;
    CheckBox checkBox;
    Button btnAssignCourse;

    ArrayList<HashMap<String, String>> dataListTeacher = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> dataListCourse = new ArrayList<HashMap<String, String>>();

    private OnFragmentInteractionListener mListener;

    public AssignCourseFragment() {
        // Required empty public constructor
    }


    public static AssignCourseFragment newInstance() {
        AssignCourseFragment fragment = new AssignCourseFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_assign_course, container, false);

        spCourse = (Spinner) rootView.findViewById(R.id.spCourse);
        spTeacherName = (Spinner) rootView.findViewById(R.id.spTeacherName);
        spSession = (Spinner) rootView.findViewById(R.id.spSession);
        spSection = (Spinner) rootView.findViewById(R.id.spSection);

        checkBox = (CheckBox) rootView.findViewById(R.id.checkBox);

        btnAssignCourse = (Button) rootView.findViewById(R.id.btnAssignCourse);


        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("userType", "2"));
        new AsyncTaskQuery("my_task", StaticValues.userInfoUrl, params, this, getContext());

        btnAssignCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String courseID = dataListCourse.get(spCourse.getSelectedItemPosition()).get("course_id");
                String teacherUserID = dataListTeacher.get(spTeacherName.getSelectedItemPosition()).get("user_id");
                String session = spSession.getSelectedItem().toString();
                String section = spSection.getSelectedItem().toString();
                String isRunning = checkBox.isChecked() ? "1" : "0";

                final List<NameValuePair> params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("courseID", courseID));
                params.add(new BasicNameValuePair("teacherUserID", teacherUserID));
                params.add(new BasicNameValuePair("session", session));
                params.add(new BasicNameValuePair("section", section));
                params.add(new BasicNameValuePair("isRunning", isRunning));

                new AlertDialog.Builder(AssignCourseFragment.this.getContext())
                        .setMessage("Do you want to assign the course?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                new AsyncTaskQuery("my_task_3", StaticValues.assignCourseUrl, params, AssignCourseFragment.this, AssignCourseFragment.this.getContext());
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onAsyncCompleted(String taskID, int success, ArrayList<String> keyList, ArrayList<HashMap<String, String>> dataList) {

        if (taskID.equals("my_task")) {

            if (success == 0)
                StaticValues.showToast(getActivity(), getResources().getString(R.string.server_error));
            else if (dataList.size() == 0)
                StaticValues.showToast(getActivity(), getResources().getString(R.string.no_data));
            else {

                this.dataListTeacher = dataList;

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                new AsyncTaskQuery("my_task_2", StaticValues.courseListUrl, params, this, getContext());
            }
        } else if (taskID.equals("my_task_2")) {

            if (success == 0)
                StaticValues.showToast(getActivity(), getResources().getString(R.string.server_error));
            else if (dataList.size() == 0)
                StaticValues.showToast(getActivity(), getResources().getString(R.string.no_data));
            else {
                this.dataListCourse = dataList;
                initSpinner(); //populate data in spinner
            }
        } else if (taskID.equals("my_task_3")) {

            if (success == 0)
                StaticValues.showToast(getActivity(), getResources().getString(R.string.server_error));
            else if (dataList.size() == 0)
                StaticValues.showToast(getActivity(), getResources().getString(R.string.no_data));
            else {

                String status = dataList.get(0).get("status");

                if (status.equals("ok"))
                    StaticValues.showToast(this.getActivity(), "Course assigned successfully.");
                else
                    StaticValues.showToast(getActivity(), status);
            }
        }
    }

    void initSpinner() {

        ArrayList<String> courseList = new ArrayList<String>();
        ArrayList<String> teacherNameList = new ArrayList<String>();
        ArrayList<String> sessionList = new ArrayList<String>();

        for (int i = 2010; i <= 2050; i++) sessionList.add(i + "");

        for (HashMap<String, String> map : dataListCourse) {
            courseList.add(map.get("course_id"));
        }

        for (HashMap<String, String> map : dataListTeacher) {
            teacherNameList.add(map.get("full_name"));
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, StaticValues.sectionList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSection.setAdapter(dataAdapter);

        dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, sessionList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSession.setAdapter(dataAdapter);

        dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, courseList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCourse.setAdapter(dataAdapter);

        dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, teacherNameList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTeacherName.setAdapter(dataAdapter);
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
