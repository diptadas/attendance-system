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
import android.widget.ListView;

import com.example.dipta.attendancesystem.R;
import com.example.dipta.attendancesystem.StaticValues;
import com.example.dipta.attendancesystem.adapters.AssignedCourseListAdapter;
import com.example.dipta.attendancesystem.util.AsyncTaskQuery;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CourseRegFragment extends Fragment implements AsyncTaskQuery.OnAsyncCompletedListener {

    ListView listView;
    Button btnReg;

    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    AssignedCourseListAdapter listAdapter;

    private OnFragmentInteractionListener mListener;

    public CourseRegFragment() {
        // Required empty public constructor
    }


    public static CourseRegFragment newInstance() {
        CourseRegFragment fragment = new CourseRegFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_course_reg, container, false);
        listView = (ListView) rootView.findViewById(R.id.listView);
        btnReg = (Button) rootView.findViewById(R.id.btnReg);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("isRunning", "1"));
        new AsyncTaskQuery("my_task", StaticValues.assignedCourseListUrl, params, this, getContext());

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(CourseRegFragment.this.getContext())
                        .setMessage("Do you want to register selected courses?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                String idPairs = "";

                                for (int i = 0; i < dataList.size(); i++) //(1, 1104051), (2, 1104051), (3, 1104051)
                                {
                                    if (listAdapter.isChecked(i)) {
                                        HashMap<String, String> map = dataList.get(i);
                                        if (!idPairs.equals("")) idPairs += ", ";
                                        idPairs += "(" + map.get("assignment_id") + ", " + StaticValues.userData.get("student_id") + ")";
                                    }
                                }

                                //StaticValues.showToast(CourseRegFragment.this.getActivity(), idPairs);

                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair("idPairs", idPairs));
                                new AsyncTaskQuery("my_task_2", StaticValues.courseRegUrl, params, CourseRegFragment.this, CourseRegFragment.this.getContext());
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

                this.dataList = dataList;
                listAdapter = new AssignedCourseListAdapter(getActivity(), dataList);
                listView.setAdapter(listAdapter);
            }
        } else if (taskID.equals("my_task_2")) {

            if (success == 0)
                StaticValues.showToast(getActivity(), getResources().getString(R.string.server_error));
            else if (dataList.size() == 0)
                StaticValues.showToast(getActivity(), getResources().getString(R.string.no_data));
            else {

                String status = dataList.get(0).get("status");

                if (status.equals("ok"))
                    StaticValues.showToast(getActivity(), "Course registration completed.");
                else
                    StaticValues.showToast(getActivity(), status);
            }
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
