package com.fm.asynctask;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;
/**
 * 聊天页面异步任务
 * */
public class AsyncTaskChat extends AsyncTask<String, Void, String>{
	private String url;
	private CallBackChat listenter;
	public AsyncTaskChat(CallBackChat listenter){
		this.listenter=listenter;
	}
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		try {
			HttpClient client=new DefaultHttpClient();
			HttpGet get=new HttpGet(params[0]);
			HttpResponse response=client.execute(get);
			Log.i("info", "123");
			Log.i("info", "---->"+response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode()==200) {
				byte [] bytes=EntityUtils.toByteArray(response.getEntity());
				String data=new String(bytes);
				Log.i("info",data);
				return data;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected void onPostExecute(String result) {
		if (result!=null) {
			listenter.getDataUrl(result);
		}
		super.onPostExecute(result);
	}

}

