package com.fm.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.NetworkImageView;
import com.fm.bean.Speak;
import com.fm.suixinfm.R;

/**
 * 发现中的gridVie更多主播适配器
 * @author Chris
 *
 */
public class GridViewSpeakAdapter extends BaseAdapter {

	private Context mContext;
	private List<Speak> speaks;
	private ImageLoader imageLoader;//网络图片加载器
	
	public GridViewSpeakAdapter(Context mContext,List<Speak> speaks,ImageLoader imageLoader){
		this.mContext = mContext;
		this.speaks = speaks;
		this.imageLoader =imageLoader;
	}
	
	@Override
	public int getCount() {
		return speaks.size();
	}

	@Override
	public Object getItem(int position) {
		return speaks.get(position);
	}

	@Override
	public long getItemId(int position) {
		return speaks.get(position).getId();
	}

	@SuppressWarnings("unused")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder=null;
		if(viewHolder==null){
			convertView=LayoutInflater.from(mContext).inflate(R.layout.gridview_speak_item, null);
			viewHolder = new ViewHolder();
			viewHolder.imageView=(NetworkImageView)convertView.findViewById(R.id.gridview_speak_imageViewId);
			viewHolder.titleTv=(TextView)convertView.findViewById(R.id.gridview_speak_titleId);
			viewHolder.fmnumTv=(TextView)convertView.findViewById(R.id.gridview_speak_fmnumId);
			
			convertView.setTag(viewHolder);
		}else{
			
			viewHolder=(ViewHolder)convertView.getTag();
			viewHolder.imageView.setImageResource(R.drawable.defaultimg);
			viewHolder.titleTv.setText("");
			viewHolder.fmnumTv.setText("");		
		}
		Speak speak = speaks.get(position);
		viewHolder.titleTv.setText(speak.getUsername());
		viewHolder.fmnumTv.setText("节目   "+speak.getCount());
		viewHolder.imageView.setImageUrl(speak.getAvatar(), imageLoader);
		return convertView;
	}
	class ViewHolder{
		private NetworkImageView imageView;
		private TextView titleTv;
		private TextView fmnumTv;
	}

}
