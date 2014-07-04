package com.jjeswseo.myandroidapi;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class RecListViewActivity extends Activity {
	private String TAG = RecListViewActivity.class.getSimpleName();
	private SimpleAdapter mListAdapter;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	private ListView mListView;
	private ArrayList<HashMap<String,Object>> dataRows;
	private HashMap<String, Object> dataRow;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rec_list_view);
		mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.rec_list_container);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.swipe_color_1, R.color.swipe_color_2, R.color.swipe_color_3, R.color.swipe_color_4);
		dataRows = new ArrayList<HashMap<String, Object>>();
		dataRow = new HashMap<String, Object>();

		dataRow.put("contract_image", android.R.drawable.ic_dialog_info);
		dataRow.put("contract_name", "�̸�");
		dataRow.put("see_detail", android.R.drawable.ic_dialog_info);
		dataRow.put("contract_number", "��ȭ��ȣ");
		dataRow.put("call_date", "��¥");
		dataRows.add(dataRow);
		
		
		//Context context 						: Activity
		//List<? extends Map<String, ?>> data 	: ������ Collection
		//int resource 							: ����Ʈ�� �� ������ ������ layout XML 
		//						  				  - to�� ���ǵ� ���̵� ��� ���ԵǾ� �־�� �Ѵ�. ���� ���� ����
		//String[] from 						: �÷����� 
		//int[] to 								: layout�� ���̵�
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
}
