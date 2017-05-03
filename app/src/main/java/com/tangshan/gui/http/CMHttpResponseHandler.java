package com.tangshan.gui.http;

import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tangshan.gui.MApplication;
import com.tangshan.gui.R;

import cz.msebera.android.httpclient.Header;


public class CMHttpResponseHandler extends AsyncHttpResponseHandler {

	private boolean isShow;

	public CMHttpResponseHandler(boolean isShow) {
		// TODO Auto-generated constructor stub
		this.isShow = isShow;
	}

	@Override
	public void onFailure(int code, Header[] header, byte[] data,
						  Throwable throwable) {
		// TODO Auto-generated method stub
		if (isShow) {
			if (code == 0) {
				Toast.makeText(
						MApplication.getInstance(),
						MApplication.getInstance().getResources()
								.getString(R.string.net_timeout_str),
						Toast.LENGTH_SHORT).show();
				return;
			} else if (code == 500) {
				Toast.makeText(
						MApplication.getInstance(),
						MApplication.getInstance().getResources()
								.getString(R.string.net_exception_str),
						Toast.LENGTH_SHORT).show();
				return;
			}
		}
	}

	@Override
	public void onSuccess(int code, Header[] header, byte[] data) {
		// TODO Auto-generated method stub

	}
}
