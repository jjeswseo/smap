package com.jjeswseo.myandroidapi;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class SimpleActivityBase extends FragmentActivity {
	public static final String TAG = SimpleActivityBase.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		initializeLogging();
	}
	public void initializeLogging(){
		Log.i(TAG, "LogWrapper Does'nt Applied!");
	}
}
