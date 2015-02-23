package com.alexwglenn.ccrewardsbuddy;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import java.net.URI;

public class MainActivity extends Activity implements DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private TextView mTextView;
    private GoogleApiClient mGoogleApiClient;

    private static String CAT_KEY = "com.alexwglenn.whichcard.rewardcategories";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String dataString = preferences.getString("RewardCategories", "");
        if (dataString.equals("")) {
            dataString = "No Data Synchronized";
        }

        final String fDataString = dataString;

        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
                mTextView.setText(fDataString);
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        Log.d("Main", "Allocating");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        Log.d("Main", "Connecting");
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.DataApi.addListener(mGoogleApiClient, this);
        Log.d("Main", "Connected");

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("Main", "connection suspended");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.DataApi.removeListener(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d("Main", "Data Changed");
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // DataItem changed
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/rewardcategories") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    String categoriesString = dataMap.getString(CAT_KEY);
                    if (mTextView != null) {
                        Log.d("Main", "Setting TV");
                        mTextView.setText(categoriesString);
                    } else {
                        Log.d("Main", "wanted to set but was null");
                    }
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    preferences.edit().putString("RewardCategories", categoriesString).commit();
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("Main", "connection failed");
    }
}
