package com.fm.suixinfm;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fm.adapter.SearchAdapter;
import com.fm.bean.Search;
import com.fm.db.DBConstas;
import com.fm.db.DBHelper;
import com.fm.db.DBOperate;
import com.fm.utils.Constants;

/**
 * 搜索Activity
 * */
public class SearchActivity extends Activity implements OnClickListener{
	private ListView listView;
	private List<Search> list;
	private SearchAdapter adapter;
	private RequestQueue mQueue;
	private JsonObjectRequest mJson;
	private AutoCompleteTextView searchTV;
	private Button seachStart;
	private ImageView searchBack;
	private DBHelper dbHelper;
	//private List<String> autoList;
	//private ArrayAdapter<String> arrayAdapter;
	private ProgressDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		dialog=new ProgressDialog(this);
		dialog.setMessage("正在搜索...");
		seachStart=(Button) findViewById(R.id.seachStart);
		searchTV=(AutoCompleteTextView) findViewById(R.id.searchView1);
		searchBack=(ImageView) findViewById(R.id.searchBack);
		seachStart.setOnClickListener(this);
		searchBack.setOnClickListener(this);
		dbHelper=new DBHelper(this);
		listView=(ListView)findViewById(R.id.search_listview);
		list=new ArrayList<Search>(); 
		adapter=new SearchAdapter(list, this);
		listView.setAdapter(adapter);
		mQueue=Volley.newRequestQueue(this);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Search new1=(Search) adapter.getItem(position);
				long _id =new1.getId();
				String music_Url =new1.getUrl();
				String title =new1.getTitle();
				String speak =new1.getSpeak();
				String background =new1.getBackground();
				int favnum = new1.getFavnum();
				Intent intent = new Intent(SearchActivity.this,MusicPlayActivity.class);
				intent.putExtra("url", music_Url);
				intent.putExtra("title", title);
				intent.putExtra("speak", speak);
				intent.putExtra("background", background);
				intent.putExtra("favnum", favnum);
				intent.putExtra("id", _id);
				startActivity(intent);
			}
		});
		//autoList=new ArrayList<String>();
		//autoList=DBOperate.selectHistroy(dbHelper, searchTV.getText().toString());
		//arrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, autoList);
		//searchTV.setAdapter(arrayAdapter);
		searchTV.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.seachStart:
			String text=searchTV.getText().toString();
			String tag=URLEncoder.encode(text) ;
			String url =String.format(Constants.SEARCH, tag);
			loadData(url);
			//DBOperate.insertHistroyTab(dbHelper,text);
			break;
		case R.id.searchBack:
			finish();
			break;
		}
	}
	/** 
	 * 加载数据
	 * **/
	private void loadData(String url) {
		dialog.show();
		list.clear();
		mJson=new JsonObjectRequest(url, null,new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				try {
					if(response==null){
						Toast.makeText(SearchActivity.this, "查无结果",Toast.LENGTH_SHORT).show();
					}
					JSONArray data=response.getJSONArray("data");
					List<Search> search=JSON.parseArray(data.toString(), Search.class);
					list.addAll(search);
					for(Search s:search){
						Log.i("msg", "----->"+s.getTitle());

					}
					adapter.notifyDataSetChanged();
					dialog.dismiss();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, null);
		mQueue.add(mJson);
	}
}
