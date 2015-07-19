package com.fm.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.NetworkImageView;
import com.baidu.mapapi.SDKInitializer;
import com.fm.bean.LeaveWord;
import com.fm.bean.Speak;
import com.fm.bean.SpeakProgram;
import com.fm.suixinfm.R;
import com.fm.utils.SDCardUtlis;

/**
 * 留言适配器
 * @author Chris
 *
 */
public class LeaveWordAdapter extends BaseAdapter {

	private Context mContext;
	private List<LeaveWord> list;
	private ImageLoader imageLoader;//网络图片加载器
	public LeaveWordAdapter(Context mContext,List<LeaveWord> list,ImageLoader imageLoader){
		this.mContext = mContext;
		this.list = list;
		this.imageLoader =imageLoader;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("unused")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder=null;
		if(viewHolder==null){
			convertView=LayoutInflater.from(mContext).inflate(R.layout.leave_word_item, null);
			viewHolder = new ViewHolder();
			viewHolder.imageView=(NetworkImageView)convertView.findViewById(R.id.leave_word_item_iv);
			viewHolder.content=(TextView)convertView.findViewById(R.id.leave_word_item_content);
			viewHolder.time=(TextView)convertView.findViewById(R.id.leave_word_item_time);
			viewHolder.userName=(TextView)convertView.findViewById(R.id.leave_word_item_username);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder)convertView.getTag();
			viewHolder.imageView.setImageResource(R.drawable.defaultimg);
			viewHolder.content.setText("");
			viewHolder.time.setText("");
			viewHolder.userName.setText("");
		}
		LeaveWord lear = list.get(position);
		viewHolder.content.setText(lear.getContent() );
		viewHolder.userName.setText(lear.getUser().getNickname());
		viewHolder.time.setText(lear.getCreated() );
		viewHolder.imageView.setImageUrl(lear.getUser().getAvatar(), imageLoader);
		return convertView;
	}
	class ViewHolder{
		private NetworkImageView imageView;
		private TextView content;
		private TextView time;
		private TextView userName;
	}

}
