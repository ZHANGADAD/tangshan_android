package com.tangshan.gui.view;


import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.tangshan.gui.R;
import com.tangshan.gui.gloable.Gloable;
import com.tangshan.gui.http.CMHttpClient;
import com.tangshan.gui.http.CMHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class CMKandianDialog extends Dialog {

	private String conditon;
	private TextView imageViewTime;
	private TextView imageView1;

	public CMKandianDialog(Context context) {
		super(context, R.style.self_dialog);
		init();
	}

	public CMKandianDialog(Context context, int theme) {
		super(context, R.style.self_dialog);
		init();
	}

	public CMKandianDialog(Context context, String condition) {
		super(context, R.style.self_dialog);
		this.conditon = condition;
		init();
	}

	private void init() {
		try {
			setContentView(R.layout.cm_kandian_details);
			setCancelable(true);
			setCanceledOnTouchOutside(true);

			findViewById(R.id.llall).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dismiss();
						}
					});

			ImageView imageView = (ImageView) findViewById(R.id.ivHeaderImageView);
			imageView.setBackgroundResource(R.drawable.daohangicon);
			findViewById(R.id.ivShareImageView).setVisibility(View.GONE);
			JSONObject jsonObject = new JSONObject(conditon);
			TextView tvLocation = (TextView) findViewById(R.id.tvHeaderTitle);
			tvLocation.setText(jsonObject.getJSONObject("basic").getString(
					"stationName"));

			TextView textView = (TextView) findViewById(R.id.tvTitle);
			textView.setText("今日看点");
			imageViewTime = (TextView) findViewById(R.id.imageView);
			imageView1 = (TextView) findViewById(R.id.imageView1);

			Window w = this.getWindow();
			LayoutParams wl = w.getAttributes();
			wl.height = LayoutParams.MATCH_PARENT;
			wl.width = LayoutParams.MATCH_PARENT;
			wl.dimAmount = 0.5f;
			wl.gravity = Gravity.CENTER;
			w.setAttributes(wl);
			w.addFlags(LayoutParams.FLAG_DIM_BEHIND);

			getDaasa();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void getDaasa() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		CMHttpClient.getInstance().get(Gloable.KANDIANURL, params,
				new CMHttpResponseHandler(false) {
					@Override
					public void onSuccess(int code, Header[] header, byte[] data) {
						// TODO Auto-generated method stub
						super.onSuccess(code, header, data);
						try {
							JSONObject object = new JSONObject(new String(data));
							imageViewTime.setText(object.getString("datatime"));
							imageView1.setText(object.getString("contents"));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int code, Header[] header,
							byte[] data, Throwable throwable) {
						// TODO Auto-generated method stub
						super.onFailure(code, header, data, throwable);
					}
				});
	}
}
