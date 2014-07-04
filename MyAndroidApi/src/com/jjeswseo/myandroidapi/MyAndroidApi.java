package com.jjeswseo.myandroidapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class MyAndroidApi extends ListActivity {
	public final static String CATEGORY_SAMPLE_CODE="com.jjeswseo.myandroidapi.category.SAMPLE_CODE";
	
	private boolean DEVELOPER_MODE = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		if (DEVELOPER_MODE) {
	         StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
	                 .detectDiskReads()
	                 .detectDiskWrites()
	                 .detectNetwork()   // or .detectAll() for all detectable problems
	                 .penaltyLog()
	                 .build());
	         StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
	                 .detectLeakedSqlLiteObjects()
	                 .detectLeakedClosableObjects()
	                 .penaltyLog()
	                 .penaltyDeath()
	                 .build());
	     }
		
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_my_android_api);
		
		Intent intent = getIntent();
		String path = intent.getStringExtra("com.example.android.apis.Path");
		
		if(path == null){
			path = "";
		}
		
		
		
		//Context context 						: Activity
		//List<? extends Map<String, ?>> data 	: ������ Collection
		//int resource 							: ����Ʈ�� �� ������ ������ layout XML 
		//						  				  - to�� ���ǵ� ���̵� ��� ���ԵǾ� �־�� �Ѵ�. ���� ���� ����
		//String[] from 						: �÷����� 
		//int[] to 								: layout�� ���̵�
		SimpleAdapter sa = new SimpleAdapter(
				this, 
				getData(path), 
				android.R.layout.simple_list_item_activated_1,
				new String[]{"title"}, 
				new int[]{android.R.id.text1}
		);
		setListAdapter(sa);
		getListView().setTextFilterEnabled(true);
	}
	
	
	/**
	 * 
	 * @return
	 */
	protected List<Map<String, Object>> getData(String prefix) {
		List<Map<String, Object>> myData = new ArrayList<Map<String, Object>>();
		
		//Intent.ACTION_MAIN
		//Main Activity Intent �� �����͸� �������� �ʴ´�.
		Intent mainIntent = new Intent("com.jjeswseo.myandroidapi.action.DEMO", null);
		//mainIntent.addCategory(MyAndroidApi.CATEGORY_SAMPLE_CODE);
		
		PackageManager pm = getPackageManager();
		List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);
		
		if(list == null) return myData;
		
		String [] prefixPath;
		String prefixWithSlash = prefix;
		
		if(prefix.equals("")){
			prefixPath = null;
		}else{
			prefixPath = prefix.split("/");
			prefixWithSlash = prefix+"/";
		}
		
		int len = list.size();
		
		Map<String, Boolean> entries = new HashMap<String, Boolean>();
		
		//Intent�� ó���� �� �ִ� Activity ������ŭ ����
		for(int i=0 ; i<len ; i++){
			ResolveInfo info = list.get(i);
			
			//Manifest�� ��ϵ� Activity�� �󺧽�Ʈ���� ���´�.
			//App/Service/Remote Service Binding ����
			CharSequence labelSeq = info.loadLabel(pm);
			addItem(myData
					,labelSeq.toString()
					,activityIntent(info.activityInfo.applicationInfo.packageName, info.activityInfo.name)
			);
			
			
			
		}
		
		
		return myData;
	}

	protected void addItem(List<Map<String, Object>> data, String name, Intent intent){
		Map<String, Object> temp = new HashMap<String, Object>();
		temp.put("title", name);
		temp.put("intent", intent);
		data.add(temp);
	}
	protected Intent activityIntent(String pkg, String componentName){
		Intent result = new Intent();
		result.setClassName(pkg,componentName);
		return result;
	}
	
	@Override
    @SuppressWarnings("unchecked")
	protected void onListItemClick(ListView l, View v, int position, long id){
		Map<String, Object> map = (Map<String, Object>)l.getItemAtPosition(position);
		Intent intent = (Intent)map.get("intent");
		startActivity(intent);
		
	}
}
