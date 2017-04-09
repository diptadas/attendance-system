package com.example.dipta.attendancesystem.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.dipta.attendancesystem.R;
import com.example.dipta.attendancesystem.StaticValues;
import com.example.dipta.attendancesystem.adapters.TeacherListAdapter;
import com.example.dipta.attendancesystem.util.AsyncTaskQuery;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TeacherListFragment extends Fragment implements AsyncTaskQuery.OnAsyncCompletedListener {

    ListView listView;

    private OnFragmentInteractionListener mListener;

    public TeacherListFragment() {
        // Required empty public constructor
    }


    public static TeacherListFragment newInstance() {
        TeacherListFragment fragment = new TeacherListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_teacher_list, container, false);

        listView = (ListView) rootView.findViewById(R.id.listView);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("userType", "2"));
        new AsyncTaskQuery("my_task", StaticValues.userInfoUrl, params, this, getContext());

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
            else
                listView.setAdapter(new TeacherListAdapter(getActivity(), dataList));
        }
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
