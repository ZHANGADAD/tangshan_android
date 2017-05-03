package com.tangshan.gui.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.tangshan.gui.R;

public class CMProgressDialog extends Dialog {

	public CMProgressDialog(Context context, int theme) {
		super(context, R.style.dialog);
		// TODO Auto-generated constructor stub
		init();

	}

	protected CMProgressDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
		init();
	}

	public CMProgressDialog(Context context) {
		super(context, R.style.dialog);
		// TODO Auto-generated constructor stub
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.load_onlyprocess);
	}

}
