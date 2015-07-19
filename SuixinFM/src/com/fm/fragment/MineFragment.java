package com.fm.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fm.adapter.MineAdapter;
import com.fm.applacation.MyApplication;
import com.fm.bean.New;
import com.fm.bean.UserLoginInfo;
import com.fm.db.DBConstas;
import com.fm.db.DBHelper;
import com.fm.db.DBOperate;
import com.fm.suixinfm.MusicPlayActivity;
import com.fm.suixinfm.R;

/**
 * 我的收藏Fragment
 * */
public class MineFragment extends Fragment {
	private View view;
	private ListView listView;
	private TextView collectBtn, downBtn, userNameShow;
	private DBHelper dbHelper;
	private List<New> list;
	private Cursor cursor;
	private MineAdapter adapter;
	private View empty;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.mine_fr, null);
		collectBtn = (TextView) view.findViewById(R.id.my_collect_btnId);
		downBtn = (TextView) view.findViewById(R.id.my_down_btnId);
		listView = (ListView) view.findViewById(R.id.listview_layoutId);

		// 设置用户名标签
		userNameShow = (TextView) view.findViewById(R.id.user_name_show);
		MyApplication application = (MyApplication) getActivity()
				.getApplicationContext();
		UserLoginInfo loginInfo = application.getLoginInfo();
		if(loginInfo != null){
			
			userNameShow.setText(loginInfo.getU_name());
		}

		empty = LayoutInflater.from(getActivity())
				.inflate(R.layout.empty, null);
		listView.setEmptyView(empty);
		collectBtn.setEnabled(false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		dbHelper = new DBHelper(getActivity());
		list = new ArrayList<New>();
		/**
		 * 我的收藏
		 * **/
		collectBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Toast.makeText(getActivity(), "收藏",
				// Toast.LENGTH_SHORT).show();
				updateData();
				collectBtn.setEnabled(false);
			}
		});
		/**
		 * 我的下载
		 * **/
		downBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				collectBtn.setEnabled(true);
				Toast.makeText(getActivity(), "你还没有下载", Toast.LENGTH_SHORT)
						.show();
			}
		});
		/**
		 * 点击听
		 * **/
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				New news = (New) adapter.getItem(position);
				String music_Url = news.getUrl();
				String title = news.getTitle();
				String speak = news.getSpeak();
				String background = news.getBackground();
				int favnum = news.getFavnum();
				Intent intent = new Intent(getActivity(),
						MusicPlayActivity.class);
				intent.putExtra("url", music_Url);
				intent.putExtra("title", title);
				intent.putExtra("speak", speak);
				intent.putExtra("background", background);
				intent.putExtra("favnum", favnum);
				intent.putExtra("id", id);
				startActivity(intent);
			}
		});
		/**
		 * 长按删除收藏
		 * **/
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				New news = (New) adapter.getItem(position);
				deleteDialog(news.getUrl(), dbHelper);
				return false;
			}
		});
	}

	/**
	 * 删除收藏数据
	 * **/
	private void deleteDialog(final String url, final DBHelper helper) {
		AlertDialog delete = null;
		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setTitle("提示")
				.setIcon(R.drawable.ic_launcher)
				.setMessage("确定要删除?")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						DBOperate.deleteDB(helper, url);
						updateData();// 刷新数据
						Toast.makeText(getActivity(), "删除成功",
								Toast.LENGTH_SHORT).show();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		delete = builder.create();
		delete.show();
	}

	@Override
	public void onStart() {
		super.onStart();
		updateData();
	}

	/**
	 * 加载数据
	 * **/
	private void updateData() {
		list.clear();
		New news = null;
		cursor = DBOperate.selectDB(dbHelper);
		while (cursor.moveToNext()) {
			news = new New();
			news.setBackground(cursor.getString(cursor
					.getColumnIndex(DBConstas.TAB_COLLECT_BG)));
			news.setFavnum(cursor.getInt(cursor
					.getColumnIndex(DBConstas.TAB_COLLECT_FAVNUM)));
			news.setId(cursor.getLong(cursor
					.getColumnIndex(DBConstas.TAB_COLLECT_id)));
			news.setUrl(cursor.getString(cursor
					.getColumnIndex(DBConstas.TAB_COLLECT_MP3)));
			news.setSpeak(cursor.getString(cursor
					.getColumnIndex(DBConstas.TAB_COLLECT_SPEAK)));
			news.setTitle(cursor.getString(cursor
					.getColumnIndex(DBConstas.TAB_COLLECT_TITLE)));
			list.add(news);
			Log.i("size", "--->" + list.size());
		}
		adapter = new MineAdapter(list, getActivity(), listView);
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
}
