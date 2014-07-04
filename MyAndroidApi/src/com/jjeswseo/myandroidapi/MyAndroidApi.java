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
		//List<? extends Map<String, ?>> data 	: 데이터 Collection
		//int resource 							: 리스트의 각 라인이 보여질 layout XML 
		//						  				  - to에 정의된 아이디가 모두 포함되어 있어야 한다. 복수 지정 가능
		//String[] from 						: 컬럼네임 
		//int[] to 								: layout의 아이디
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
		//Main Activity Intent 로 데이터를 전달하지 않는다.
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
		
		//Intent를 처리할 수 있는 Activity 갯수만큼 루프
		for(int i=0 ; i<len ; i++){
			ResolveInfo info = list.get(i);
			
			//Manifest에 등록된 Activity의 라벨스트링을 얻어온다.
			//App/Service/Remote Service Binding 형식
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
