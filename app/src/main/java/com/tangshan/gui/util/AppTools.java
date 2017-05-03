package com.tangshan.gui.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.text.TextUtils;

public class AppTools {
	/**
	 * 判断当前是否有网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetConnected(Context context) {
		try {

			ConnectivityManager connManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connManager == null) {
				return false;
			}
			if (connManager.getActiveNetworkInfo() == null) {
				return false;
			}
			return connManager.getActiveNetworkInfo().isAvailable();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 获取唯一标shi
	 * 
	 * @param context
	 * @return
	 */
	public static String getUUID(Context context) {
		try {
			org.json.JSONObject json = new org.json.JSONObject();
			android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			String device_id = tm.getDeviceId();

			android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);

			String mac = wifi.getConnectionInfo().getMacAddress();
			json.put("mac", mac);

			if (TextUtils.isEmpty(device_id)) {
				device_id = mac;
			}

			if (TextUtils.isEmpty(device_id)) {
				device_id = android.provider.Settings.Secure.getString(
						context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
			}

			json.put("device_id", device_id);

			CMLog.i("TAG", "{\"device_id\":\"" + device_id + "\", \"mac\":\""
					+ mac + "\"}");

			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
