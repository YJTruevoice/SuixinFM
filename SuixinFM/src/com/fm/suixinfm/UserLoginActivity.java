package com.fm.suixinfm;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.fm.applacation.MyApplication;
import com.fm.bean.UserLoginInfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UserLoginActivity extends Activity {

	private EditText userNameED, passwordED;

	private Button loginButton;

	private String loginUrl = com.fm.utils.Constants.LOGIN_URL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_login_activity_layout);
		
		initViews();
		
		login();

	}

	/**
	 * 初始化Views
	 */
	private void initViews() {

		userNameED = (EditText) findViewById(R.id.editusername);
		passwordED = (EditText) findViewById(R.id.editpassword);

		loginButton = (Button) findViewById(R.id.loginconfirmbtn);

	}

	/**
	 * 点击登陆界面
	 */
	private void login() {

		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				checkEdit();

			}
		});

	}

	/**
	 * 检查用户名和密码是否为空
	 */
	private void checkEdit() {

		final ProgressDialog proDialog = new ProgressDialog(this);

		if ("".equals(userNameED.getText().toString())) {
			Toast.makeText(getApplicationContext(), R.string.check_username,
					Toast.LENGTH_LONG).show();
		} else if ("".equals(passwordED.getText().toString())) {
			Toast.makeText(getApplicationContext(), R.string.check_password,
					Toast.LENGTH_LONG).show();
		} else {

			proDialog.setMessage("正在提交内容...");
			proDialog.setIndeterminate(false);
			proDialog.setCancelable(true);
			proDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			proDialog.show();
			new Thread() {
				public void run() {
					Looper.prepare();

					requestPro();

					proDialog.dismiss();
					Looper.loop();
				};
			}.start();

		}

	}

	/**
	 * post请求过程
	 */
	private void requestPro() {

		String name = userNameED.getText().toString();
		String password = passwordED.getText().toString();
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();

		NameValuePair namePair = new BasicNameValuePair("username", name);
		NameValuePair passPair = new BasicNameValuePair("password", password);

		paramsList.add(namePair);
		paramsList.add(passPair);

		try {
			HttpEntity entity = new UrlEncodedFormEntity(paramsList);
			HttpPost post = new HttpPost(loginUrl);//
			post.setEntity(entity);// 将请求体的内容加入请求中
			HttpClient client = new DefaultHttpClient();// 建立一个用于发送请求的客户端对象
			HttpResponse response = client.execute(post);// 客户端对象执行post请求

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String resultStr = EntityUtils.toString(response.getEntity());
				Log.i("RESULTSTR", resultStr);

				String token = null;
				JSONObject result = new JSONObject(resultStr);
				String msg = result.getString("msg");
				if (msg.equals("ok")) {
					token = result.getString("token");

					UserLoginInfo loginInfo = new UserLoginInfo();
					loginInfo.setU_name(userNameED.getText().toString());
					loginInfo.setToken(token);
					MyApplication lIA = (MyApplication) getApplicationContext();
					lIA.setLoginInfo(loginInfo);

					Intent intent = new Intent(UserLoginActivity.this,
							MainActivity.class);

					startActivity(intent);

					onDestroy();

				} else if (msg.equals("error")) {
					token = "error,此用户名不存在或者正在审核中";
					Toast.makeText(this, token, Toast.LENGTH_LONG).show();
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
