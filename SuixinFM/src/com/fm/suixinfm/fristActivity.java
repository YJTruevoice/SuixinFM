package com.fm.suixinfm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

public class fristActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frist_empty);
		
		boolean isFrist =readShared("isFrist");
		Intent intent = new Intent();
	//	Toast.makeText(getApplicationContext(), isFrist+"", Toast.LENGTH_LONG).show();
		if (isFrist) {
			intent.setClass(getApplicationContext(), welcomeActivity.class);
			writeShared("isFrist", false);
			
			startActivity(intent);
			finish();

			
		}else {
			intent.setClass(getApplicationContext(), SplashActivity.class);
			startActivity(intent);
			finish();
		}
		
		

	}
	
	//读取SharedPreference共享参数
	private   boolean readShared(String key){

		SharedPreferences preferences  = getSharedPreferences("key",MODE_PRIVATE);
		boolean value = preferences.getBoolean(key, true);
		
		return value;

	}
	//写入Sharepreference
	private void writeShared(String key,Boolean value){

		//获取SharedPreference的对象,并指定共享参数的文件名和读取模式
		SharedPreferences preferences = getSharedPreferences("key", MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();//获取写的对象
		editor.putBoolean(key, value);
		
		editor.commit();//提交操作
	}
	
	

}
