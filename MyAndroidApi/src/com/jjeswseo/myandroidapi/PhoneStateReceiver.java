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
		//Toast.makeText(context, "��ȭ����["+state+"].", Toast.LENGTH_SHORT).show();
		
		//Log.i(TAG, "onReceive >>>>>["+intent.getStringExtra(TelephonyManager.EXTRA_STATE)+"]");

		//String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
		// state�� null�� �Ѿ�´�!
		if(state == TelephonyManager.CALL_STATE_IDLE)
		{
			//��ȭ ���� �� ���� ...
			//Log.i(TAG, "state[EXTRA_STATE_IDLE]");
			Toast.makeText(context, "��ȭ ����.", Toast.LENGTH_SHORT).show();
			context.stopService(new Intent(context, com.jjeswseo.myandroidapi.RecService.class));
		}
		else if(state == TelephonyManager.CALL_STATE_RINGING)
		{
		    //��ȭ �� �︱ �� ���� ...
			//Log.i(TAG, "state[EXTRA_STATE_RINGING]");
			Toast.makeText(context, "��ȭ ��.", Toast.LENGTH_SHORT).show();
		}
		else if(state == TelephonyManager.CALL_STATE_OFFHOOK)
		{
			//��ȭ �� ������ �� ���� ...
			//Log.i(TAG, "state[EXTRA_STATE_OFFHOOK]");
			Toast.makeText(context, "��ȭ ��.", Toast.LENGTH_SHORT).show();
			Log.i(TAG, "service�� ȣ���մϴ�.");
			context.startService(new Intent(context, com.jjeswseo.myandroidapi.RecService.class));
		}

		if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL))
		{
			Toast.makeText(context, "�߽� ��.", Toast.LENGTH_SHORT).show();
			//��ȭ�� �ɶ� ���� ���� ...
			//Log.i(TAG, "state[ACTION_NEW_OUTGOING_CALL]");
			//Toast.makeText(context, "��ȭ�� �ɶ�.", Toast.LENGTH_SHORT).show();
		}
		//Toast.makeText(context, "onReceive�� ����Ǿ����ϴ�.", Toast.LENGTH_SHORT).show();
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
						Toast.makeText(context, "�������Դϴ�.", Toast.LENGTH_SHORT).show();
					}else if(state == TelephonyManager.CALL_STATE_RINGING){
						Toast.makeText(context, "��ȭ���Խ��ϴ�.", Toast.LENGTH_SHORT).show();
					}else if(state == TelephonyManager.CALL_STATE_OFFHOOK){
						Toast.makeText(context, "��ȭ���Դϴ�.", Toast.LENGTH_SHORT).show();
					}
					pState = state;
				}
			}
		}, PhoneStateListener.LISTEN_CALL_STATE);
		if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){
			Toast.makeText(context, "�߽����Դϴ�.", Toast.LENGTH_SHORT).show();
		}
		*/
		//String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
		//Toast.makeText(context, "�߽����Դϴ�["+phoneNumber+"].", Toast.LENGTH_SHORT).show();
		//Log.i(TAG, "onReceive <<<<<["+intent.getStringExtra(TelephonyManager.EXTRA_STATE)+"]");
	}

}
