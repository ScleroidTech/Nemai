package com.scleroid.nemai.volley_support;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Ganesh on 14-12-2015.
 */
public class VolleyPostJSONMethod {

    private static final String TAG = "VolleyStuff";
    Context context;
    VolleyCompleteListener mVolleylistener;
    String tag;
    ShowLoader showLoader;
    ShowNetworkErrorDialog showNetworkErrorDialogDialog;
    private int serviceCode;
    private Map<String, String> map;

    public VolleyPostJSONMethod(Context context, VolleyCompleteListener volleyCompleteListener, Map<String, String> map, ShowLoader loader, String tag) {
        this.map = map;

        this.tag = tag;
        showLoader = loader;
        showNetworkErrorDialogDialog = new ShowNetworkErrorDialog(context);

        this.context = context;

        networkCheck(context, volleyCompleteListener, map, tag);
    }

    private void networkCheck(Context context, VolleyCompleteListener volleyCompleteListener, Map<String, String> map, String tag) {

        if (showNetworkErrorDialogDialog.showDialog()) {
            mVolleylistener = volleyCompleteListener;
            myBackgroundGetClass(context, volleyCompleteListener, map, tag);

        }
    }


    private void myBackgroundGetClass(final Context context, final VolleyCompleteListener volleyCompleteListener, final Map<String, String> map, String tag) {

        String url = map.get("url");
        final int[] statusCode = {0};
        map.remove("url");
            showLoader.showDialog();

        mVolleylistener = volleyCompleteListener;

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(map), new Response.Listener<JSONObject>() {


            @Override
                    public void onResponse(JSONObject jsonObject) {
                        showLoader.dismissDialog();
                Log.d(TAG, "onResponse " + jsonObject.toString());
                mVolleylistener.onTaskCompleted(jsonObject, statusCode[0]);
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse " + error.toString());

                            showLoader.dismissDialog();
                        if (error instanceof NetworkError) {
                            showNetworkErrorDialogDialog.showDialog();
                            mVolleylistener.onTaskFailed("Your Network seems slow", statusCode[0]);

                        } else if (error instanceof ServerError) {
                            mVolleylistener.onTaskFailed("We're sorry, Our servers seems having some issues", statusCode[0]);
                        } else if (error instanceof AuthFailureError) {
                            mVolleylistener.onTaskFailed("We're sorry, Our servers seems having some issues", statusCode[0]);
                        } else if (error instanceof ParseError) {
                            mVolleylistener.onTaskFailed("We're sorry, Our servers seems having some issues", statusCode[0]);
                        } else if (error instanceof NoConnectionError) {
                            showNetworkErrorDialogDialog.showDialog();
                            mVolleylistener.onTaskFailed("Your Network seems Unavailable", statusCode[0]);

                        } else if (error instanceof TimeoutError) {

                            mVolleylistener.onTaskFailed("Oops. Request Timed Out!", statusCode[0]);
                        }
                        //   mVolleylistener.onTaskFailed(error.toString());
                    }


                }) {

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Log.d(TAG, "parseNetworkResponse" + response.statusCode + response.data.toString() + response.headers.toString() + response.networkTimeMs + response.notModified);
                statusCode[0] = response.statusCode;

                return super.parseNetworkResponse(response);
            }

            @Override
            protected Map<String, String> getParams() {
                Log.d(TAG, "getParams " + map);
                return map;
            }
        };

        int socketTimeout = 1000;//10 seconds - change to what you want
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
