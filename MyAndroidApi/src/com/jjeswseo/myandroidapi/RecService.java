package com.jjeswseo.myandroidapi;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class RecService extends Service {
	String TAG = "RecService";
	MediaRecorder mMediaRecorder = null;
	String path = null;
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
		}
	}
	@Override
	public int onStartCommand(Intent intent, int flag, int startId){
		Log.i(TAG, "onStartCommand");
		Toast.makeText(this, "service start..", Toast.LENGTH_SHORT).show();
		
		String ext = Environment.getExternalStorageState();
		if (ext.equals(Environment.MEDIA_MOUNTED)) {
			path = Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		path += "/test.3gp";
		Log.i(TAG, "File ["+path+"]");
		mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
		mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mMediaRecorder.setOutputFile(path);
		Log.i(TAG, "Recording Start["+path+"]");
		try {
			mMediaRecorder.prepare();
			mMediaRecorder.start();
		} catch (IllegalStateException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return START_STICKY;
	}
	@Override
	public void onDestroy() {
		mMediaRecorder.stop();
		mMediaRecorder.release();
		Log.i(TAG, "Recording End["+path+"]");
		Toast.makeText(this, "service done..", Toast.LENGTH_SHORT).show();
	}
}
