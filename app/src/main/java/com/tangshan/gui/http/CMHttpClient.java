package com.tangshan.gui.http;

import java.io.File;
import java.util.Map;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

public class CMHttpClient {

	protected static final AsyncHttpClient httpClient = new AsyncHttpClient();

	private static final CMHttpClient byHttpClient = new CMHttpClient();

	public static CMHttpClient getInstance() {
		httpClient.setTimeout(10);
		return byHttpClient;
	}

	public void post(String url, RequestParams params,
			CMHttpResponseHandler responseHandler) {
		if (params == null)
			params = new RequestParams();
		params.setUseJsonStreamer(true);
		httpClient.post(getAbsolutelyUrl(url), params, responseHandler);
	}

	public void get(String url, RequestParams params,
			CMHttpResponseHandler responseHandler) {
		if (params == null)
			params = new RequestParams();
		httpClient.get(getAbsolutelyUrl(url), params, responseHandler);
	}

	private String getAbsolutelyUrl(String url) {
		return "http://wx.hbweather.com.cn/jy_module/API/" + url;
	}

	public void postFile(String url, Map<String, File> params,
			CMHttpResponseHandler responseHandler) throws Exception {
		RequestParams requestParams = new RequestParams();
		requestParams.put("filedata", params.get("filedata"));
		httpClient.post(getAbsolutelyUrl(url), requestParams, responseHandler);
	}

}
