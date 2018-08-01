package com.scleroid.nemai.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.scleroid.nemai.R;
import com.scleroid.nemai.ServerConstants;
import com.scleroid.nemai.data.localdb.PinCode;
import com.scleroid.nemai.data.localdb.PinDatabaseHelper;
import com.scleroid.nemai.volley_support.VolleyCompleteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by scleroid on 15/9/17.
 */

public class PinAutoCompleteAdapter extends BaseAdapter implements Filterable {
    private static final int MAX_RESULTS = 10;
    String TAG = "PinAutoCompleteAdapter";
    private Context mContext;
    private List<PinCode> mResultPinList;

    public PinAutoCompleteAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {

        if (mResultPinList != null)
            return mResultPinList.size();
        return 0;
    }

    @Override
    public PinCode getItem(int index) {
        return mResultPinList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View listView, ViewGroup parent) {
        if (listView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listView = inflater.inflate(R.layout.list_item_pin, parent, false);
        }
        /*Log.d(TAG, "Location " + getItem(position).getLocation() + "\n" +
                getItem(position).getPincode() + "\n" +
                getItem(position).getArea() + "\n" +
                " State" + getItem(position).getState());*/
        ((TextView) listView.findViewById(R.id.popup_pin_location_text_view)).setText(String.format("%s , ", getItem(position).getLocation()));
        ((TextView) listView.findViewById(R.id.popup_pin_pincode_text_view)).setText(String.format("%s , ", getItem(position).getPincode()));
        ((TextView) listView.findViewById(R.id.popup_pin_area_text_view)).setText(getItem(position).getArea());
        ((TextView) listView.findViewById(R.id.popup_pin_state_text_view)).setText(getItem(position).getState());

        return listView;




    }
    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence data) {
                FilterResults filterResults = new FilterResults();

                if (data != null) {

	                Flowable<PinCode> observable;
	                List<PinCode> pinCodes = findPins(data.toString(), mContext);

                    // performFiltering is already performed on a worker thread
                    // so we don't need to subscribeOn a background thread explicitly

                  /*  if (numberOrNot(data.toString())) {

                        // query = "SELECT * from india where pincode LIKE ?";
                      *//*  pinCodes = PinDatabase.getPinDatabase(mContext)
                                .pinDao()
                                .getAllIndiaViaPin(data.toString());*//*
                  //      pinCodes = observable.toList().blockingGet();

                        Log.d(TAG, true + "number");
                    } else {
                   *//*     pinCodes = PinDatabase.getPinDatabase(mContext)
                                .pinDao()
                                .getAllIndiaViaCity(data.toString());*//*
                       // pinCodes = observable.toList().blockingGet();
                    }
*/

                    if (pinCodes != null) {


                        // Assign the data to the FilterResults
                        filterResults.values = pinCodes;

                        filterResults.count = pinCodes.size();
                    }//notifyDataSetChanged();

                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence data, FilterResults results) {


                if (results != null && results.count > 0) {

                    mResultPinList = (List<PinCode>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }};
    }

	private boolean numberOrNot(String input) {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    /**
     * Returns a search result for the given data from local database
     */
    private List<PinCode> findPins(String input, Context mContext) {
        PinDatabaseHelper dbHelper = new PinDatabaseHelper(mContext);
	    dbHelper.prepareDatabase();
	    return dbHelper.getPincodes(input);
	   /* try {
		    dbHelper.prepareDatabase();
		    return dbHelper.getPincodes(input);
	    } catch (IOException e) {
		    Log.d(TAG, "IOException" + e.getMessage());
		    return null;
	    }
*/
    }


    /**
     * Returns a search result for the given data from network
     */
    public List<PinCode> findPins(Context context, String userInput) {


        VolleyCompleteListener volleyCompleteListener = new VolleyCompleteListener() {
            @Override
            public void onTaskCompleted(JSONObject response, int statusCode) {
                mResultPinList = new ArrayList<PinCode>();
                Log.i(TAG, response.toString());

                        try {
                            JSONArray json = new JSONArray(response);

                            for(int i=0;i<json.length();i++){
                                // HashMap<String, String> map = new HashMap<String, String>();
                                JSONObject e = json.getJSONObject(i);
                                String location = e.getString("location");
                                String pincode= e.getString("pincode");
                                String state = e.getString("state");
                                String area = e.getString("area");
                                Log.i(TAG, "Location : " + location + "Pincode : " + pincode + i);

                                mResultPinList.add(new PinCode(location, pincode, state, area));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "JSONException " + e.getMessage());
                        }

            }

            @Override
            public void onTaskFailed(String response, int statusCode) {
                Log.i(TAG, response);
            }
        };
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(ServerConstants.URL, ServerConstants.serverUrl.POST_PINCODE);
        map.put("origins", userInput);

        // new VolleyPostJSONMethod(mContext, volleyCompleteListener, map, loader, "pincode");

        return mResultPinList;
    }

}
