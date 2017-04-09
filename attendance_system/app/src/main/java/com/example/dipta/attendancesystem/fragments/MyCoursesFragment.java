package com.example.dipta.attendancesystem.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.dipta.attendancesystem.R;
import com.example.dipta.attendancesystem.StaticValues;
import com.example.dipta.attendancesystem.activities.PercentageActivity;
import com.example.dipta.attendancesystem.adapters.MyCoursesListAdapter;
import com.example.dipta.attendancesystem.util.AsyncTaskQuery;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MyCoursesFragment extends Fragment implements AsyncTaskQuery.OnAsyncCompletedListener {

    ListView listView;
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();

    private OnFragmentInteractionListener mListener;

    public MyCoursesFragment() {
        // Required empty public constructor
    }


    public static MyCoursesFragment newInstance() {
        MyCoursesFragment fragment = new MyCoursesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_courses, container, false);
        listView = (ListView) rootView.findViewById(R.id.listView);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("userType", StaticValues.userData.get("user_type")));

        if (StaticValues.userData.get("user_type").equals("2"))
            params.add(new BasicNameValuePair("userID", StaticValues.userData.get("user_id")));
        else
            params.add(new BasicNameValuePair("studentID", StaticValues.userData.get("student_id")));

        new AsyncTaskQuery("my_task", StaticValues.myCoursesUrl, params, this, getContext());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(MyCoursesFragment.this.getContext(), PercentageActivity.class);
                intent.putExtra("assignmentID", dataList.get(i).get("assignment_id"));
                startActivity(intent);
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
                listView.setAdapter(new MyCoursesListAdapter(getActivity(), dataList));
            }
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
