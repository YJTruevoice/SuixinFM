package com.fm.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ant.liao.GifView;
import com.fm.adapter.SpeakProgramAdapter;
import com.fm.bean.SpeakProgram;
import com.fm.suixinfm.MusicPlayActivity;
import com.fm.suixinfm.R;
import com.fm.utils.Constants;
import com.fm.utils.SDCardUtlis;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
/**
 * 主播节目的Fragment
 * **/
public class SpeakProgramFragment extends Fragment{
	private View view;
	private PullToRefreshListView pullToRefreshListView;
	private ListView listView;
	private List<SpeakProgram> programs;
	private SpeakProgramAdapter adapter;
	private Bundle bundle;
	private long id;
	private String url;
	private RequestQueue mQueue;
	private ImageLoader mLoader;
	private JsonObjectRequest mJson;
	private int index=0;
	private boolean isMore;
	private LruCache<String, Bitmap> lruCache;
	private View loadView;
	private GifView gifView;
 	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view=inflater.inflate(R.layout.speak_program, null);
		pullToRefreshListView=(PullToRefreshListView) view.findViewById(R.id.speak_program_listview);
		listView=pullToRefreshListView.getRefreshableView();
		pullToRefreshListView.setMode(Mode.BOTH);//下拉刷新
		loadView=inflater.inflate(R.layout.empty_load,null);
		gifView=(GifView) loadView.findViewById(R.id.gifview);
		gifView.setGifImage(R.drawable.load);
		listView.setEmptyView(loadView);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		bundle=getArguments();
		id=bundle.getLong("_id", -1);
		//缓存
		long maxSize=Runtime.getRuntime().maxMemory()/1024; //从byte转成kb
		int cacheSize=(int)(maxSize/8);//剩余内存的
		lruCache=new LruCache<String, Bitmap>(cacheSize){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				// TODO Auto-generated method stub
				int size=value.getRowBytes()*value.getHeight()/1024; //byte－>kb
				return size; //从byte转成kb
			}
			
			@Override
			protected void entryRemoved(boolean evicted, String key,
					Bitmap oldValue, Bitmap newValue) {
				// TODO Auto-generated method stub
				SDCardUtlis.saveImage(key, oldValue);
				super.entryRemoved(evicted, key, oldValue, newValue);
			}
		};
		mQueue=Volley.newRequestQueue(getActivity());
		mLoader=new ImageLoader(mQueue, new ImageCache() {
			@Override
			public void putBitmap(String url, Bitmap bitmap) {
				// TODO Auto-generated method stub
				lruCache.put(url, bitmap);
			}
			@Override
			public Bitmap getBitmap(String url) {
				// TODO Auto-generated method stub
				Bitmap bitmap=lruCache.get(url);
				if(bitmap!=null){
					bitmap=SDCardUtlis.readImage(url); //从扩展卡中读取图片
				}
				return bitmap;
			}
		});
		programs=new ArrayList<SpeakProgram>();
		jsonPare();
		adapter=new SpeakProgramAdapter(getActivity(), programs, mLoader);
		listView.setAdapter(adapter);
		/**
		 * 上下拉刷新
		 * **/
		pullToRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				isMore=false;
				jsonPare();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				isMore=true;
				jsonPare();
			}
		});
		/**
		 * 点击事件
		 * */
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				SpeakProgram program=(SpeakProgram) adapter.getItem(position-1);
				String music_Url =program.getUrl();
				String title =program.getTitle();
				String speak =program.getSpeak();
				String background =program.getBackground();
				long _id=program.getId();
				int favnum = program.getFavnum();
				Intent intent = new Intent(getActivity(),MusicPlayActivity.class);
				intent.putExtra("url", music_Url);
				intent.putExtra("title", title);
				intent.putExtra("speak", speak);
				intent.putExtra("background", background);
				intent.putExtra("favnum", favnum);
				intent.putExtra("id", _id);
				startActivity(intent);
				
			}
		});
	}
	/**
	 * 解析
	 * */
	private void jsonPare(){
		if(isMore){
			url=String.format(Constants.SPEAK_PROGRAM,index+10,id);
		}else{
			url=String.format(Constants.SPEAK_PROGRAM,0,id);
			programs.clear();
		}
		mJson=new JsonObjectRequest(url, null, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					JSONArray data=response.getJSONArray("data");
					List<SpeakProgram> sList=JSON.parseArray(data.toString(), SpeakProgram.class);
					for(SpeakProgram s:sList){
						Log.i("msg", "--->"+s.getUrl());
					}
					programs.addAll(sList);
					adapter.notifyDataSetChanged();
					pullToRefreshListView.onRefreshComplete();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, null);
		mQueue.add(mJson);
	}

}
