package com.example.android.theguardian;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by MohamedFadel on 2/3/2018.
 */

public class EventLoader extends AsyncTaskLoader<List<Event>> {

    private String url;
    public EventLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Event> loadInBackground() {
        if(url == null)
            return null;
        List<Event> eventList = QueryUtils.fetchEarthquakeData(url);
        return eventList;
    }
}
