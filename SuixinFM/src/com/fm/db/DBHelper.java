package com.fm.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context context) {
		super(context, DBConstas.DB_NAME, null, DBConstas.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(DBConstas.CREATE_TAB_COLLECT);
		db.execSQL(DBConstas.CREATE_TAB_HISTROY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		if (newVersion > oldVersion) {
			db.execSQL(DBConstas.DROP_TAB_COLLECT);
			db.execSQL(DBConstas.DROP_TAB_HISTROY);
			onCreate(db);
		}
	}
}
