package com.jjeswseo.myandroidapi;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

public class SwipeRefreshLayoutBasic extends SimpleActivityBase {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.swipe_refresh_layout_basic);
    	
    	FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    	SwipeRefreshLayoutBasicFragment fragment = new SwipeRefreshLayoutBasicFragment();
    	transaction.replace(R.id.swipe_refresh_layout_basic_layout, fragment);
    	transaction.commit();
    }
}
