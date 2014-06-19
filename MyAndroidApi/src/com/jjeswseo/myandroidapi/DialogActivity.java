package com.jjeswseo.myandroidapi;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * @author swseo
 * Floating Window를 구현하려면 
 * Manifest.xml의 Activity항목에 다음을 추가해야 적용된다.
 * android:theme="@android:style/Theme.Holo.Dialog
 */
public class DialogActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//타이틀바 왼쪽에 이미지를 넣고자 할때 호출한다.
		//getWindow().setFeatureDrawableResource() 와 같이 쓴다.
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		
		setContentView(R.layout.activity_dialog);
		getWindow().setTitle("This is just a test");
		getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, android.R.drawable.ic_dialog_alert);
		
		Button button = (Button)findViewById(R.id.add);
		button.setOnClickListener(mAddContentListener);
		
		button = (Button)findViewById(R.id.remove);
		button.setOnClickListener(mRemoveContentListener);
		
		
		
	}
	private OnClickListener mAddContentListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			LinearLayout layout = (LinearLayout)findViewById(R.id.inner_content);
			ImageView iv = new ImageView(DialogActivity.this);
			iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
			iv.setPadding(4, 4, 4, 4);
			layout.addView(iv);
			
		}
	};
	
	private OnClickListener mRemoveContentListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			LinearLayout layout = (LinearLayout)findViewById(R.id.inner_content);
			int num = layout.getChildCount();
			if(num > 0) layout.removeViewAt(num-1);
			
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dialog, menu);
		return true;
	}

}
