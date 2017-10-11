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
import com.scleroid.nemai.models.PinCode;
import com.scleroid.nemai.volley_support.MyVolleyPostMethod1;
import com.scleroid.nemai.volley_support.VolleyCompleteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

        return mResultPinList.size();
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
        ((TextView) listView.findViewById(R.id.popup_pin_text_view)).setText(getItem(position).getPincode() + "(" + getItem(position).getLocation() + ")");

        return listView;




    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List<PinCode> pinCodes = findPins(mContext, constraint.toString());


                    if (pinCodes != null) {


                        // Assign the data to the FilterResults
                        filterResults.values = pinCodes;

                        filterResults.count = pinCodes.size();
                    }//notifyDataSetChanged();

                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {


                if (results != null && results.count > 0) {

                    mResultPinList = (List<PinCode>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }};
        return filter;
    }

    /**
     * Returns a search result for the given book title.
     */
    public List<PinCode> findPins(Context context, String userInput) {


        VolleyCompleteListener volleyCompleteListener = new VolleyCompleteListener() {
            @Override
            public void onTaskCompleted(String response, int serviceCode) {
                mResultPinList = new ArrayList<PinCode>();
                Log.i(TAG,response);
                switch (serviceCode) {
                    case ServerConstants.ServiceCode.POST_PINCODE:

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

                        break;
                }
            }

            @Override
            public void onTaskFailed(String response, int serviceCode) {
                Log.i(TAG,response);
            }
        };
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(ServerConstants.URL, ServerConstants.serverUrl.POST_PINCODE);
        map.put("origins", userInput);

        new MyVolleyPostMethod1(mContext,volleyCompleteListener,map,ServerConstants.ServiceCode.POST_PINCODE,true);

        return mResultPinList;
    }

}
