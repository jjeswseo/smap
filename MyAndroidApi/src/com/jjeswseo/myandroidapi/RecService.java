package com.jjeswseo.myandroidapi;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.provider.CallLog;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;

public class RecService extends Service {
	String TAG = "RecService";
	MediaRecorder mMediaRecorder = null;
	String tmpFileName = null;
	String tmpDir = null;
	String date = null;
	long dateLong;
	String savedFileName = null;
	String number = null;
	String _id = null;
	String incomingNumber = "";
	private ExecutorService mThreadPool;
	 
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	@Override
	public void onCreate(){
		super.onCreate();
		Log.i(TAG, "onCreate");
		if(mMediaRecorder == null){
			mMediaRecorder = new MediaRecorder();
		}else{
			mMediaRecorder.reset();
		}
		mThreadPool = Executors.newCachedThreadPool();
	}
	@Override
	public int onStartCommand(Intent intent, int flag, int startId){
		incomingNumber = intent.getStringExtra("incomingNumber");
		Log.i(TAG, incomingNumber);
		String ext = Environment.getExternalStorageState();
		if (ext.equals(Environment.MEDIA_MOUNTED)) {
			tmpDir = Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		tmpFileName = "test.3gp";
		
		mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
		mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mMediaRecorder.setOutputFile(tmpDir+File.separator+tmpFileName);
		
		try {
			mMediaRecorder.prepare();
		} catch (IllegalStateException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		mMediaRecorder.start();
		return START_STICKY;
	}
	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestory Call");
		mMediaRecorder.stop();
		mMediaRecorder.release();
		Log.i(TAG, "MediaRecorder Reset!");
		Cursor mCursor = getBaseContext().getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
		mCursor.moveToFirst();
		number = mCursor.getString(mCursor.getColumnIndex(CallLog.Calls.NUMBER));
		if(mCursor.getString(mCursor.getColumnIndex(CallLog.Calls.TYPE)) == "incoming"){
			number = incomingNumber;
		}
		
		String type = mCursor.getString(mCursor.getColumnIndex(CallLog.Calls.TYPE));
		_id = mCursor.getString(mCursor.getColumnIndex(CallLog.Calls._ID));
		date = mCursor.getString(mCursor.getColumnIndex(CallLog.Calls.DATE));
		dateLong = Long.parseLong(date);
		Log.i(TAG, "number,type,date["+number+"]["+type+"]["+date+"]");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss",Locale.getDefault());
		String mDateString = sdf.format(new Date(dateLong));
		savedFileName = number+"("+mDateString+")";
		mCursor.close();
		
		
		mThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				Log.i(TAG, "Temp File["+tmpDir+File.separator+tmpFileName+"]");
				File tmpFile = new File(tmpDir+File.separator+tmpFileName);
				File saveFile = new File(tmpDir+File.separator+savedFileName+".3gp");
				tmpFile.renameTo(saveFile);
				Log.i(TAG, "rename["+savedFileName+"]");
				Log.i("RecService[Runnable]","Saved File ["+tmpDir+File.separator+savedFileName+".3gp]");
				savedFileName = tmpDir+File.separator+savedFileName+".3gp";
				
				Uri lookupUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,Uri.encode(number));
				
				Cursor cursor = getContentResolver().query(lookupUri,new String[]{Contacts._ID, Contacts.DISPLAY_NAME}, null, null, null);
				String contractId = "";
				String contractName = "";
				if(cursor.getCount() == 1){
					Log.i(TAG, cursor.getCount()+"");
					cursor.moveToNext();
					contractId = cursor.getString(cursor.getColumnIndex(Contacts._ID));
					contractName = cursor.getString(cursor.getColumnIndex(Contacts.DISPLAY_NAME));
					
				}else if(cursor.getCount() > 1){
					Log.w(TAG, "Contract Count["+cursor.getCount()+"]");
				}else{
					contractId ="";
					contractName="";
				}
				
				RecDBOpenHelper mHelper = new RecDBOpenHelper(getBaseContext());
				SQLiteDatabase mDb = mHelper.getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put("call_id", _id);
				values.put("contract_id", contractId);
				values.put("call_number", number);
				values.put("call_date", dateLong);
				values.put("filename", savedFileName);
				mDb.insert(RecDBOpenHelper.DB_TABLE_NAME, null, values);
				Log.i(TAG, "Table 1 row Created!!");
			}
		});
		
		
	}
}
