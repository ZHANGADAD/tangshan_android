package com.tangshan.gui.view;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.tangshan.gui.R;
import com.tangshan.gui.preference.CMPreference;
import com.tangshan.gui.util.Util;

public class CMTianqiDetailsDialog extends Dialog {

	private String conditon, adfs;
	Context context;
	int index;
	private CMPreference preference;

	public CMTianqiDetailsDialog(Context context) {
		super(context, R.style.self_dialog);
		this.context = context;
		init();
	}

	public CMTianqiDetailsDialog(Context context, int theme) {
		super(context, R.style.self_dialog);
		this.context = context;
		init();
	}

	public CMTianqiDetailsDialog(Context context, String afasfsf,
			String condition) {
		super(context, R.style.self_dialog);
		this.conditon = condition;
		this.adfs = afasfsf;
		index = -1;
		this.context = context;
		init();
	}

	public CMTianqiDetailsDialog(Context context, String afasfsf,
			String condition, int index) {
		// TODO Auto-generated constructor stub
		super(context, R.style.self_dialog);
		this.conditon = condition;
		this.adfs = afasfsf;
		this.context = context;
		this.index = index;
		init();
	}

	private void init() {
		try {
			setContentView(R.layout.cm_tianqi_details);
			setCancelable(true);
			setCanceledOnTouchOutside(true);
			preference = new CMPreference(context);
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

			JSONObject jsonObject = new JSONObject(adfs);
			String name = jsonObject.getJSONObject("basic").getString(
					"stationName");
			TextView tvLocation = (TextView) findViewById(R.id.tvHeaderTitle);
			if (index != -1) {
				tvLocation.setCompoundDrawables(null, null, null, null);
			} else {
				String locationCity = preference.getLocationCityInfo()
						.getsName();
				// String defaldaf = preference.getDefautlCityInfo().getsName();
				// if (defaldaf.equals(name)) {
				// Drawable drawable = context.getResources().getDrawable(
				// R.drawable.homda);
				// drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				// drawable.getIntrinsicHeight());
				// tvLocation.setCompoundDrawables(null, null, null, null);
				// } else
				if (locationCity.equals(name)) {
					Drawable drawable = context.getResources().getDrawable(
							R.drawable.location);
					drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
							drawable.getIntrinsicHeight());
					tvLocation.setCompoundDrawables(drawable, null, null, null);
				} else {
					tvLocation.setCompoundDrawables(null, null, null, null);
				}
			}
			tvLocation.setText(name);
			TextView tvTempture = (TextView) findViewById(R.id.tvTempture);
			Util.setTempreTure(
					jsonObject.getJSONObject("now").getString("tmp"),
					tvTempture);

			TextView tvQingyin = (TextView) findViewById(R.id.tvQingyin);
			tvQingyin.setText(jsonObject.getJSONArray("daily_forecast")
					.getJSONObject(0).getString("txt_d"));
			TextView tvFeng = (TextView) findViewById(R.id.tvFeng);
			tvFeng.setText(jsonObject.getJSONObject("now").getString(
					"wind_dir_txt")
					+ " "
					+ jsonObject.getJSONObject("now").getString("wind_sc")
					+ "级");
			TextView tvShidu = (TextView) findViewById(R.id.tvShidu);
			tvShidu.setText(jsonObject.getJSONObject("now").getString("hum")
					+ "%");
			TextView tvQiya = (TextView) findViewById(R.id.tvQiya);
			tvQiya.setText(jsonObject.getJSONObject("now").getString("pres")
					+ "hpa");
			TextView tvJiangshuiliang = (TextView) findViewById(R.id.tvJiangshuiliang);
			tvJiangshuiliang.setText(jsonObject.getJSONObject("now").getString(
					"pcpn")
					+ "mm");
			TextView tvRichu = (TextView) findViewById(R.id.tvRichu);
			tvRichu.setText(jsonObject.getJSONObject("now")
					.getString("sunrise"));
			TextView tvRiluo = (TextView) findViewById(R.id.tvRiluo);
			tvRiluo.setText(jsonObject.getJSONObject("now").getString("sunset"));
			TextView tvKongqizhiliang = (TextView) findViewById(R.id.tvKongqizhiliang);
			tvKongqizhiliang.setText(conditon);

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
}
