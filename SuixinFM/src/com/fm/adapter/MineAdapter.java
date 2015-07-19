package com.fm.adapter;

import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.baidu.platform.comapi.map.v;
import com.fm.adapter.NewAdapter.ViewHolder;
import com.fm.asynctask.CallBackImage;
import com.fm.asynctask.ImageAsncTask;
import com.fm.bean.New;
import com.fm.suixinfm.R;
import com.fm.utils.SDCardUtlis;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/***
 * 我的收藏,下载的适配器
 * */
public class MineAdapter extends BaseAdapter{
	private List<New> list;
	private Context context;
	private ListView listView;
	public MineAdapter(List<New> list,Context context,ListView listView) {
		this.list=list;
		this.context=context;
		this.listView=listView;
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

	@SuppressWarnings("unused")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder=null;
		if(viewHolder==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.mine_item, null);
			viewHolder = new ViewHolder();
			viewHolder.imageView=(ImageView)convertView.findViewById(R.id.mine_imageViewId);
			viewHolder.titleTv=(TextView)convertView.findViewById(R.id.mine_titleId);
			viewHolder.speakTv=(TextView)convertView.findViewById(R.id.mine_speakId);
			viewHolder.favnumTv=(TextView)convertView.findViewById(R.id.mine_favnumId);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder)convertView.getTag();
			viewHolder.imageView.setImageResource(R.drawable.defaultimg);
			viewHolder.titleTv.setText("");
			viewHolder.speakTv.setText("");
			viewHolder.favnumTv.setText("");
		}
		New new1=list.get(position);
		viewHolder.titleTv.setText(new1.getTitle());
		viewHolder.speakTv.setText("主播   "+new1.getSpeak());
		viewHolder.favnumTv.setText("收藏   "+new1.getFavnum());
		String image=new1.getBackground();
		viewHolder.imageView.setTag(image);
		Bitmap bitmap=SDCardUtlis.readImage(image);
		if(bitmap!=null){
			viewHolder.imageView.setImageBitmap(bitmap);
		}else{
			new ImageAsncTask(new CallBackImage() {
				@Override
				public void downImage(Bitmap bitmap,String url) {
					// TODO Auto-generated method stub
					ImageView imageView=(ImageView) listView.findViewWithTag(url);
					if(imageView!=null){
						imageView.setImageBitmap(bitmap);
					}
				}
			}).execute(image);
		}
		return convertView;
	}
	class ViewHolder{
		private ImageView imageView;
		private  TextView titleTv;
		private  TextView speakTv;
		private  TextView favnumTv;
	}
}
