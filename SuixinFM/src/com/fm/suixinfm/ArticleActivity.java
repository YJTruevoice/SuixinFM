package com.fm.suixinfm;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class ArticleActivity extends Activity {
	private WebView textView;
	private RequestQueue queue;
	private String url, title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acticle_layout);
		textView = (WebView) findViewById(R.id.acticle_textView);
		url = getIntent().getStringExtra("url");
		title = getIntent().getStringExtra("title");
		TextView tieleView = (TextView) findViewById(R.id.acticle_titleView);
		tieleView.setText(title);
		queue = Volley.newRequestQueue(getApplicationContext());
		final StringRequest request = new StringRequest(url,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.i("----re", url + "ssss" + response);
						try {
							JSONObject object = new JSONObject(response);
							String txtString = object.getString("data");
							// Spanned spanned = Html.fromHtml(txtString);
							textView.loadDataWithBaseURL(null, txtString,
									"text/html", "utf-8", null);

						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, null);

		queue.add(request);

	}

	public void Acticleback(View view) {
		finish();
	}

}
