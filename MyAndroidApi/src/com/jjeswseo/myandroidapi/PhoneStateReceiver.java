package com.jjeswseo.myandroidapi;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneStateReceiver extends BroadcastReceiver {
	private final String TAG = "PhoneStateReceiver";
	private String incommingNumber = "";
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
			Bundle bundle = intent.getExtras();
			incommingNumber = bundle.getString("incoming_number");
			Log.i(TAG,"IncomingNumber["+incommingNumber+"]");
		}
		else if(state == TelephonyManager.CALL_STATE_OFFHOOK)
		{
			
			Intent recvIntent = new Intent(context, com.jjeswseo.myandroidapi.RecService.class);
			recvIntent.putExtra("incomingNumber", incommingNumber);
			context.startService(recvIntent);
		}
		if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL))
		{
		}
	}

}
