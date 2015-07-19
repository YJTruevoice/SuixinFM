package com.fm.adapter;
import java.util.List;

import com.fm.bean.Chat;
import com.fm.suixinfm.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 聊天适配器
 * */
public class AdapterChat extends BaseAdapter{
	private List<Chat> list;
	private Context context;
	private RelativeLayout layout;
	public AdapterChat(List<Chat> list,Context context){
		this.list=list;
		this.context=context;
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
		if (list.get(position).getFlag()==Chat.RECEIVE) {
			layout=(RelativeLayout) LayoutInflater.from(context).inflate(R.layout.chat_left,null);
		}
		if (list.get(position).getFlag()==Chat.SEND) {
			layout=(RelativeLayout) LayoutInflater.from(context).inflate(R.layout.chat_right,null);
		}
		TextView tv=(TextView) layout.findViewById(R.id.tv);
		tv.setText(list.get(position).getContent());
		TextView time=(TextView)layout.findViewById(R.id.time);
		time.setText(list.get(position).getTime());
		return layout;
	}

}
