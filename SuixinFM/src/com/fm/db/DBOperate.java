package com.fm.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/***
 * 数据库操作类
 * **/
public class DBOperate {
	public static String columns_collect[] = { DBConstas.TAB_ID,
			DBConstas.TAB_COLLECT_BG, DBConstas.TAB_COLLECT_FAVNUM,
			DBConstas.TAB_COLLECT_id, DBConstas.TAB_COLLECT_MP3,
			DBConstas.TAB_COLLECT_SPEAK, DBConstas.TAB_COLLECT_TITLE };
	public static String columns_histroy[] = { DBConstas.TAB_ID,
	/*
	 * DBConstas.TAB_HISTROY_BG, DBConstas.TAB_HISTROY_FAVNUM,
	 * DBConstas.TAB_HISTROY_id, DBConstas.TAB_HISTROY_MP3,
	 * DBConstas.TAB_HISTROY_SPEAK, DBConstas.TAB_HISTROY_TITLE
	 */
	DBConstas.TAB_HISTROY_SEARCH_TEXT };
	private static SQLiteDatabase database;
	private static ContentValues values;

	/**
	 * 收藏数据,收藏表
	 * **/
	public static boolean collectData(DBHelper dbHelper, String url,
			String title, String speak, String image, long id, int favnum) {
		database = dbHelper.getWritableDatabase();
		values = new ContentValues();
		// 保存之前先查询
		Cursor cursor = database.query(DBConstas.TAB_COLLECT, columns_collect,
				DBConstas.TAB_COLLECT_MP3 + " = ?", new String[] { url }, null,
				null, null);
		if (!cursor.moveToNext()) {
			values.put(DBConstas.TAB_COLLECT_BG, "" + image);
			values.put(DBConstas.TAB_COLLECT_FAVNUM, "" + favnum);
			values.put(DBConstas.TAB_COLLECT_id, "" + id);
			values.put(DBConstas.TAB_COLLECT_MP3, "" + url);
			values.put(DBConstas.TAB_COLLECT_SPEAK, "" + speak);
			values.put(DBConstas.TAB_COLLECT_TITLE, "" + title);
			database.insert(DBConstas.TAB_COLLECT, null, values);
			Log.i("size", "" + values.size());
			return true;
		} else {
			// 删除
			database.delete(DBConstas.TAB_COLLECT, null, null);
			return false;
		}
	}

	/**
	 * 查询数据,.收藏表
	 * **/
	public static Cursor selectDB(DBHelper dbHelper) {
		database = dbHelper.getWritableDatabase();
		Cursor cursor = database.query(DBConstas.TAB_COLLECT, columns_collect,
				null, null, null, null, null);
		return cursor;
	}

	/***
	 * 删除数据,收藏表
	 * */

	public static int deleteDB(DBHelper dbHelper, String url) {
		database = dbHelper.getWritableDatabase();
		int id = database.delete(DBConstas.TAB_COLLECT,
				DBConstas.TAB_COLLECT_MP3 + " = ?", new String[] { url });
		return id;
	}

	/**
	 * 插入数据到历史记录表
	 * **/

	public static boolean insertHistroyTab(DBHelper dbHelper,/* List<Search> list */
			String text) {
		database = dbHelper.getWritableDatabase();
		values = new ContentValues();
		/*
		 * for(Search s:list){ Cursor
		 * cursor=database.query(DBConstas.TAB_COLLECT, columns_histroy,
		 * DBConstas.TAB_COLLECT_MP3+" = ?", new String[]{s.getUrl()},
		 * null,null, null); if(!cursor.moveToNext()){
		 * values.put(DBConstas.TAB_HISTROY_BG, ""+s.getBackground());
		 * values.put(DBConstas.TAB_HISTROY_FAVNUM, ""+s.getFavnum());
		 * values.put(DBConstas.TAB_HISTROY_id, ""+s.getId());
		 * values.put(DBConstas.TAB_HISTROY_MP3, ""+s.getUrl());
		 * values.put(DBConstas.TAB_HISTROY_SPEAK,""+s.getSpeak());
		 * values.put(DBConstas.TAB_HISTROY_TITLE, ""+s.getTitle());
		 * database.insert(DBConstas.TAB_HISTROY, null, values); } }
		 */
		Cursor cursor = database.query(DBConstas.TAB_HISTROY, columns_histroy,
				DBConstas.TAB_HISTROY_SEARCH_TEXT + " = ?",
				new String[] { text }, null, null, null);
		if (!cursor.moveToNext()) {
			values.put(DBConstas.TAB_HISTROY_SEARCH_TEXT, "" + text);
			database.insert(DBConstas.TAB_HISTROY, null, values);
		}
		// 保存之前先查询
		return false;
	}

	/**
	 * 从历史记录表中查询数据
	 * */

	/**
	 * 查询数据,.收藏表
	 * **/
	public static List<String> selectHistroy(DBHelper dbHelper, String text) {
		database = dbHelper.getWritableDatabase();
		List<String> strings = new ArrayList<String>();
		Cursor cursor = database.query(DBConstas.TAB_HISTROY, columns_histroy,
				DBConstas.TAB_HISTROY_SEARCH_TEXT + " = ?",
				new String[] { text }, null, null, null);
		while (!cursor.moveToNext()) {
			String s = cursor.getString(cursor
					.getColumnIndex(DBConstas.TAB_HISTROY_SEARCH_TEXT));
			strings.add(s);
		}
		return strings;
	}
}
