package com.jjeswseo.myandroidapi;


import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

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
		String[] projection = new String[] 
				{ ContactsContract.CommonDataKinds.Phone.CONTACT_ID, 
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, 
				ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
				ContactsContract.CommonDataKinds.Phone.NUMBER};
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		String where = ContactsContract.CommonDataKinds.Phone.IS_PRIMARY+"=?"+ " AND "
				+ ContactsContract.CommonDataKinds.Phone.IS_SUPER_PRIMARY+"=?";
		//Cursor contactCursor = getContentResolver().query(uri, null, where, new String[]{"1", "1"}, null);
		//MyUtil.logCursor(TAG, contactCursor); 
		ContactInfo info = MyUtil.getContract(getContentResolver(), "01090054306");
		Log.v(TAG, info.toString());
		String imagethumbnailuri = info.getPhotoThumbnailUri();
		String imageUri = info.getPhotoUri();
		Uri imgThmbUri = Uri.parse(imagethumbnailuri);
		Uri imgUri = Uri.parse(imageUri);
		//InputStream is = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),imgThmbUri);
		//Bitmap bm = BitmapFactory.decodeStream(is);
		ImageView iv = (ImageView)findViewById(R.id.photo);
		iv.setImageURI(imgUri);
		//iv.setImageBitmap(bm);
		
	}
}
