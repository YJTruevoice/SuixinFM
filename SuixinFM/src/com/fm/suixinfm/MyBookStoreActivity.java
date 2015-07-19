package com.fm.suixinfm;

import java.util.List;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyBookStoreActivity extends Activity {
	private BaiduMap baiduMap;
	private MapView mapView;
	double x, y,cx,cy;
	private PoiSearch poiSearch;
	private boolean isFirst = true;
	private TextView nameTv,addressTv,postTv,phoneTv;
	private View itemView = null;
	private int[] icons = { R.drawable.icon_marka, R.drawable.icon_markb,
			R.drawable.icon_markc, R.drawable.icon_markd,
			R.drawable.icon_marke, R.drawable.icon_markf,
			R.drawable.icon_markg, R.drawable.icon_markh,
			R.drawable.icon_marki, R.drawable.icon_markj };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baidumap);
		mapView = (MapView) findViewById(R.id.bmapView1);
		baiduMap = mapView.getMap();

		baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		baiduMap.setMyLocationEnabled(true);// 启动定位服务
		itemView= LinearLayout.inflate(getApplicationContext(), R.layout.item_layout, null);
		nameTv=(TextView)itemView.findViewById(R.id.nameId);
	
		addressTv=(TextView)itemView.findViewById(R.id.addressId);
		postTv=(TextView)itemView.findViewById(R.id.postCodeId);
		phoneTv=(TextView)itemView.findViewById(R.id.phoneId);
		itemView.setBackgroundResource(R.drawable.popup);
		itemView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				baiduMap.hideInfoWindow();

			}
		});
		com.fm.applacation.MyApplication myApplaction = (com.fm.applacation.MyApplication) getApplication();
		poiSearch = PoiSearch.newInstance();
		x = myApplaction.x;
		y = myApplaction.y;
		baiduMap.setMyLocationData(new MyLocationData.Builder().latitude(y) // 纬度
				.longitude(x) // 经度
				.accuracy(200).direction(100) // 设置方向
				.build());

		isFrist();
		poiNearbySearchOptionListeren();//搜索当前位置下的医院

		setOnGetPoiSearchResultListener();//查询的事件

		setOnMarkerClickListener();//标记的监听事件

	}


	public void poiNearbySearchOptionListeren() {
		LatLng curll = new LatLng(y, x);//当前定位的位置

		PoiNearbySearchOption option = new PoiNearbySearchOption();
		option.location(curll)// 当前的位置
		.radius(10000)// 周边的半径 单位:m
		.keyword("书店")// 关键字
		.pageNum(1)// yehao
		.pageCapacity(10);// 每页显示的最大数量
		moveTarage(curll);

		// 发起检索
		poiSearch.searchNearby(option);
	}


	public void isFrist() {
		if (isFirst) {

			MapStatusUpdate update = MapStatusUpdateFactory
					.newLatLng(new LatLng(y, x));

			baiduMap.animateMapStatus(update); // 将地图的中心点移动当前定位的位置

			isFirst = false;
		}
	}



	public void setOnMarkerClickListener() {
		baiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@SuppressLint("NewApi")
			@Override
			public boolean onMarkerClick(Marker marker) {

				Bundle extraInfo=marker.getExtraInfo();

				String string = extraInfo.getString("name");
				nameTv.setText(string);
				
				addressTv.setText(extraInfo.getString("address"));
				postTv.setText(extraInfo.getString("postCode"));
				phoneTv.setText(extraInfo.getString("phone"));


				InfoWindow infoWindow=new InfoWindow(itemView,marker.getPosition(),-50);
				Toast.makeText(getApplicationContext(), string, 2).show();
				baiduMap.showInfoWindow(infoWindow);

				return false;
			}
		});
	}

	public void setOnGetPoiSearchResultListener() {
		poiSearch
		.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {

			@Override
			public void onGetPoiResult(PoiResult result) {// 一般的检索

				List<PoiInfo> infos = result.getAllPoi();
				if (result == null || result.getAllPoi() == null) {
					Toast.makeText(getApplicationContext(), "查无结果",
							Toast.LENGTH_LONG).show();
					return;
				}

				int index=0;
				for(PoiInfo info:infos){

					Bundle extraInfo=new Bundle();
					extraInfo.putString("name", info.name);  //poi名称
					extraInfo.putString("address", info.address); //中文地址
					extraInfo.putString("postCode", info.postCode); //邮编
					extraInfo.putString("phone", info.phoneNum); //电话
					extraInfo.putString("uid", info.uid); //poi的唯一标识

					BitmapDescriptor icon=BitmapDescriptorFactory.fromResource(icons[index++]);
					OverlayOptions marker=new MarkerOptions()
					.title(info.name)
					.icon(icon)
					.extraInfo(extraInfo)
					.position(info.location);

					baiduMap.addOverlay(marker);

				}
			}

			@Override
			public void onGetPoiDetailResult(PoiDetailResult arg0) {// 详细的情况
				// TODO Auto-generated method stub
				if (arg0.error != SearchResult.ERRORNO.NO_ERROR) {
					Log.i("-->>", "URL-->检索失败");
				} else {
					Log.i("-->>", "URL-->" + arg0.getDetailUrl());

				}
			}
		});
	}

	public void markLocal(double x,double y,String palce) {
		LatLng llA = new LatLng(x, y); // 指定纬度和经度
		BitmapDescriptor iconA = BitmapDescriptorFactory
				.fromResource(R.drawable.location_arrows); // 指定marker的图标
		OverlayOptions markerA = new MarkerOptions().position(llA).icon(iconA)
				.title(palce); // 构建一个Marker对象
		baiduMap.addOverlay(markerA); // 将标注增加到地图的图层上
	}

	private void moveTarage(LatLng curLL) {
		// 设置地图新的中心点和缩放级别
		MapStatusUpdate update = MapStatusUpdateFactory
				.newMapStatus(new MapStatus.Builder().target(curLL).zoom(13)
						.build());

		// 更新地图的状态
		baiduMap.setMapStatus(update);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		mapView.onDestroy();
	}
	public void mapBack(View view){
		finish();
	}

}
