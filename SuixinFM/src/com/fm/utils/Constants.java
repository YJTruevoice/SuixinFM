package com.fm.utils;

import java.io.File;

import android.os.Environment;

public class Constants {

	/**
	 * 最新地址
	 */
	public static final String NEW_PATH = "http://bapi.xinli001.com/fm/broadcasts.json/?rows=10&key=9f3f57a7483a05ec42ecd912549276f0&offset=%d&speaker_id=0";

	/**
	 * 发现接口 心情,场景 %S 关键字:悲伤,快乐,烦躁.......
	 */

	public static final String FIND_MIND = "http://bapi.xinli001.com/fm/broadcasts.json/?tag=%s&rows=10&key=9f3f57a7483a05ec42ecd912549276f0&offset=%d&speaker_id=0";

	/**
	 * 更多主播
	 */
	public static final String FIND_SPEAK = "http://bapi.xinli001.com/fm/speakers.json/?rows=10&key=9f3f57a7483a05ec42ecd912549276f0&offset=%d";

	/**
	 * 主播节目
	 */

	public static final String SPEAK_PROGRAMME = "http://bapi.xinli001.com/fm/broadcasts.json/?rows=10&key=9f3f57a7483a05ec42ecd912549276f0&offset=0&speaker_id=%d";

	/**
	 * 主播留言
	 */

	public static final String LEAVER_WORD = "http://bapi.xinli001.com/fm/speaker_comment_list.json/?rows=10&key=9f3f57a7483a05ec42ecd912549276f0&offset=%d&speaker_id=%d";

	/**
	 * 图片存储路径
	 */
	public static final String IMAGE_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator
			+ "fm_images";

	/**
	 * 随心地址
	 * **/
	public static final String FOLLOW_HEART = "http://bapi.xinli001.com/fm/random_tags.json/?rows=3&key=9f3f57a7483a05ec42ecd912549276f0";
	public static final String FOLLOW_HEART_VIEWPAGER = "http://bapi.xinli001.com/fm/random_tags.json/?rows=10&key=9f3f57a7483a05ec42ecd912549276f0";
	/**
	 * 发现页面gridview中的主播地址
	 * 
	 * **/
	public static final String GRIDVIEW_SPEAK = "http://bapi.xinli001.com/fm/speakers.json/?rows=12&key=9f3f57a7483a05ec42ecd912549276f0&offset=0";

	// 文本请求的类型
	public static final short REQUEST_TYPE_TEXT = 1;

	// 图片请求的类型
	public static final short REQUEST_TYPE_IMAGE = 2;

	// 文章的地址
	public static final String ARTICLE = "http://bapi.xinli001.com/fm/get_article.json/?key=9f3f57a7483a05ec42ecd912549276f0&id=%d";

	/**
	 * 主播信息
	 * **/
	public static final String SPEAK_INFO = "http://bapi.xinli001.com/fm/speaker.json/?key=9f3f57a7483a05ec42ecd912549276f0&id=%d";

	/**
	 * 主播节目
	 * */
	public static final String SPEAK_PROGRAM = "http://bapi.xinli001.com/fm/broadcasts.json/?rows=10&key=9f3f57a7483a05ec42ecd912549276f0&offset=%d&speaker_id=%d";

	/**
	 * 搜索
	 * **/
	public static final String SEARCH = "http://bapi.xinli001.com/fm/broadcasts.json/?key=9f3f57a7483a05ec42ecd912549276f0&q=%s";

	// 视频连接
	public static final String VIDEO_URL = "http://qa.m.56.com/tea/api/api.opera.php?json={%22product%22%3A%22cz_musictea_phone%22%2C%22num%22%3A%2210%22%2C%22date_month%22%3A%22%22%2C%22type%22%3A%22get_opera_list_app%22%2C%22start%22%3A%220%22%2C%22signature%22%3A%2219de3f219c7efe343941b4cb71bd6cf8%22}";
	// 精品推荐
	public static final String PUSH_URL = "http://bapi.xinli001.com/rmdapp/apps.json/?rows=10&key=9f3f57a7483a05ec42ecd912549276f0&offset=%d&slug=android";

	/***
	 * 评论地址
	 * */
	public static final String COMMENT = "http://bapi.xinli001.com//fm/comments.json/?rows=10&key=9f3f57a7483a05ec42ecd912549276f0&broadcast_id=%d&offset=%d&user_id=0";

	// 登录地址
	public static final String LOGIN_URL = "http://api.jishanghui.cn/Api/Login";

}
