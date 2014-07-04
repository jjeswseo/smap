package com.jjeswseo.myandroidapi;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class HelloActivity extends Activity {
	String TAG = "HelloActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hello);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hello, menu);
		return true;
	}
	public void helloClick(View v){
		Log.v(TAG, "helloClick Fire");
		Log.i(TAG, "Package["+getApplicationContext().getPackageName()+"]");
	}
	
	private OnClickListener mStartRecordListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			
		}
	};
}
