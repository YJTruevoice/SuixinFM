package com.fm.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


/**
 * 下载数据
 * */
public class HttpDown {
	public static byte [] down(String url){
		HttpClient client=new DefaultHttpClient();
		HttpGet get=new HttpGet(url);
		try {
			HttpResponse response=client.execute(get);
			if(response.getStatusLine().getStatusCode()==200){
				byte [] bytes=EntityUtils.toByteArray(response.getEntity());
				return bytes;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			client.getConnectionManager().shutdown();
		}
		return null;
	}
}
