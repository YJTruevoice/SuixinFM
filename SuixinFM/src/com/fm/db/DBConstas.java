package com.fm.db;

import java.io.File;

import android.os.Environment;

/**
 * 数据库收藏类
 * */
public class DBConstas {
	// 数据库名称
	public static final String DB_NAME = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator
			+ "fm.db";
	// 数据库版本号
	public static final int DB_VERSION = 2;
	// 收藏表
	public static final String TAB_COLLECT = "t_collect";
	// 历史记录表
	public static final String TAB_HISTROY = "t_histroy";
	// 收藏表的字段
	public static final String TAB_ID = "_id";
	public static final String TAB_COLLECT_MP3 = "mp3_url";
	public static final String TAB_COLLECT_id = "id";
	public static final String TAB_COLLECT_SPEAK = "speak";
	public static final String TAB_COLLECT_BG = "background";
	public static final String TAB_COLLECT_TITLE = "title";
	public static final String TAB_COLLECT_FAVNUM = "favnum";

	// 创建收藏表
	public static final String CREATE_TAB_COLLECT = "CREATE TABLE "
			+ TAB_COLLECT + "(" + TAB_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + TAB_COLLECT_MP3
			+ " TEXT," + TAB_COLLECT_id + " TEXT," + TAB_COLLECT_SPEAK
			+ " TEXT," + TAB_COLLECT_BG + " TEXT," + TAB_COLLECT_TITLE
			+ " TEXT," + TAB_COLLECT_FAVNUM + " TEXT" + ")";
	// 删除收藏表
	public static final String DROP_TAB_COLLECT = "DROP TABLE IF EXISTS "
			+ TAB_COLLECT;

	// 历史记录表的字段

	/*
	 * public static final String TAB_HISTROY_MP3="mp3_url"; public static final
	 * String TAB_HISTROY_id="id"; public static final String
	 * TAB_HISTROY_SPEAK="speak"; public static final String
	 * TAB_HISTROY_BG="background"; public static final String
	 * TAB_HISTROY_TITLE="title"; public static final String
	 * TAB_HISTROY_FAVNUM="favnum";
	 */
	public static final String TAB_HISTROY_SEARCH_TEXT = "search";

	// 创建历史记录表
	public static final String CREATE_TAB_HISTROY = "CREATE TABLE "
			+ TAB_HISTROY + "(" + TAB_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
			/*
			 * +TAB_HISTROY_MP3+" TEXT," +TAB_HISTROY_id+" TEXT,"
			 * +TAB_HISTROY_SPEAK+" TEXT," +TAB_HISTROY_TITLE+" TEXT,"
			 * +TAB_HISTROY_BG+" TEXT," +TAB_HISTROY_FAVNUM+" TEXT"
			 */
			+ TAB_HISTROY_SEARCH_TEXT + " TEXT" + ")";
	// 删除收场表
	public static final String DROP_TAB_HISTROY = "DROP TABLE IF EXISTS "
			+ TAB_HISTROY;
}
