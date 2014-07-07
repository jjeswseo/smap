package com.jjeswseo.myandroidapi;

import java.io.InputStream;

import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;
import android.widget.Toast;

public class TestActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String number="01090054306";
		Uri lookupUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,Uri.encode(number));
		
		Cursor cursor = getContentResolver().query(lookupUri,new String[]{Contacts._ID, Contacts.DISPLAY_NAME}, null, null, null);
		
		cursor.moveToNext();
		String name = cursor.getString(cursor.getColumnIndex(Contacts.DISPLAY_NAME));
		String contractId = cursor.getString(cursor.getColumnIndex(Contacts._ID));
		
		Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
		Toast.makeText(this, contractId, Toast.LENGTH_SHORT).show();
		Bitmap bm = getPhoto(Long.valueOf(contractId));
		Toast.makeText(this, String.valueOf(bm.getWidth()), Toast.LENGTH_SHORT).show();
		
	}
	
	public Bitmap getPhoto(Long contactId) {

		Uri contactPhotoUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);

		InputStream photoDataStream = Contacts.openContactPhotoInputStream(getContentResolver(),contactPhotoUri);
		
		Toast.makeText(this, photoDataStream.toString(), Toast.LENGTH_SHORT).show();
		Bitmap photo = BitmapFactory.decodeStream(photoDataStream);

		return photo;

	}
}
