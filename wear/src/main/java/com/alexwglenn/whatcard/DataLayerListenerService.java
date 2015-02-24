package com.alexwglenn.whatcard;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.common.data.Freezable;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;
import com.squareup.otto.Produce;

import java.util.List;

/**
 * Created by aglenn on 2/23/15.
 */
public class DataLayerListenerService extends WearableListenerService {
    private static String CAT_KEY = "com.alexwglenn.whichcard.rewardcategories";

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {

        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);

        Log.d("Main", "Data Changed");
        for (DataEvent event : events) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // DataItem changed
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/rewardcategories") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    String categoriesString = dataMap.getString(CAT_KEY);

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    preferences.edit().putString("RewardCategories", categoriesString).commit();

                    BusProvider.getInstance().post(produceCategoryUpdatedEvent());
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }
    }

    @Produce
    public CategoryUpdatedEvent produceCategoryUpdatedEvent() {
        return new CategoryUpdatedEvent();
    }
}
