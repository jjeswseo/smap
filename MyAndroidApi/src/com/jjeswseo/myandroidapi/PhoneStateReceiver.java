package com.jjeswseo.myandroidapi;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

public class PhoneStateReceiver extends BroadcastReceiver {
	private final String TAG = "PhoneStateReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		TelephonyManager telManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		int state = telManager.getCallState();
		//String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
		// state가 null이 넘어온다!
		if(state == TelephonyManager.CALL_STATE_IDLE)
		{
			context.stopService(new Intent(context, com.jjeswseo.myandroidapi.RecService.class));
		}
		else if(state == TelephonyManager.CALL_STATE_RINGING)
		{

		}
		else if(state == TelephonyManager.CALL_STATE_OFFHOOK)
		{
			context.startService(new Intent(context, com.jjeswseo.myandroidapi.RecService.class));
		}
		if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL))
		{
		}
	}

}
