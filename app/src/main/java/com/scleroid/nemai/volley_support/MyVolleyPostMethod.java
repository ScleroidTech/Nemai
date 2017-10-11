package com.scleroid.nemai.volley_support;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.scleroid.nemai.volley_support.MyVolleyPostMethod1.isNetworkAvailable;

/**
 * Created by Nanostuffs on 14-12-2015.
 */
public class MyVolleyPostMethod {

    Context context;
    VolleyCompleteListener mVolleylistener;
    ShowLoader showLoader;
    private int serviceCode;
    private Map<String, String> map;

    public MyVolleyPostMethod(Context context, HashMap<String, String> map, int serviceCode, boolean isDialog) {
        this.map = map;
        this.serviceCode = serviceCode;
        this.context = context;
        if (isDialog){
            showLoader = new ShowLoader(context);
        }
        if (isNetworkAvailable(context)) {
            mVolleylistener = (VolleyCompleteListener) context;
            myBackgroundGetClass(context, serviceCode, map, isDialog);
        } else {
            showToast(context, "No Internet Connection");
        }
    }


    private void myBackgroundGetClass(final Context context, int serviceCode, final Map<String, String> map, final boolean isDialog) {

        final int code = serviceCode;
        String url = map.get("url");
        map.remove("url");
        if (isDialog){
            showLoader.showDialog();
        }
        mVolleylistener = (VolleyCompleteListener) context;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mVolleylistener.onTaskCompleted(response, code);
                        if (isDialog){
                            showLoader.dismissDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mVolleylistener.onTaskFailed(error.toString(), code);
                        if (isDialog){
                            showLoader.dismissDialog();
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
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void showToast(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

}
