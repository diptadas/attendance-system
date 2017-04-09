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
import com.example.dipta.attendancesystem.activities.AttendanceSheetActivity;
import com.example.dipta.attendancesystem.adapters.AttendanceCourseListAdapter;
import com.example.dipta.attendancesystem.util.AsyncTaskQuery;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TakeAttendanceFragment extends Fragment implements AsyncTaskQuery.OnAsyncCompletedListener {

    ListView listView;

    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();

    private OnFragmentInteractionListener mListener;

    public TakeAttendanceFragment() {
        // Required empty public constructor
    }


    public static TakeAttendanceFragment newInstance() {
        TakeAttendanceFragment fragment = new TakeAttendanceFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_take_attendance, container, false);
        listView = (ListView) rootView.findViewById(R.id.listView);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("userID", StaticValues.userData.get("user_id"))); //teacher_user_id
        new AsyncTaskQuery("my_task", StaticValues.attendanceCourseListUrl, params, this, getContext());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //StaticValues.showToast(TakeAttendanceFragment.this.getActivity(), dataList.get(i).get("assignment_id"));
                Intent intent = new Intent(TakeAttendanceFragment.this.getContext(), AttendanceSheetActivity.class);
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
                listView.setAdapter(new AttendanceCourseListAdapter(getActivity(), dataList));
            }
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
