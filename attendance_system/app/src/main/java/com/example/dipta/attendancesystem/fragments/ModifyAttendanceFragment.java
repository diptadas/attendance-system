package com.example.dipta.attendancesystem.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.dipta.attendancesystem.R;
import com.example.dipta.attendancesystem.StaticValues;
import com.example.dipta.attendancesystem.activities.ModifyAttendanceSheetActivity;
import com.example.dipta.attendancesystem.adapters.AttendanceCourseListAdapter;
import com.example.dipta.attendancesystem.util.AsyncTaskQuery;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class ModifyAttendanceFragment extends Fragment implements AsyncTaskQuery.OnAsyncCompletedListener {

    ListView listView;

    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> dataListDate = new ArrayList<HashMap<String, String>>();

    String assignmentID;
    String date;

    private OnFragmentInteractionListener mListener;

    public ModifyAttendanceFragment() {
        // Required empty public constructor
    }


    public static ModifyAttendanceFragment newInstance() {
        ModifyAttendanceFragment fragment = new ModifyAttendanceFragment();
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

                assignmentID = dataList.get(i).get("assignment_id");

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("assignmentID", assignmentID));
                new AsyncTaskQuery("my_task_2", StaticValues.modifyAttendanceDateListUrl, params, ModifyAttendanceFragment.this, ModifyAttendanceFragment.this.getContext());

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
        } else if (taskID.equals("my_task_2")) {

            if (success == 0)
                StaticValues.showToast(getActivity(), getResources().getString(R.string.server_error));
            else if (dataList.size() == 0)
                StaticValues.showToast(getActivity(), getResources().getString(R.string.no_data));
            else {

                this.dataListDate = dataList;

                ArrayList<String> dates = new ArrayList<String>();

                for (HashMap<String, String> map : dataList) {
                    try {
                        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(map.get("date_time"));
                        dates.add(new SimpleDateFormat("dd-MMM-yyyy hh:mm a").format(date));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(ModifyAttendanceFragment.this.getContext());
                builderSingle.setTitle("Select date");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ModifyAttendanceFragment.this.getContext(), android.R.layout.simple_list_item_1, dates);

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        date = dataListDate.get(which).get("date_time");

                        Intent intent = new Intent(ModifyAttendanceFragment.this.getContext(), ModifyAttendanceSheetActivity.class);
                        intent.putExtra("assignmentID", assignmentID);
                        intent.putExtra("date", date);
                        startActivity(intent);

                    }
                });
                builderSingle.show();
            }
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
