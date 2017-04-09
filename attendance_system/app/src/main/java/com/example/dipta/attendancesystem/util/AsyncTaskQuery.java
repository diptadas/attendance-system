/*
 * Dipta Das
 * CUET CSE 11
 * dipta670@gmail.com
 */

package com.example.dipta.attendancesystem.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AsyncTaskQuery extends AsyncTask<String, String, String> {

    int success;
    ArrayList<String> keyList = new ArrayList<String>();
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();

    String taskID;
    String url;
    List<NameValuePair> params;
    OnAsyncCompletedListener listener;

    ProgressDialog pDialog;

    public AsyncTaskQuery(String taskID, String url, List<NameValuePair> params, Object object, Context context) {

        if (object instanceof OnAsyncCompletedListener) {
            listener = (OnAsyncCompletedListener) object;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAsyncCompletedListener");
        }

        this.taskID = taskID;
        this.url = url;
        this.params = params;

        pDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_DARK);
        pDialog.setMessage("Loading data...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        execute();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected String doInBackground(String... args) {

        try {
            JSONObject json = new JSONParser().makeHttpRequest(url, "GET", params);

            success = json.getInt("success");

            if (success == 1) {

                JSONArray key = json.getJSONArray("key");
                JSONArray data = json.getJSONArray("data");

                for (int i = 0; i < key.length(); i++) keyList.add(key.get(i).toString());

                for (int i = 0; i < data.length(); i++) {

                    JSONObject obj = data.getJSONObject(i);

                    HashMap<String, String> map = new HashMap<String, String>();

                    for (int j = 0; j < keyList.size(); j++) {
                        String columnName = keyList.get(j);
                        map.put(columnName, obj.getString(columnName));
                    }

                    dataList.add(map);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            success = 0;
        }

        return null;
    }

    protected void onPostExecute(String file_url) {
        pDialog.dismiss();
        listener.onAsyncCompleted(taskID, success, keyList, dataList);
    }

    public interface OnAsyncCompletedListener {
        void onAsyncCompleted(String taskID, int success, ArrayList<String> keyList, ArrayList<HashMap<String, String>> dataList);
    }
}
