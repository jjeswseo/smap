package com.jjeswseo.myandroidapi;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class RecListViewActivity extends Activity {
	private String TAG = RecListViewActivity.class.getSimpleName();
	private SimpleAdapter mListAdapter;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private ListView mListView;
	private ArrayList<HashMap<String,Object>> dataRows;
	private HashMap<String, Object> dataRow;
	private RecDBOpenHelper mDBOpenHelper = null;
	private SQLiteDatabase mDB = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rec_list_view);
		mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.rec_list_container);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.swipe_color_1, R.color.swipe_color_2, R.color.swipe_color_3, R.color.swipe_color_4);
		dataRows = new ArrayList<HashMap<String, Object>>();
		dataRow = new HashMap<String, Object>();
		
		
		query();
		
		
/*
		dataRow.put("contract_image", android.R.drawable.ic_dialog_info);
		dataRow.put("contract_name", "이름");
		dataRow.put("see_detail", android.R.drawable.ic_dialog_info);
		dataRow.put("contract_number", "전화번호");
		dataRow.put("call_date", "날짜");
		dataRows.add(dataRow);
	*/	
		
		//Context context 						: Activity
		//List<? extends Map<String, ?>> data 	: 데이터 Collection
		//int resource 							: 리스트의 각 라인이 보여질 layout XML 
		//						  				  - to에 정의된 아이디가 모두 포함되어 있어야 한다. 복수 지정 가능
		//String[] from 						: 컬럼네임 
		//int[] to 								: layout의 아이디
		mListAdapter = new SimpleAdapter(
				this, 
				dataRows,
				R.layout.rec_list_row, 
				new String[]{"contract_image","contract_name","see_detail","contract_number","call_date"},
				new int[]{R.id.contract_image, R.id.contract_name, R.id.see_detail,R.id.contract_number,R.id.call_date});
		Log.i(TAG,"Adapter["+mListAdapter.toString()+"]");
		mListView = (ListView)findViewById(R.id.rec_list);
		mListView.setAdapter(mListAdapter);
		Log.i(TAG,"setAdapter[]");
		
	}
	
	void open(){
		if(mDBOpenHelper == null){
			mDBOpenHelper = new RecDBOpenHelper(this);
			mDB = mDBOpenHelper.getWritableDatabase();
		}
	}
	
	void query(){
		if(mDB == null) open();
		Cursor c = null;
		c = mDB.query(
				RecDBOpenHelper.DB_TABLE_NAME,
				new String[]{"_id", "call_id", "contract_id", "call_number", "call_date", "filename"},
				null,
				null,
				null,
				null,
				null
				);
		
		dataRows = new ArrayList<HashMap<String, Object>>();
		
		while(c.moveToNext()){
			dataRow = new HashMap<String, Object>();
			
			int id = c.getInt(c.getColumnIndex("_id"));
			int callId = c.getInt(c.getColumnIndex("call_id"));
			String contractId = c.getString(c.getColumnIndex("contract_id"));
			String callNumber = c.getString(c.getColumnIndex("call_number"));
			long call_date = c.getLong(c.getColumnIndex("call_date"));
			String filename = c.getString(c.getColumnIndex("filename"));
			
			ContactInfo info = MyUtil.getContract(getContentResolver(), callNumber);
			String displayName = null;
			String contactImgUriStr = null;
			if(info != null){
				displayName = info.getDisplayName();
				contactImgUriStr = info.getPhotoUri();
				Log.i(TAG, "displayName["+displayName+"]contactImgUriStr["+contactImgUriStr+"]");
				Uri contactImgUri = null;
				if(contactImgUriStr != null){
					contactImgUri = Uri.parse(contactImgUriStr);
					dataRow.put("contract_image", contactImgUri);
				}else{
					dataRow.put("contract_image", R.drawable.ic_launcher);
				}
				
				
				
			}else{
				displayName = callNumber;
				
			}
			
			dataRow.put("contract_name", displayName);
			
			dataRow.put("see_detail", R.drawable.ic_launcher);
			dataRow.put("contract_number", callNumber);
			java.text.DateFormat format = java.text.DateFormat.getDateInstance(java.text.DateFormat.FULL);
			dataRow.put("call_date", format.format(new Date(call_date)));
			dataRows.add(dataRow);
			
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rec_service, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			default: 
				Log.v(TAG, String.valueOf(item.getItemId()));
		}
		return super.onOptionsItemSelected(item);
	}
	
	public String getContract(Long contractId) {
		String name = "";
		Cursor cursor = this.getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
				new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", 
				new String[]{String.valueOf(contractId)}, 
				null);
		if (cursor.moveToFirst()) {
			name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
		}
		else{
			name = "이름없음";
		}
		cursor.close();
		return name;
	}
	public Bitmap getPhoto(Long contactId) {
		Bitmap photo = null;
		Uri contactPhotoUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);

		InputStream photoDataStream = Contacts.openContactPhotoInputStream(getContentResolver(),contactPhotoUri); 
		if(photoDataStream != null){
			photo = BitmapFactory.decodeStream(photoDataStream);
		}else{
			photo = null;
		}
		
		return photo;

		}
}
