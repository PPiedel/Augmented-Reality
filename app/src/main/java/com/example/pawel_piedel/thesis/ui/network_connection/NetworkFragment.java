package com.example.pawel_piedel.thesis.ui.network_connection;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.example.pawel_piedel.thesis.R;

/**
 * Created by Pawel_Piedel on 26.07.2017.
 * Headless fragment based on https://medium.com/@ali.muzaffar/use-headless-fragment-for-android-m-run-time-permissions-and-to-check-network-connectivity-b48615f6272d
 */

public class NetworkFragment extends Fragment {
    public static final String LOG_TAG = NetworkFragment.class.getSimpleName();
    private Activity activity;
    private BroadcastReceiver broadcastReceiver;
    private   AlertDialog alertDialog = null;

    public static NetworkFragment newInstance() {

        Bundle args = new Bundle();

        NetworkFragment fragment = new NetworkFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public NetworkFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isNetworkAvailable(context)){
                    showAlertDialog();
                }
                else if (alertDialog!=null && alertDialog.isShowing()){
                    alertDialog.dismiss();
                }
            }
        };
        activity.registerReceiver(broadcastReceiver, filter);

        if (isNetworkAvailable(activity)){
            showAlertDialog();
        }
        else if (alertDialog!=null && alertDialog.isShowing()){
            alertDialog.dismiss();
        }


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (broadcastReceiver != null) {
            activity.unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
        }
    }

    private void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage("Sprawdź swoje połączenie z Interetem !")
                .setTitle("Sieć niedostępna");

        builder.setPositiveButton(R.string.ok, (dialog, id) -> dialog.dismiss());
        //builder.setNegativeButton(R.string.cancel, (dialog, id) -> dialog.cancel());

        alertDialog= builder.create();
        alertDialog.show();
    }

    private static boolean  isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return networkInfo == null || !networkInfo.isConnected();
    }

}
