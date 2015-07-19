package com.fm.suixinfm;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FeedBackActivity extends Activity {

	private EditText feedbackContent;
	private Button submitBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback_activity_layout);

		feedbackContent = (EditText) findViewById(R.id.edit_feedback_content);

		submitBtn = (Button) findViewById(R.id.submit_feedback_btn);

		final ProgressDialog progressDialog = new ProgressDialog(this);
		submitBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if ("".equals(feedbackContent.getText().toString().trim())) {
					Toast.makeText(getApplicationContext(), "您的反馈不能为空,请您编辑...",
							Toast.LENGTH_LONG).show();
				} else {
					progressDialog.setMessage("正在提交您的反馈信息...");
					progressDialog.setCancelable(false);// 不能用"取消"按钮关闭

					progressDialog
							.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					progressDialog.setIndeterminate(false);
					progressDialog.show();
					new Thread() {
						public void run() {
							Looper.prepare();
							try {
								sleep(3000);
								// 关闭对话框
								progressDialog.dismiss();
								Toast.makeText(getApplicationContext(),
										"已提交至服务器", Toast.LENGTH_LONG).show();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							Looper.loop();
						}
					}.start();

					feedbackContent.setText("");

				}
			}
		});

	}

}
