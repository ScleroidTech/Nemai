package info.androidhive.navigationdrawer.network;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import info.androidhive.navigationdrawer.R;
import info.androidhive.navigationdrawer.ServerConstants;
import info.androidhive.navigationdrawer.activity.TrackingActivity;
import info.androidhive.navigationdrawer.models.PinCode;
import info.androidhive.navigationdrawer.volley_support.MyVolleyPostMethod1;
import info.androidhive.navigationdrawer.volley_support.VolleyCompleteListener;

/**
 * Created by scleroid on 15/9/17.
 */

public class PinAutoCompleteAdapter extends BaseAdapter implements Filterable {
    String TAG="PinAutoCompleteAdapter";

    private static final int MAX_RESULTS = 10;
    private Context mContext;

    private List<PinCode> mResultPinList = new ArrayList<PinCode>();

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
        ((TextView) listView.findViewById(R.id.popup_pin_text_view)).setText(getItem(position).getPincode());
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

                    // Assign the data to the FilterResults
                  filterResults.values = pinCodes;
                   filterResults.count = pinCodes.size();

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
    private List <PinCode> findPins(Context context, String bookTitle) {

        // GoogleBooksProtocol is a wrapper for the Google Books API
        VolleyCompleteListener volleyCompleteListener = new VolleyCompleteListener() {
            @Override
            public void onTaskCompleted(String response, int serviceCode) {
                Log.i(TAG,response);
                switch (serviceCode) {
                    case ServerConstants.ServiceCode.POST_PINCODE:

                        try {
                            JSONArray json = new JSONArray(response);

                            for(int i=0;i<json.length();i++){
                                HashMap<String, String> map = new HashMap<String, String>();
                                JSONObject e = json.getJSONObject(i);
                                String location = e.getString("location");
                                String pincode= e.getString("pincode");
                                String state = e.getString("state");
                                String area = e.getString("area");


                                mResultPinList.add(new PinCode(location,pincode,state,area));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
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

        new MyVolleyPostMethod1(mContext,volleyCompleteListener,map,ServerConstants.ServiceCode.POST_PINCODE,true);
        //new MyVolleyGetMethod(this,volleyCompleteListener,map,ServerConstants.ServiceCode.POST_COURIER,true);

        return mResultPinList;
    }

}
