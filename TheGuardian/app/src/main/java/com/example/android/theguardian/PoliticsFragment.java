package com.example.android.theguardian;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MohamedFadel on 2/4/2018.
 */

public class PoliticsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Event>>{

    private final String URL_QUERY =
            "http://content.guardianapis.com/search";


    private final String sectionName = "politics";
    private final int LOADER_ID = 3;
    private CustomAdapter customAdapter;
    private TextView emptystateTextView;
    private ProgressBar progressBar;
    private boolean isConnected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // check if there is internet connection
        ConnectivityManager cm =
                (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        emptystateTextView = view.findViewById(R.id.txt_empty_state);
        progressBar = view.findViewById(R.id.progress);

        LoaderManager loaderManager = getLoaderManager();
        ListView listView = view.findViewById(R.id.news_list);
        customAdapter = new CustomAdapter(getActivity(), new ArrayList<Event>());
        listView.setAdapter(customAdapter);
        listView.setEmptyView(emptystateTextView);

        if(isConnected){
            loaderManager.initLoader(LOADER_ID, null, this);
        }else{
            emptystateTextView.setText(R.string.no_connection);
            progressBar.setVisibility(View.GONE);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Event currentEvent = customAdapter.getItem(i);
                openWebPage(currentEvent.getWebUrl());
            }
        });
    }

    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    @Override
    public Loader<List<Event>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String minEventNumber = sharedPrefs.getString(
                getString(R.string.settings_min_news_key),
                getString(R.string.settings_min_news_default));

        String orderBy  = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(URL_QUERY);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("section", sectionName);
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("page-size", minEventNumber);
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("api-key", getResources().getString(R.string.user_key));

        return new EventLoader(getActivity(), uriBuilder.toString());
    }


    @Override
    public void onLoadFinished(Loader<List<Event>> loader, List<Event> events) {
        progressBar.setVisibility(View.GONE);
        emptystateTextView.setText(R.string.no_data);
        customAdapter.clear();
        if (events != null && !events.isEmpty())
            customAdapter.addAll(events);
    }

    @Override
    public void onLoaderReset(Loader<List<Event>> loader) {
        customAdapter.clear();
    }
}
