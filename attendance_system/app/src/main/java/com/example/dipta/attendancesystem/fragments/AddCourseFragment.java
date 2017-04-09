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
import android.widget.Button;
import android.widget.EditText;

import com.example.dipta.attendancesystem.R;
import com.example.dipta.attendancesystem.StaticValues;
import com.example.dipta.attendancesystem.util.AsyncTaskQuery;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AddCourseFragment extends Fragment implements AsyncTaskQuery.OnAsyncCompletedListener {

    EditText txtCourseID, txtCourseTitle;
    Button btnAddCourse;
    private OnFragmentInteractionListener mListener;

    public AddCourseFragment() {
        // Required empty public constructor
    }


    public static AddCourseFragment newInstance() {
        AddCourseFragment fragment = new AddCourseFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_add_course, container, false);

        txtCourseID = (EditText) rootView.findViewById(R.id.courseID);
        txtCourseTitle = (EditText) rootView.findViewById(R.id.courseTitle);
        btnAddCourse = (Button) rootView.findViewById(R.id.btnAddCourse);

        btnAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String courseID = txtCourseID.getText().toString().trim().toUpperCase();
                String courseTitle = txtCourseTitle.getText().toString().trim();

                if (courseID.equals("") || courseTitle.equals("")) {
                    StaticValues.showToast(AddCourseFragment.this.getActivity(), "Please fill up all fields");
                } else {

                    final List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("courseID", courseID));
                    params.add(new BasicNameValuePair("courseTitle", courseTitle));

                    new AlertDialog.Builder(AddCourseFragment.this.getContext())
                            .setMessage("Do you want to add course?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    new AsyncTaskQuery("my_task", StaticValues.addCourseUrl, params, AddCourseFragment.this, AddCourseFragment.this.getContext());
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();

                }
            }
        });

        return rootView;
    }

    public void onButtonPressed(Uri uri) //callback parent activity
    {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

                String status = dataList.get(0).get("status");

                if (status.equals("ok"))
                    StaticValues.showToast(getActivity(), "Course added.");
                else
                    StaticValues.showToast(getActivity(), status);
            }
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
