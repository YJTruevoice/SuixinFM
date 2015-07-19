package com.fm.suixinfm;

import android.app.Activity;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONObject;

import com.fm.adapter.AdapterChat;
import com.fm.asynctask.AsyncTaskChat;
import com.fm.asynctask.CallBackChat;
import com.fm.bean.Chat;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class ChatActivity extends Activity implements CallBackChat,
		OnClickListener {
	private AsyncTaskChat chatAsyncTask;
	private String content_str;
	private List<Chat> list;
	private ListView listView;
	private EditText sendText;
	private Button sendButon;
	private AdapterChat adapter;
	private String[] welcome_array;
	private double curTime, oldTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_aty_chat);
		initView();
		/* 返回 */
		findViewById(R.id.iv_chat_back).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
	}

	private void initView() {

		listView = (ListView) this.findViewById(R.id.chat_listView);
		sendText = (EditText) this.findViewById(R.id.Message);
		sendButon = (Button) this.findViewById(R.id.chat_Send);
		list = new ArrayList<Chat>();
		Chat data = null;
		data = new Chat(getWelcome(), Chat.RECEIVE, getTime());
		list.add(data);
		sendButon.setOnClickListener(this);
		adapter = new AdapterChat(list, this);
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	/**
	 * 第一次加载时,机器人说的话
	 * */
	public String getWelcome() {
		String welcome = null;
		welcome_array = getResources().getStringArray(R.array.welcome_message);
		int index = (int) (Math.random() * welcome_array.length - 1);
		welcome = welcome_array[index];
		return welcome;
	}

	public String getTime() {
		curTime = System.currentTimeMillis();
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		Date date = new Date();
		String time = format.format(date);
		if (oldTime - curTime >= 5 * 60 * 1000) {
			oldTime = curTime;
			return time;
		}
		return "";
	}

	/**
	 * 返回异步任务下载的数据
	 * */
	@Override
	public void getDataUrl(String data) {
		parseText(data);
	}

	/**
	 * @param data
	 *            JSON数据的解析
	 */
	public void parseText(String data) {
		try {
			JSONObject json = new JSONObject(data);
			int code = json.getInt("code");
			String txt = json.getString("text");
			Chat listData;
			listData = new Chat(txt, Chat.RECEIVE, getTime());
			list.add(listData);
			adapter.notifyDataSetChanged();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		getTime();
		content_str = sendText.getText().toString();
		String content_str_trim = content_str.replace(" ", "");
		String content_str_trim_enter = content_str_trim.replace("\n", "");
		Chat listData;
		listData = new Chat(content_str, Chat.SEND, getTime());
		list.add(listData);
		sendText.setText("");
		adapter.notifyDataSetChanged();
		chatAsyncTask = new AsyncTaskChat(this);
		String url = "http://www.tuling123.com/openapi/api?key=e6b741d5a1a2408fdb1668c2623ec54b&info="
				+ content_str_trim_enter;
		chatAsyncTask.execute(url);
	}
}