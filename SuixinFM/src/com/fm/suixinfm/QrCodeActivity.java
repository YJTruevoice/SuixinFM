package com.fm.suixinfm;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.zxing.activity.CaptureActivity;

public class QrCodeActivity extends Activity {

	private TextView textView;
	private TextView codeButton;
	private Button submit;
	private String text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qrcode_layout);
		
		textView =(TextView)findViewById(R.id.codeTextId);
		codeButton =(TextView)findViewById(R.id.codeButtonId);
		submit=(Button)findViewById(R.id.QrSubmitId);
		
		//CaptureActivity类库中的Activity
		Intent intent=new Intent(getApplicationContext(), CaptureActivity.class);
		startActivityForResult(intent, 0);
		startActivity(intent);//启动相机	
		
		codeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				//CaptureActivity类库中的Activity
				Intent intent=new Intent(getApplicationContext(), CaptureActivity.class);
				startActivityForResult(intent, 0);
				startActivity(intent);//启动相机	
				
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(resultCode==RESULT_OK){
			text=data.getExtras().getString("result");
			textView.setText(text);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void onClick(View view){
		Intent intent =new Intent();
		switch (view.getId()) {
		case R.id.codeBackId:
			finish();
			break;

		case R.id.QrSubmitId:
			if(text.endsWith(".mp3")){
				intent.setClass(QrCodeActivity.this, MusicPlayActivity.class);
				intent.putExtra("url", text);
				startActivity(intent);
			}else{
				intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				Uri content_url = Uri.parse(text);
				intent.setData(content_url);
				startActivity(intent);

			}
			break;
		}
	}
}
