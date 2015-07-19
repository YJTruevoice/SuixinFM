package com.fm.suixinfm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DetailActivity extends Activity {

	private WebView webView;
	private ProgressDialog dialog;

	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_layout);
		dialog = new ProgressDialog(this);
		dialog.setTitle("提示");
		dialog.setMessage("正在拼命给你加载...");
		dialog.show();
		webView=(WebView)findViewById(R.id.webViewId);

		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.setWebViewClient(new MyWebViewClient());
		webView.getSettings().setSupportZoom(true);// 缩放  
		webView.setEnabled(true);
		webView.getSettings().setPluginState(PluginState.ON);
		webView.setWebChromeClient(new WebChromeClient());
		Intent intent = getIntent();
		String url = intent.getStringExtra("url");
		webView.loadUrl(url);
	}

	private class MyWebViewClient extends WebViewClient {

		@Override  
		public WebResourceResponse shouldInterceptRequest(WebView view,  
				String url) {  
			WebResourceResponse response = null;  
			response = super.shouldInterceptRequest(view, url);  
			return response;  
		}  

		//重写父类方法，让新打开的网页在当前的WebView中显示
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}


		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			dialog.dismiss();
		}
	}

}
