package com.tangshan.gui.preference;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;

import com.tangshan.gui.bean.MCityInfo;
import com.tangshan.gui.gloable.Gloable;
import com.tangshan.gui.util.Util;

public class CMPreference {

	private SharedPreferences preferences;

	public CMPreference(Context context) {
		preferences = context.getSharedPreferences(
				CMPreference.class.getSimpleName() + "_CMPreference",
				Context.MODE_PRIVATE);
	}

	public void setChooseCityList(List<MCityInfo> afasf) {
		StringBuilder builder = new StringBuilder();
		for (MCityInfo cityInfo : afasf) {
			String conyent = cityInfo.getsNum() + "-" + cityInfo.getsName();
			builder.append(conyent).append(";");
		}
		if (builder.toString().length() > 0) {
			preferences
					.edit()
					.putString(
							"setChooseCityList",
							builder.toString().substring(0,
									builder.toString().length() - 1)).commit();
		} else {
			preferences.edit().putString("setChooseCityList", null).commit();
		}

	}

	public List<MCityInfo> getChooseCityList() {
		List<MCityInfo> list = new ArrayList<MCityInfo>();
		String conyent = preferences.getString("setChooseCityList", null);
		if (Util.isEmpty(conyent))
			return list;
		String[] cadaff = conyent.split(";");
		for (String adaF : cadaff) {
			MCityInfo info = new MCityInfo();
			info.setsNum(adaF.split("-")[0]);
			info.setsName(adaF.split("-")[1]);
			list.add(info);
			info = null;
		}
		return list;
	}

	public void setDefautlCityInfo(MCityInfo cityInfo) {
		StringBuilder builder = new StringBuilder();
		builder.append(cityInfo.getsNum()).append("-")
				.append(cityInfo.getsName());
		preferences.edit().putString("setDefautlCityInfo", builder.toString())
				.commit();
	}

	public MCityInfo getDefautlCityInfo() {
		String contwte = preferences.getString("setDefautlCityInfo", null);
		MCityInfo info = new MCityInfo();
		if (Util.isEmpty(contwte)) {
			info.setsName("唐山");
			info.setsNum("54534");
			setDefautlCityInfo(info);
		} else {
			info.setsName(contwte.split("-")[1]);
			info.setsNum(contwte.split("-")[0]);
		}
		return info;
	}

	public void setLatitude(String latitude) {
		// TODO Auto-generated method stub
		preferences.edit().putString("atitude", latitude).commit();
	}

	public String getLatitude() {
		return preferences.getString("atitude", null);
	}

	public void setLongtitude(String longtitude) {
		// TODO Auto-generated method stub
		preferences.edit().putString("setLongtitude", longtitude).commit();
	}

	public String getLongtitude() {
		return preferences.getString("setLongtitude", null);
	}

	public int getTianqi() {
		return preferences.getInt("getTianqi", 1);
	}

	public void setTianqi(int ddd) {
		preferences.edit().putInt("getTianqi", ddd).commit();
	}

	public void setQixiang(int ddd) {
		preferences.edit().putInt("getQixiang", ddd).commit();
	}

	public int getQixiang() {
		// TODO Auto-generated method stub
		return preferences.getInt("getQixiang", 1);
	}

	public int getPush() {
		return preferences.getInt("getPush", 1);
	}

	public void setPush(int ddd) {
		preferences.edit().putInt("getPush", ddd).commit();
	}

	public String getPushToken() {
		return preferences.getString("getPushToken", null);
	}

	public void setPushToken(String rirrr) {
		preferences.edit().putString("getPushToken", rirrr).commit();
	}

	public String getPushTime() {
		return preferences.getString("getPushTokenTime", "7:00-22:00");
	}

	public void setPushTime(String dasfsf) {
		preferences.edit().putString("getPushTokenTime", dasfsf).commit();
	}

	public void setGps(int i) {
		// TODO Auto-generated method stub
		preferences.edit().putInt("setGps", i).commit();
	}

	public int getGps() {
		return preferences.getInt("setGps", 1);
	}

	public void setLocationCityInfo(MCityInfo mCityInfo) {
		StringBuilder builder = new StringBuilder();
		builder.append(mCityInfo.getsNum()).append("-")
				.append(mCityInfo.getsName());
		preferences.edit().putString("getLocationCityInfo", builder.toString())
				.commit();
	}

	public MCityInfo getLocationCityInfo() {
		String contwte = preferences.getString("getLocationCityInfo", null);
		MCityInfo info = new MCityInfo();
		if (Util.isEmpty(contwte)) {
			info.setsName("唐山");
			info.setsNum("54534");
			setLocationCityInfo(info);
		} else {
			info.setsName(contwte.split("-")[1]);
			info.setsNum(contwte.split("-")[0]);
		}
		return info;
	}

	public String getShiyuanhui() {
		// TODO Auto-generated method stub
		long timeee = preferences.getLong("setShiyuanhui_yime", 0);
		if (System.currentTimeMillis() - timeee < Gloable.EXPIRETIME) {
			return preferences.getString("setShiyuanhui", null);
		} else {
			return null;
		}
	}

	public void setShiyuanhui(String consafdg) {
		preferences.edit()
				.putLong("setShiyuanhui_yime", System.currentTimeMillis())
				.putString("setShiyuanhui", consafdg).commit();
	}

	public String getTianqiContent(String key) {
		long timeee = preferences.getLong("setTianqiContent_tttt" + key, 0);
		if (System.currentTimeMillis() - timeee < Gloable.EXPIRETIME) {
			return preferences.getString("setTianqiContent" + key, null);
		} else {
			return null;
		}
	}

	public void setTianqiContent(String consafdg, String key) {
		preferences
				.edit()
				.putLong("setTianqiContent_tttt" + key,
						System.currentTimeMillis())
				.putString("setTianqiContent" + key, consafdg).commit();
	}

	public String getPushTimeEnd() {
		// TODO Auto-generated method stub
		return preferences.getString("getPushTimeEnd", "2200");
	}

	public void setPushTimeEnd(String d) {
		preferences.edit().putString("getPushTimeEnd", d).commit();
	}

	public String getPushTimeStart() {
		// TODO Auto-generated method stub
		return preferences.getString("getPushTimeStart", "0700");
	}

	public void setPushTimeStart(String d) {
		preferences.edit().putString("getPushTimeStart", d).commit();
	}
}
