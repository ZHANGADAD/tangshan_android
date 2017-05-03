package com.tangshan.gui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.tangshan.gui.MainActivity;
import com.tangshan.gui.R;
import com.tangshan.gui.ui.fuwu.CMFuwu;
import com.tangshan.gui.ui.settings.CMSetting;
import com.tangshan.gui.ui.shiyuanhui.CMShiyuanhui;
import com.tangshan.gui.ui.tianqi.CMTianqi;

public class CMTableView extends LinearLayout implements OnClickListener {

	private int[] textViews = new int[] { R.id.llShouye, R.id.llZaixian,
			R.id.llService, R.id.llPersernal };

	public CMTableView(Context context) {
		super(context);
		initView();

	}

	public CMTableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	private void initView() {
		View view = LayoutInflater.from(getContext()).inflate(
				R.layout.common_tabs, this);
		view.findViewById(R.id.llShouye).setOnClickListener(this);
		view.findViewById(R.id.llZaixian).setOnClickListener(this);
		view.findViewById(R.id.llService).setOnClickListener(this);
		view.findViewById(R.id.llPersernal).setOnClickListener(this);
		changeTab(1);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		Context context = getContext();
		if (context == null)
			return;
		switch (view.getId()) {
		case R.id.llShouye:
			changeTab(0);
			if (context instanceof MainActivity) {
				CMShiyuanhui fragment = CMShiyuanhui.getInstance();
				((MainActivity) context).switchContent(fragment, false);
			}
			break;
		case R.id.llZaixian:
			changeTab(1);
			if (context instanceof MainActivity) {
				CMTianqi fragment = CMTianqi.getInstance();
				((MainActivity) context).switchContent(fragment, false);
			}
			break;
		case R.id.llService:
			changeTab(2);
			if (context instanceof MainActivity) {
				CMFuwu fragment = CMFuwu.getInstance();
				((MainActivity) context).switchContent(fragment, false);
			}
			break;
		case R.id.llPersernal:
			changeTab(3);
			if (context instanceof MainActivity) {
				CMSetting fragment = CMSetting.getInstance();
				((MainActivity) context).switchContent(fragment, false);
			}
			break;

		default:
			break;
		}
	}

	public void changeTab(int index) {
		// TODO Auto-generated method stub
		for (int i = 0; i < textViews.length; i++) {
			View textView = findViewById(textViews[i]);
			if (i == index) {
				textView.setBackgroundColor(Color.parseColor("#01203d"));
			} else {
				textView.setBackgroundColor(Color.parseColor("#00000000"));
			}
		}

	}
}
