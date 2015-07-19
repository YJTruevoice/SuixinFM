package com.fm.music;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import cn.sharesdk.framework.d;

import com.fm.suixinfm.R;
import com.fm.utils.SDCardUtlis;

/**
 * 下载MP3的异步任务
 * */
public class DownMp3Asynctask extends AsyncTask<String, Integer, byte[]>{
	private Context context;
	private ProgressDialog dialog;
	public DownMp3Asynctask(Context context) {
		// TODO Auto-generated constructor stub
		this.context=context;
		dialog=new ProgressDialog(context);
		dialog.setMessage("正在下载...");
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); 
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		dialog.show();
	}
	@Override
	protected byte[] doInBackground(String... params) {
		// TODO Auto-generated method stub
		HttpClient httpClient=new DefaultHttpClient();
		HttpGet get=new HttpGet(params[0]);
		byte [] result=null;//返回结果是数组
		ByteArrayOutputStream oos=new ByteArrayOutputStream();
		try {
			HttpResponse httpResponse=httpClient.execute(get);
			if(httpResponse.getStatusLine().getStatusCode()==200){
				InputStream in=httpResponse.getEntity().getContent();
				byte [] buff=new byte[100];//缓冲区
				int length=0;
				int total=0;//已经下载的文件长度
				long file_long=httpResponse.getEntity().getContentLength();//文件长度
				while((length=in.read(buff))!=-1){
					total=total+length;
					int progress_value=(int)((total/(float)file_long)*100);
					publishProgress(progress_value);
					oos.write(buff, 0, length);
				}
				oos.flush();
				result=oos.toByteArray();
				SDCardUtlis.saveMp3(SDCardUtlis.getFileName(params[0]), result);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			httpClient.getConnectionManager().shutdown();
		}
		return result;
	}
	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
		dialog.setProgress(values[0]);
	}
	@Override
	protected void onPostExecute(byte[] result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		dialog.dismiss();
		Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT).show();
	}
}
