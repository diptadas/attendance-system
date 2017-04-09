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
import com.example.dipta.attendancesystem.adapters.AddAdminListAdapter;
import com.example.dipta.attendancesystem.util.AsyncTaskQuery;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AddAdminFragment extends Fragment implements AsyncTaskQuery.OnAsyncCompletedListener {

    ListView listView;

    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    AddAdminListAdapter listAdapter;

    private OnFragmentInteractionListener mListener;

    public AddAdminFragment() {
        // Required empty public constructor
    }


    public static AddAdminFragment newInstance() {
        AddAdminFragment fragment = new AddAdminFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_admin, container, false);

        listView = (ListView) rootView.findViewById(R.id.listView);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("userType", "2"));
        new AsyncTaskQuery("my_task", StaticValues.userInfoUrl, params, this, getContext());

        Button btnAddAdmin = (Button) rootView.findViewById(R.id.btnAddAdmin);

        btnAddAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(AddAdminFragment.this.getContext())
                        .setMessage("Do you want to modify admin list?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                String adminIDs = "";
                                String nonAdminIDs = "";

                                for (int i = 0; i < dataList.size(); i++) {
                                    String userID = dataList.get(i).get("user_id");
                                    if (listAdapter.isChecked(i)) {
                                        if (!adminIDs.equals("")) adminIDs += ", ";
                                        adminIDs += userID;
                                    } else {
                                        if (!nonAdminIDs.equals("")) nonAdminIDs += ", ";
                                        nonAdminIDs += userID;
                                    }
                                }

                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair("adminIDs", adminIDs));
                                params.add(new BasicNameValuePair("nonAdminIDs", nonAdminIDs));
                                new AsyncTaskQuery("my_task_2", StaticValues.addAdminUrl, params, AddAdminFragment.this, AddAdminFragment.this.getContext());
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
                listAdapter = new AddAdminListAdapter(getActivity(), dataList);
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
                    StaticValues.showToast(getActivity(), "Admin list modified.");
                else
                    StaticValues.showToast(getActivity(), status);
            }
        }
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
