package com.tangshan.gui.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.tangshan.gui.R;
import com.tangshan.gui.adapter.JiaotongAdapter;
import com.tangshan.gui.bean.MCityInfo;
import com.tangshan.gui.gloable.Gloable;
import com.tangshan.gui.http.CMHttpClient;
import com.tangshan.gui.http.CMHttpResponseHandler;
import com.tangshan.gui.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class CMListDialog extends Dialog {

	private String conditon;
	private int index;
	private ListView listView;
	private Context context;
	private ProgressBar progressBar;

	public CMListDialog(Context context) {
		super(context, R.style.self_dialog);
		this.context = context;
		init();
	}

	public CMListDialog(Context context, int theme) {
		super(context, R.style.self_dialog);
		this.context = context;
		init();
	}

	public CMListDialog(Context context, int theme, String conditon) {
		super(context, R.style.self_dialog);
		this.context = context;
		this.conditon = conditon;
		this.index = theme;
		init();
	}

	private void init() {
		try {
			setContentView(R.layout.cm_tianqi_details_list);
			setCancelable(true);
			setCanceledOnTouchOutside(true);

			progressBar = (ProgressBar) findViewById(R.id.adasf);

			ImageView imageView = (ImageView) findViewById(R.id.ivHeaderImageView);
			imageView.setBackgroundResource(R.drawable.back);
			findViewById(R.id.ivShareImageView).setVisibility(View.GONE);
			TextView tvLocation = (TextView) findViewById(R.id.tvHeaderTitle);
			Drawable drawable = context.getResources().getDrawable(
					R.drawable.homda);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());
			tvLocation.setCompoundDrawables(null, null, null, null);
			tvLocation.setText(conditon);

			findViewById(R.id.btBack).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dismiss();
						}
					});

			listView = (ListView) findViewById(R.id.llList);
			getData();

			Window w = this.getWindow();
			LayoutParams wl = w.getAttributes();
			wl.height = LayoutParams.MATCH_PARENT;
			wl.width = LayoutParams.MATCH_PARENT;
			wl.dimAmount = 0.5f;
			wl.gravity = Gravity.CENTER;
			w.setAttributes(wl);
			w.addFlags(LayoutParams.FLAG_DIM_BEHIND);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void getData() {
		String url = Gloable.LUKUANGURL;
		if (index == 1) {
			url = Gloable.XINGCHEURL;
		}

		RequestParams params = new RequestParams();
		CMHttpClient.getInstance().get(url, params,
				new CMHttpResponseHandler(true) {
					@Override
					public void onSuccess(int code, Header[] header, byte[] data) {
						// TODO Auto-generated method stub
						super.onSuccess(code, header, data);
						progressBar.setVisibility(View.GONE);
						configData(new String(data));
					}

					@Override
					public void onFailure(int code, Header[] header,
							byte[] data, Throwable throwable) {
						// TODO Auto-generated method stub
						super.onFailure(code, header, data, throwable);
						progressBar.setVisibility(View.GONE);
					}
				});

	}

	private void configData(String string) {
		// TODO Auto-generated method stub
		if (Util.isEmpty(string))
			return;
		try {
			List<MCityInfo> infos = new ArrayList<MCityInfo>();
			JSONArray jsonArray = new JSONArray(string);
			for (int i = 0; i < jsonArray.length(); i++) {
				MCityInfo info = new MCityInfo();
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				info.setsName(jsonObject.getString("area"));
				info.setContent(jsonObject.getString("info"));
				info.setsNum(jsonObject.getString("datatime"));
				infos.add(info);
				info = null;
			}
			JiaotongAdapter adapter = new JiaotongAdapter(context, infos);
			listView.setAdapter(adapter);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
