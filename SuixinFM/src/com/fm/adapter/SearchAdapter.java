package com.fm.adapter;

import java.util.List;

import cn.jpush.android.ui.h;

import com.fm.bean.Search;
import com.fm.suixinfm.R;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/***
 * 搜索适配器
 * */
public class SearchAdapter extends BaseAdapter {
	private List<Search> list;
	private Context context;
	public SearchAdapter(List<Search> list,Context context) {
		// TODO Auto-generated constructor stub
		this.context=context;
		this.list=list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder=null;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.search_item, null);
			holder=new ViewHolder();
			holder.title=(TextView) convertView.findViewById(R.id.search_title);
			convertView.setTag(holder);
		}
		else{
			holder=(ViewHolder) convertView.getTag();
			holder.title.setText("");
		}
		Search s=list.get(position);
		holder.title.setText(s.getTitle());
		return convertView;
	}
	class ViewHolder{
		private TextView title; 
	}
}
