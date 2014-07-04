package com.jjeswseo.myandroidapi;

import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SwipeRefreshLayoutBasicFragment extends Fragment {
	private static final String LOG_TAG = SwipeRefreshLayoutBasicFragment.class.getSimpleName();
	private static final int LIST_ITEM_COUNT = 20;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private ListView mListView;
	private ArrayAdapter<String> mListAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

		View view = inflater.inflate(R.layout.swipe_refresh_layout_basic_fragment, container, false);
		mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swiperefresh);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.swipe_color_1, R.color.swipe_color_2, R.color.swipe_color_3, R.color.swipe_color_4);
		mListView = (ListView)view.findViewById(android.R.id.list);
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);
		mListAdapter = new ArrayAdapter<String>(
				getActivity()
				, android.R.layout.simple_list_item_1
				, android.R.id.text1
				, Cheeses.randomList(LIST_ITEM_COUNT));
		mListView.setAdapter(mListAdapter);
		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				Log.i(LOG_TAG, "onRefresh Call!!!!");
				initiateRefresh();
			}
		});
	}
	private void initiateRefresh(){
		Log.i(LOG_TAG, "initiateRefresh");
		new DummyBackgroundTask().execute();
	}
	private void onRefreshComplete(List<String> result) {
        Log.i(LOG_TAG, "onRefreshComplete");

        // Remove all items from the ListAdapter, and then replace them with the new items
        mListAdapter.clear();
        for (String cheese : result) {
            mListAdapter.add(cheese);
        }

        // Stop the refreshing indicator
        mSwipeRefreshLayout.setRefreshing(false);
    }

	private class DummyBackgroundTask extends AsyncTask<Void, Void, List<String>>{
		static final int TASK_DURATION = 3 * 1000; // 3 seconds
		@Override
        protected List<String> doInBackground(Void... params) {
            // Sleep for a small amount of time to simulate a background-task
            try {
                Thread.sleep(TASK_DURATION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Return a new random list of cheeses
            return Cheeses.randomList(LIST_ITEM_COUNT);
        }
		@Override
        protected void onPostExecute(List<String> result) {
            super.onPostExecute(result);

            // Tell the Fragment that the refresh has completed
            onRefreshComplete(result);
        }


	}
}
