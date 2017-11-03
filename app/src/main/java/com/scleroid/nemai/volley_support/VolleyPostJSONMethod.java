package com.scleroid.nemai.volley_support;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nanostuffs on 14-12-2015.
 */
public class VolleyPostJSONMethod {

    Context context;
    VolleyCompleteListener mVolleylistener;
    String tag;
    private int serviceCode;
    private Map<String, String> map;
    // ShowLoader showLoader;

    public VolleyPostJSONMethod(Context context, VolleyCompleteListener volleyCompleteListener, HashMap<String, String> map, boolean isDialog, String tag) {
        this.map = map;
        this.tag = tag;


        this.context = context;
        if (isDialog){
            //      showLoader = new ShowLoader(context);
        }
        if (isNetworkAvailable(context)) {
            mVolleylistener = volleyCompleteListener;
            myBackgroundGetClass(context, volleyCompleteListener, map, isDialog, tag);
        } else {
            showToast(context, "No Internet Connection");
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    private void myBackgroundGetClass(final Context context, VolleyCompleteListener volleyCompleteListener, final Map<String, String> map, final boolean isDialog, String tag) {

        String url = map.get("url");
        map.remove("url");
        if (isDialog){
            //    showLoader.showDialog();
        }
        mVolleylistener = volleyCompleteListener;
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(map), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        mVolleylistener.onTaskCompleted(jsonObject);
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mVolleylistener.onTaskFailed(error.toString());
                        if (isDialog){
                            //                showLoader.dismissDialog();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return map;
            }
        };

        int socketTimeout = 10000000;//10 seconds - change to what you want
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //RequestQueue requestQueue = Volley.newRequestQueue(context);
        //requestQueue.add(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest, tag);
    }

    private void showToast(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }


}
