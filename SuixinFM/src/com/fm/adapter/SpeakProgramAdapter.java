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
import com.fm.bean.Speak;
import com.fm.bean.SpeakProgram;
import com.fm.suixinfm.R;
import com.fm.utils.SDCardUtlis;

/**
 * 发现中的主播节目
 * @author Chris
 *
 */
public class SpeakProgramAdapter extends BaseAdapter {

	private Context mContext;
	private List<SpeakProgram> list;
	private ImageLoader imageLoader;//网络图片加载器
	public SpeakProgramAdapter(Context mContext,List<SpeakProgram> list,ImageLoader imageLoader){
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
			convertView=LayoutInflater.from(mContext).inflate(R.layout.speak_program_item, null);
			viewHolder = new ViewHolder();
			viewHolder.imageView=(NetworkImageView)convertView.findViewById(R.id.speak_program_imageViewId);
			viewHolder.titleTv=(TextView)convertView.findViewById(R.id.speak_program_titleId);
			viewHolder.fmnumTv=(TextView)convertView.findViewById(R.id.speak_program_number);
			viewHolder.name=(TextView)convertView.findViewById(R.id.speak_program_name);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder)convertView.getTag();
			viewHolder.imageView.setImageResource(R.drawable.defaultimg);
			viewHolder.titleTv.setText("");
			viewHolder.fmnumTv.setText("");
			viewHolder.name.setText("");
		}
		SpeakProgram speak = list.get(position);
		Log.i("speak", speak.getTitle());
		viewHolder.titleTv.setText(speak.getTitle());
		viewHolder.fmnumTv.setText("收藏 "+speak.getFavnum());
		viewHolder.name.setText("主播"+speak.getSpeak());
		viewHolder.imageView.setImageUrl(speak.getCover(), imageLoader);
		return convertView;
	}
	class ViewHolder{
		private NetworkImageView imageView;
		private TextView titleTv;
		private TextView fmnumTv;
		private TextView name;
	}

}
