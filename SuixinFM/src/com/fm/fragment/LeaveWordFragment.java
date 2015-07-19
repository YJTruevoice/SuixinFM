package com.fm.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ant.liao.GifView;
import com.fm.adapter.LeaveWordAdapter;
import com.fm.bean.LeaveWord;
import com.fm.bean.User;
import com.fm.suixinfm.R;
import com.fm.utils.Constants;
import com.fm.utils.SDCardUtlis;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 留言
 * **/
public class LeaveWordFragment extends Fragment{
	private View view;
	private PullToRefreshListView pullToRefreshListView;
	private ListView listView;
	private LeaveWordAdapter adapter;
	private List<LeaveWord> list; 
	private RequestQueue mQueue;
	private ImageLoader mLoader;
	private JsonObjectRequest mJson;
	private int index=0;
	private boolean isMore;
	private long id;
	private String url;
	private Bundle bundle;
	private LruCache<String, Bitmap> lruCache;
	private View loadView;
	private GifView gifView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view=inflater.inflate(R.layout.leave_word_fr,null);
		pullToRefreshListView=(PullToRefreshListView) view.findViewById(R.id.listview_leave_word);
		listView=pullToRefreshListView.getRefreshableView();
		pullToRefreshListView.setMode(Mode.BOTH);
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
		long maxSize=Runtime.getRuntime().maxMemory()/1024; //从byte转成kb
		int cacheSize=(int)(maxSize/8);//剩余内存的
		lruCache=new LruCache<String, Bitmap>(cacheSize){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				// TODO Auto-generated method stub
				int size=value.getRowBytes()*value.getHeight()/1024; //byte－>kb
				return size; 
			}
			@Override
			protected void entryRemoved(boolean evicted, String key,
					Bitmap oldValue, Bitmap newValue) {
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
		list=new ArrayList<LeaveWord>();
		jsonPare();
		adapter=new LeaveWordAdapter(getActivity(), list, mLoader);
		listView.setAdapter(adapter);
		pullToRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				isMore=false;
				jsonPare();
			}
			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				isMore=true;
				jsonPare();
			}
		});
	}
	/***
	 * 下载数据并解析
	 * */
	private void jsonPare(){
		if(isMore){
			url=String.format(Constants.LEAVER_WORD ,index+10,id);
		}else{
			url=String.format(Constants.LEAVER_WORD,0,id);
			list.clear();
		}
		mJson=new JsonObjectRequest(url, null, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				try {
					JSONArray data=response.getJSONArray("data");
					LeaveWord word=null;
					User user=null;
					List<LeaveWord> words=new ArrayList<LeaveWord>();
					for(int i=0;i<data.length();i++){
						JSONObject object=data.getJSONObject(i);
						word=new LeaveWord();
						word.setContent(object.getString("content"));
						word.setCreated(object.getString("created"));
						JSONObject userObject=object.getJSONObject("user");
						user=new User();
						user.setAvatar(userObject.getString("avatar"));
						user.setNickname(userObject.getString("nickname"));
						word.setUser(user);
						words.add(word);
					}
					list.addAll(words);
					adapter.notifyDataSetChanged();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		},null);
		mQueue.add(mJson);
	}
}
