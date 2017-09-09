package info.androidhive.navigationdrawer.volley_support;

/**
 * Created by Nanostuffs on 25-05-2015.
 */
public interface VolleyCompleteListener {

    void onTaskCompleted(String response, int serviceCode);

    void onTaskFailed(String response, int serviceCode);

}
