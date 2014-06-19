package com.jjeswseo.myandroidapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class PhoneStateReceiver extends BroadcastReceiver {
	private final String TAG = "PhoneStateReceiver";
	//private static int pState = TelephonyManager.CALL_STATE_IDLE;
	@Override
	public void onReceive(Context context, Intent intent) {
		
		TelephonyManager telManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		int state = telManager.getCallState();
		//Toast.makeText(context, "통화상태["+state+"].", Toast.LENGTH_SHORT).show();
		
		//Log.i(TAG, "onReceive >>>>>["+intent.getStringExtra(TelephonyManager.EXTRA_STATE)+"]");

		//String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
		// state가 null이 넘어온다!
		if(state == TelephonyManager.CALL_STATE_IDLE)
		{
			//통화 종료 후 구현 ...
			//Log.i(TAG, "state[EXTRA_STATE_IDLE]");
			Toast.makeText(context, "통화 종료.", Toast.LENGTH_SHORT).show();
			context.stopService(new Intent(context, com.jjeswseo.myandroidapi.RecService.class));
		}
		else if(state == TelephonyManager.CALL_STATE_RINGING)
		{
		    //통화 벨 울릴 시 구현 ...
			//Log.i(TAG, "state[EXTRA_STATE_RINGING]");
			Toast.makeText(context, "통화 벨.", Toast.LENGTH_SHORT).show();
		}
		else if(state == TelephonyManager.CALL_STATE_OFFHOOK)
		{
			//통화 중 상태일 때 구현 ...
			//Log.i(TAG, "state[EXTRA_STATE_OFFHOOK]");
			Toast.makeText(context, "통화 중.", Toast.LENGTH_SHORT).show();
			Log.i(TAG, "service를 호출합니다.");
			context.startService(new Intent(context, com.jjeswseo.myandroidapi.RecService.class));
		}

		if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL))
		{
			Toast.makeText(context, "발신 중.", Toast.LENGTH_SHORT).show();
			//전화를 걸때 상태 구현 ...
			//Log.i(TAG, "state[ACTION_NEW_OUTGOING_CALL]");
			//Toast.makeText(context, "전화를 걸때.", Toast.LENGTH_SHORT).show();
		}
		//Toast.makeText(context, "onReceive가 수행되었습니다.", Toast.LENGTH_SHORT).show();
		/*
		MyPhoneStateListener phoneListener = new MyPhoneStateListener();
		TelephonyManager telManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		telManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
		*/
		/*
		telManager.listen(new PhoneStateListener(){
			public void onCallStateChanged(int state, String incomingNumber){
				if(state != pState){
					if(state == TelephonyManager.CALL_STATE_IDLE){
						Toast.makeText(context, "대기상태입니다.", Toast.LENGTH_SHORT).show();
					}else if(state == TelephonyManager.CALL_STATE_RINGING){
						Toast.makeText(context, "전화가왔습니다.", Toast.LENGTH_SHORT).show();
					}else if(state == TelephonyManager.CALL_STATE_OFFHOOK){
						Toast.makeText(context, "통화중입니다.", Toast.LENGTH_SHORT).show();
					}
					pState = state;
				}
			}
		}, PhoneStateListener.LISTEN_CALL_STATE);
		if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){
			Toast.makeText(context, "발신중입니다.", Toast.LENGTH_SHORT).show();
		}
		*/
		//String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
		//Toast.makeText(context, "발신중입니다["+phoneNumber+"].", Toast.LENGTH_SHORT).show();
		//Log.i(TAG, "onReceive <<<<<["+intent.getStringExtra(TelephonyManager.EXTRA_STATE)+"]");
	}

}
