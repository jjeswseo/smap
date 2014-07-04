package com.jjeswseo.myandroidapi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RecDBOpenHelper extends SQLiteOpenHelper {
	final String LOG_TAG = RecDBOpenHelper.class.getSimpleName();
	static final String DB_NAME = "Recs.db";
	static final String DB_TABLE_NAME = "Recs";
	static final int DB_VERSION = 3;
	public RecDBOpenHelper(Context context){
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(LOG_TAG, "onCreate Call");
		db.execSQL("CREATE TABLE "+DB_TABLE_NAME +" ("
				+ "_id					INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "call_id 				INTEGER, "
				+ "call_number			TEXT, "
				+ "call_date			REAL, "
				+ "filename			   	TEXT );"
				);
		Log.i(LOG_TAG, "onCreate Executed");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(LOG_TAG, "onUpgrade Call");
		db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_NAME);
		onCreate(db);
	}

}
