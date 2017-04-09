package com.example.dipta.attendancesystem.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dipta.attendancesystem.R;
import com.example.dipta.attendancesystem.StaticValues;
import com.example.dipta.attendancesystem.activities.LoginActivity;
import com.example.dipta.attendancesystem.util.AsyncTaskQuery;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ChangePassFragment extends Fragment implements AsyncTaskQuery.OnAsyncCompletedListener {

    EditText txtPassword, txtNewPass, txtConfirmPass;
    Button btnSubmit;
    private OnFragmentInteractionListener mListener;

    public ChangePassFragment() {
        // Required empty public constructor
    }


    public static ChangePassFragment newInstance() {
        ChangePassFragment fragment = new ChangePassFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_change_pass, container, false);

        txtPassword = (EditText) rootView.findViewById(R.id.password);
        txtNewPass = (EditText) rootView.findViewById(R.id.newPassword);
        txtConfirmPass = (EditText) rootView.findViewById(R.id.confirmPassword);
        btnSubmit = (Button) rootView.findViewById(R.id.btnSubmit);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String password = txtPassword.getText().toString().trim();
                String newPassword = txtNewPass.getText().toString().trim();
                String confirmPass = txtConfirmPass.getText().toString().trim();

                if (password.equals("") || newPassword.equals("") || confirmPass.equals("")) {

                    StaticValues.showToast(ChangePassFragment.this.getActivity(), "Please fill up all fields");
                } else if (!newPassword.equals(confirmPass)) {

                    Toast.makeText(ChangePassFragment.this.getContext(), "Please confirm new password", Toast.LENGTH_LONG).show();
                } else if (!password.equals(StaticValues.userData.get("password"))) {

                    Toast.makeText(ChangePassFragment.this.getContext(), "Wrong current password.", Toast.LENGTH_LONG).show();
                } else {

                    final List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("userID", StaticValues.userData.get("user_id")));
                    params.add(new BasicNameValuePair("password", newPassword));

                    new AlertDialog.Builder(ChangePassFragment.this.getContext())
                            .setMessage("Do you want to change password?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    new AsyncTaskQuery("my_task", StaticValues.changePassUrl, params, ChangePassFragment.this, ChangePassFragment.this.getContext());
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

                if (status.equals("ok")) {
                    StaticValues.showToast(getActivity(), "Password changed. Please login again.");

                    StaticValues.userData = null;

                    SharedPreferences.Editor editor = StaticValues.prefLoggedIn.edit();
                    editor.putInt("userID", -1);
                    editor.commit();

                    Intent i = new Intent(this.getContext(), LoginActivity.class);
                    startActivity(i);
                    this.getActivity().finish();
                } else
                    StaticValues.showToast(getActivity(), status);
            }
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
