package com.jjeswseo.myandroidapi;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.provider.CallLog;
import android.util.Log;

public class RecService extends Service {
	String TAG = "RecService";
	MediaRecorder mMediaRecorder = null;
	String tmpFileName = null;
	String tmpDir = null;
	String date = null;
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
		String number = mCursor.getString(mCursor.getColumnIndex(CallLog.Calls.NUMBER));
		String type = mCursor.getString(mCursor.getColumnIndex(CallLog.Calls.TYPE));
		date = mCursor.getString(mCursor.getColumnIndex(CallLog.Calls.DATE));
		Log.i(TAG, "number,type,date["+number+"]["+type+"]["+date+"]");
		mCursor.close();
		mThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				Log.i(TAG, "Temp File["+tmpDir+File.separator+tmpFileName+"]");
				File tmpFile = new File(tmpDir+File.separator+tmpFileName);
				File saveFile = new File(tmpDir+File.separator+date+".3gp");
				tmpFile.renameTo(saveFile);
				Log.i("RecService[Runnable]","Saved File ["+tmpDir+File.separator+date+".3gp]");
			}
		});
		
		
	}
}
